/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.infrastructure.persistence.adapter;

import io.camunda.community.fnol.gcc.motor.application.port.out.FnolRepositoryPort;
import io.camunda.community.fnol.gcc.motor.domain.enums.*;
import io.camunda.community.fnol.gcc.motor.domain.model.MotorFnolCase;
import io.camunda.community.fnol.gcc.motor.infrastructure.persistence.entity.FnolCaseEntity;
import io.camunda.community.fnol.gcc.motor.infrastructure.persistence.repository.FnolCaseJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Component
public class FnolRepositoryAdapter implements FnolRepositoryPort {

    private final FnolCaseJpaRepository jpaRepository;

    public FnolRepositoryAdapter(FnolCaseJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public MotorFnolCase save(MotorFnolCase fnolCase) {
        FnolCaseEntity entity = toEntity(fnolCase);
        FnolCaseEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MotorFnolCase> findByFnolId(String fnolId) {
        return jpaRepository.findByFnolId(fnolId)
                .map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByFnolId(String fnolId) {
        return jpaRepository.existsByFnolId(fnolId);
    }

    @Override
    @Transactional
    public void updateProcessInstanceKey(String fnolId, String processInstanceKey) {
        jpaRepository.updateProcessInstanceKey(fnolId, processInstanceKey);
    }

    @Override
    @Transactional
    public void updateStatus(String fnolId, String status) {
        jpaRepository.updateStatus(fnolId, status);
    }

    /**
     * Convert domain model to JPA entity.
     */
    private FnolCaseEntity toEntity(MotorFnolCase fnolCase) {
        FnolCaseEntity entity = new FnolCaseEntity();

        entity.setFnolId(fnolCase.getFnolId());
        entity.setCountry(fnolCase.getCountry() != null ? fnolCase.getCountry().name() : null);
        entity.setMobileNumber(fnolCase.getMobileNumber());
        entity.setNationalId(fnolCase.getNationalId());
        entity.setReporterName(fnolCase.getInsuredName());
        entity.setReporterEmail(null); // Not in domain model

        entity.setPlateNumber(fnolCase.getPlateNumber());
        entity.setPlateCountry(fnolCase.getPlateCountry() != null ? fnolCase.getPlateCountry().name() : null);
        entity.setVehicleType(fnolCase.getVehicleType() != null ? fnolCase.getVehicleType().name() : "PRIVATE");
        entity.setVehicleMake(null); // Not in domain model
        entity.setVehicleModel(null); // Not in domain model
        entity.setVehicleYear(null); // Not in domain model
        entity.setVehicleColor(null); // Not in domain model

        entity.setPolicyNumber(fnolCase.getPolicyNumber());
        entity.setCoverageType(fnolCase.getCoverageType() != null ? fnolCase.getCoverageType().name() : "COMPREHENSIVE");
        entity.setFleetFlag(fnolCase.isFleetFlag());

        // Convert OffsetDateTime to LocalDate/LocalTime
        if (fnolCase.getLossDateTime() != null) {
            entity.setIncidentDate(fnolCase.getLossDateTime().toLocalDate());
            entity.setIncidentTime(fnolCase.getLossDateTime().toLocalTime());
        }
        entity.setIncidentLocation(fnolCase.getLossLocationTextOriginal());
        entity.setIncidentLatitude(null); // Not in domain model
        entity.setIncidentLongitude(null); // Not in domain model
        entity.setDescription(fnolCase.getAccidentDescriptionOriginal());

        entity.setDrivable(fnolCase.isDrivable());
        entity.setInjuries(fnolCase.hasInjuries());
        entity.setThirdPartyInvolved(false); // Not in domain model
        entity.setPoliceReportNumber(fnolCase.getPoliceReportNumber());

        entity.setPreferredLanguage(fnolCase.getLanguageCode() != null ? fnolCase.getLanguageCode().name() : "EN");
        entity.setStatus(fnolCase.getProcessStatus() != null ? fnolCase.getProcessStatus() : "SUBMITTED");
        entity.setSeverityLevel(fnolCase.getSeverityLevel());
        entity.setRoute(fnolCase.getRoute());
        entity.setProcessInstanceKey(fnolCase.getProcessInstanceKey());

        return entity;
    }

    /**
     * Convert JPA entity to domain model.
     */
    private MotorFnolCase toDomain(FnolCaseEntity entity) {
        GccCountry country = entity.getCountry() != null ? GccCountry.valueOf(entity.getCountry()) : null;
        GccCountry plateCountry = entity.getPlateCountry() != null ? GccCountry.valueOf(entity.getPlateCountry()) : country;

        // Build lossDateTime from incidentDate and incidentTime
        OffsetDateTime lossDateTime = null;
        if (entity.getIncidentDate() != null) {
            if (entity.getIncidentTime() != null) {
                lossDateTime = OffsetDateTime.of(entity.getIncidentDate(), entity.getIncidentTime(), ZoneOffset.UTC);
            } else {
                lossDateTime = entity.getIncidentDate().atStartOfDay().atOffset(ZoneOffset.UTC);
            }
        }

        // Build submittedAt from createdAt
        OffsetDateTime submittedAt = entity.getCreatedAt() != null
                ? entity.getCreatedAt().atOffset(ZoneOffset.UTC)
                : OffsetDateTime.now();

        MotorFnolCase fnolCase = MotorFnolCase.builder()
                .fnolId(entity.getFnolId())
                .country(country)
                .mobileNumber(entity.getMobileNumber())
                .nationalId(entity.getNationalId())
                .insuredName(entity.getReporterName())
                .plateNumber(entity.getPlateNumber())
                .plateCountry(plateCountry)
                .vehicleType(entity.getVehicleType() != null ? VehicleType.valueOf(entity.getVehicleType()) : VehicleType.PRIVATE)
                .policyNumber(entity.getPolicyNumber())
                .coverageType(entity.getCoverageType() != null ? CoverageType.valueOf(entity.getCoverageType()) : CoverageType.COMPREHENSIVE)
                .fleetFlag(entity.isFleetFlag())
                .lossDateTime(lossDateTime)
                .lossLocationTextOriginal(entity.getIncidentLocation())
                .accidentDescriptionOriginal(entity.getDescription())
                .drivable(entity.isDrivable())
                .injuries(entity.isInjuries())
                .policeReportNumber(entity.getPoliceReportNumber())
                .languageCode(entity.getPreferredLanguage() != null ? LanguageCode.valueOf(entity.getPreferredLanguage()) : LanguageCode.EN)
                .processStatus(entity.getStatus())
                .processInstanceKey(entity.getProcessInstanceKey())
                .submittedAt(submittedAt)
                .build();

        return fnolCase;
    }
}
