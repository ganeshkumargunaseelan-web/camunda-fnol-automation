/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.application.service;

import io.camunda.community.fnol.gcc.motor.application.port.in.SubmitFnolUseCase;
import io.camunda.community.fnol.gcc.motor.application.port.out.FnolRepositoryPort;
import io.camunda.community.fnol.gcc.motor.application.port.out.IdSequencePort;
import io.camunda.community.fnol.gcc.motor.application.port.out.ProcessStarterPort;
import io.camunda.community.fnol.gcc.motor.application.port.out.WebhookNotifierPort;
import io.camunda.community.fnol.gcc.motor.domain.enums.CoverageType;
import io.camunda.community.fnol.gcc.motor.domain.enums.GccCountry;
import io.camunda.community.fnol.gcc.motor.domain.enums.LanguageCode;
import io.camunda.community.fnol.gcc.motor.domain.enums.VehicleType;
import io.camunda.community.fnol.gcc.motor.domain.model.MotorFnolCase;
import io.camunda.community.fnol.gcc.motor.domain.model.SeverityFlags;
import io.camunda.community.fnol.gcc.motor.domain.service.FnolIdGenerator;
import io.camunda.community.fnol.gcc.motor.domain.service.SeverityCalculator;
import io.camunda.community.fnol.gcc.motor.domain.valueobject.FnolId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Service
public class MotorFnolService implements SubmitFnolUseCase {

    private static final Logger log = LoggerFactory.getLogger(MotorFnolService.class);

    private final FnolIdGenerator fnolIdGenerator;
    private final FnolRepositoryPort fnolRepository;
    private final ProcessStarterPort processStarter;
    private final IdSequencePort idSequence;
    private final WebhookNotifierPort webhookNotifier;
    private final GccValidationService validationService;
    private final LanguageNormalizationService languageService;
    private final IdempotencyService idempotencyService;
    private final SeverityCalculator severityCalculator;

    public MotorFnolService(
            FnolIdGenerator fnolIdGenerator,
            FnolRepositoryPort fnolRepository,
            ProcessStarterPort processStarter,
            IdSequencePort idSequence,
            WebhookNotifierPort webhookNotifier,
            GccValidationService validationService,
            LanguageNormalizationService languageService,
            IdempotencyService idempotencyService,
            SeverityCalculator severityCalculator) {
        this.fnolIdGenerator = fnolIdGenerator;
        this.fnolRepository = fnolRepository;
        this.processStarter = processStarter;
        this.idSequence = idSequence;
        this.webhookNotifier = webhookNotifier;
        this.validationService = validationService;
        this.languageService = languageService;
        this.idempotencyService = idempotencyService;
        this.severityCalculator = severityCalculator;
    }

    @Override
    @Transactional
    public FnolSubmissionResult submit(FnolSubmissionCommand command) {
        log.info("Processing FNOL submission for country: {}", command.countryCode());

        // Step 1: Check idempotency
        Optional<String> existingFnolId = idempotencyService.findExisting(command.idempotencyKey());
        if (existingFnolId.isPresent()) {
            log.info("Duplicate submission detected, returning existing FNOL: {}", existingFnolId.get());
            MotorFnolCase existingCase = fnolRepository.findByFnolId(existingFnolId.get())
                    .orElseThrow(() -> new IllegalStateException("Idempotency record exists but FNOL not found"));
            return buildResult(existingCase, true);
        }

        // Step 2: Validate all inputs
        validationService.validateAll(
                command.countryCode(),
                command.mobileNumber(),
                command.nationalId(),
                command.plateNumber(),
                command.plateCountry()
        );

        // Step 3: Normalize text fields
        String normalizedDescription = languageService.normalize(command.description());
        String normalizedLocation = languageService.normalize(command.incidentLocation());

        // Step 4: Generate FNOL ID
        long sequence = idSequence.nextValue();
        FnolId fnolId = fnolIdGenerator.generate(command.countryCode(), sequence);
        log.debug("Generated FNOL ID: {}", fnolId.getValue());

        // Step 5: Build domain objects
        GccCountry country = GccCountry.fromCode(command.countryCode())
                .orElseThrow(() -> new IllegalArgumentException("Invalid country: " + command.countryCode()));

        GccCountry plateCountry = GccCountry.fromCode(command.plateCountry()).orElse(country);

        CoverageType coverageType = parseCoverageType(command.coverageType());
        VehicleType vehicleType = parseVehicleType(command.vehicleType());
        LanguageCode preferredLanguage = parseLanguageCode(command.preferredLanguage());

        // Build loss datetime
        OffsetDateTime lossDateTime = null;
        if (command.incidentDate() != null) {
            if (command.incidentTime() != null) {
                lossDateTime = OffsetDateTime.of(command.incidentDate(), command.incidentTime(), ZoneOffset.UTC);
            } else {
                lossDateTime = command.incidentDate().atStartOfDay().atOffset(ZoneOffset.UTC);
            }
        }

        // Step 6: Create the FNOL case
        MotorFnolCase fnolCase = MotorFnolCase.builder()
                .fnolId(fnolId.getValue())
                .correlationId(command.correlationId())
                .country(country)
                .mobileNumber(command.mobileNumber())
                .nationalId(command.nationalId())
                .insuredName(command.reporterName())
                .plateNumber(command.plateNumber())
                .plateCountry(plateCountry)
                .vehicleType(vehicleType)
                .policyNumber(command.policyNumber())
                .coverageType(coverageType)
                .fleetFlag(command.isFleet())
                .lossDateTime(lossDateTime)
                .lossLocationTextOriginal(command.incidentLocation())
                .lossLocationTextNormalized(normalizedLocation)
                .accidentDescriptionOriginal(command.description())
                .accidentDescriptionNormalized(normalizedDescription)
                .drivable(command.isDrivable())
                .injuries(command.hasInjuries())
                .policeReportNumber(command.policeReportNumber())
                .languageCode(preferredLanguage)
                .processStatus("SUBMITTED")
                .submittedAt(OffsetDateTime.now())
                .build();

        // Step 7: Calculate severity
        SeverityFlags severity = severityCalculator.calculate(fnolCase);
        fnolCase.setSeverityFlags(severity);

        // Step 8: Save to database
        fnolRepository.save(fnolCase);
        log.info("FNOL case saved: {}", fnolId.getValue());

        // Step 9: Register idempotency key
        if (command.idempotencyKey() != null && !command.idempotencyKey().isBlank()) {
            idempotencyService.register(command.idempotencyKey(), fnolId.getValue());
        }

        // Step 10: Start Camunda process
        String processInstanceKey = processStarter.startFnolProcess(fnolCase);
        fnolCase.setProcessInstanceKey(processInstanceKey);
        fnolRepository.updateProcessInstanceKey(fnolId.getValue(), processInstanceKey);
        log.info("Process started for FNOL {}: {}", fnolId.getValue(), processInstanceKey);

        // Step 11: Send webhook notification (async, non-blocking)
        webhookNotifier.notifyFnolCreated(fnolCase);

        return buildResult(fnolCase, false);
    }

    @Override
    public Optional<FnolStatusResult> getStatus(String fnolId) {
        return fnolRepository.findByFnolId(fnolId)
                .map(this::buildStatusResult);
    }

    @Override
    public Optional<FnolDetailResult> getDetail(String fnolId) {
        return fnolRepository.findByFnolId(fnolId)
                .map(this::buildDetailResult);
    }

    /**
     * Build submission result from FNOL case.
     */
    private FnolSubmissionResult buildResult(MotorFnolCase fnolCase, boolean duplicate) {
        return new FnolSubmissionResult(
                fnolCase.getFnolId(),
                fnolCase.getProcessStatus(),
                fnolCase.getSeverityLevel(),
                fnolCase.getRoute(),
                fnolCase.getProcessInstanceKey(),
                toLocalDateTime(fnolCase.getSubmittedAt()),
                duplicate
        );
    }

    /**
     * Build status result from FNOL case.
     */
    private FnolStatusResult buildStatusResult(MotorFnolCase fnolCase) {
        return new FnolStatusResult(
                fnolCase.getFnolId(),
                fnolCase.getProcessStatus(),
                fnolCase.getSeverityLevel(),
                fnolCase.getRoute(),
                toLocalDateTime(fnolCase.getSubmittedAt()),
                toLocalDateTime(fnolCase.getSubmittedAt()) // Using submittedAt as updatedAt for now
        );
    }

    /**
     * Build detail result from FNOL case.
     */
    private FnolDetailResult buildDetailResult(MotorFnolCase fnolCase) {
        return new FnolDetailResult(
                fnolCase.getFnolId(),
                fnolCase.getCountry() != null ? fnolCase.getCountry().name() : null,
                fnolCase.getMobileNumber(),
                fnolCase.getNationalId(),
                fnolCase.getInsuredName(),
                null, // reporterEmail - not in domain model
                fnolCase.getPlateNumber(),
                fnolCase.getVehicleType() != null ? fnolCase.getVehicleType().name() : null,
                null, // vehicleMake
                null, // vehicleModel
                null, // vehicleYear
                null, // vehicleColor
                fnolCase.getPolicyNumber(),
                fnolCase.getCoverageType() != null ? fnolCase.getCoverageType().name() : null,
                fnolCase.isFleetFlag(),
                fnolCase.getLossDateTime() != null ? fnolCase.getLossDateTime().toLocalDate() : null,
                fnolCase.getLossDateTime() != null ? fnolCase.getLossDateTime().toLocalTime() : null,
                fnolCase.getLossLocationTextOriginal(),
                null, // latitude
                null, // longitude
                fnolCase.getAccidentDescriptionOriginal(),
                fnolCase.isDrivable(),
                fnolCase.hasInjuries(),
                false, // thirdPartyInvolved - not in domain model
                fnolCase.getPoliceReportNumber(),
                fnolCase.getLanguageCode() != null ? fnolCase.getLanguageCode().name() : null,
                List.of(), // attachments - simplified
                fnolCase.getProcessStatus(),
                fnolCase.getSeverityLevel(),
                fnolCase.getRoute(),
                fnolCase.getProcessInstanceKey(),
                toLocalDateTime(fnolCase.getSubmittedAt()),
                toLocalDateTime(fnolCase.getSubmittedAt())
        );
    }

    private CoverageType parseCoverageType(String code) {
        if (code == null || code.isBlank()) {
            return CoverageType.COMPREHENSIVE;
        }
        return CoverageType.fromValue(code).orElse(CoverageType.COMPREHENSIVE);
    }

    private VehicleType parseVehicleType(String code) {
        if (code == null || code.isBlank()) {
            return VehicleType.PRIVATE;
        }
        return VehicleType.fromValue(code).orElse(VehicleType.PRIVATE);
    }

    private LanguageCode parseLanguageCode(String code) {
        if (code == null || code.isBlank()) {
            return LanguageCode.EN;
        }
        return LanguageCode.fromCode(code).orElse(LanguageCode.EN);
    }

    private LocalDateTime toLocalDateTime(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            return LocalDateTime.now();
        }
        return offsetDateTime.toLocalDateTime();
    }
}
