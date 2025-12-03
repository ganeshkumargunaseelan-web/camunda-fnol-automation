/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.api.controller;

import io.camunda.community.fnol.gcc.motor.api.dto.*;
import io.camunda.community.fnol.gcc.motor.application.port.in.SubmitFnolUseCase;
import io.camunda.community.fnol.gcc.motor.application.port.in.SubmitFnolUseCase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fnol")
@Tag(name = "FNOL", description = "First Notice of Loss operations")
public class FnolController {

    private static final Logger log = LoggerFactory.getLogger(FnolController.class);

    private final SubmitFnolUseCase submitFnolUseCase;

    public FnolController(SubmitFnolUseCase submitFnolUseCase) {
        this.submitFnolUseCase = submitFnolUseCase;
    }

    @PostMapping
    @Operation(summary = "Submit a new FNOL", description = "Submit a new First Notice of Loss for motor insurance")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "FNOL submitted successfully",
                    content = @Content(schema = @Schema(implementation = FnolSubmitResponse.class))),
            @ApiResponse(responseCode = "200", description = "Duplicate submission (idempotency)",
                    content = @Content(schema = @Schema(implementation = FnolSubmitResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<FnolSubmitResponse> submitFnol(
            @Valid @RequestBody FnolSubmitRequest request,
            @Parameter(description = "Idempotency key for duplicate prevention")
            @RequestHeader(value = "X-Idempotency-Key", required = false) String idempotencyKey,
            @Parameter(description = "Correlation ID for tracing")
            @RequestHeader(value = "X-Correlation-ID", required = false) String correlationId
    ) {
        log.info("Received FNOL submission request for country: {}, correlationId: {}",
                request.countryCode(), correlationId);

        FnolSubmissionCommand command = buildCommand(request, idempotencyKey, correlationId);
        FnolSubmissionResult result = submitFnolUseCase.submit(command);

        FnolSubmitResponse response;
        HttpStatus status;

        if (result.isDuplicate()) {
            response = FnolSubmitResponse.duplicate(
                    result.fnolId(),
                    result.status(),
                    result.severityLevel(),
                    result.route(),
                    result.processInstanceKey(),
                    result.createdAt()
            );
            status = HttpStatus.OK;
        } else {
            response = FnolSubmitResponse.success(
                    result.fnolId(),
                    result.status(),
                    result.severityLevel(),
                    result.route(),
                    result.processInstanceKey(),
                    result.createdAt()
            );
            status = HttpStatus.CREATED;
        }

        log.info("FNOL submission processed. FNOL ID: {}, Status: {}", result.fnolId(), status);
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/{fnolId}")
    @Operation(summary = "Get FNOL details", description = "Retrieve full details of an existing FNOL")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "FNOL found",
                    content = @Content(schema = @Schema(implementation = FnolDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "FNOL not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<FnolDetailResponse> getFnolDetail(
            @Parameter(description = "FNOL ID") @PathVariable String fnolId
    ) {
        log.debug("Fetching FNOL detail for: {}", fnolId);

        return submitFnolUseCase.getDetail(fnolId)
                .map(this::toDetailResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{fnolId}/status")
    @Operation(summary = "Get FNOL status", description = "Retrieve status of an existing FNOL")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "FNOL found",
                    content = @Content(schema = @Schema(implementation = FnolStatusResponse.class))),
            @ApiResponse(responseCode = "404", description = "FNOL not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<FnolStatusResponse> getFnolStatus(
            @Parameter(description = "FNOL ID") @PathVariable String fnolId
    ) {
        log.debug("Fetching FNOL status for: {}", fnolId);

        return submitFnolUseCase.getStatus(fnolId)
                .map(this::toStatusResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Build the submission command from the request DTO.
     */
    private FnolSubmissionCommand buildCommand(FnolSubmitRequest request,
                                                String idempotencyKey,
                                                String correlationId) {
        List<AttachmentData> attachments = request.attachments() != null
                ? request.attachments().stream()
                    .map(a -> new AttachmentData(a.url(), a.type(), a.description()))
                    .toList()
                : List.of();

        return FnolSubmissionCommand.builder()
                .idempotencyKey(idempotencyKey)
                .correlationId(correlationId)
                .countryCode(request.countryCode())
                .mobileNumber(request.mobileNumber())
                .nationalId(request.nationalId())
                .reporterName(request.reporterName())
                .reporterEmail(request.reporterEmail())
                .plateNumber(request.plateNumber())
                .plateCountry(request.plateCountry() != null ? request.plateCountry() : request.countryCode())
                .vehicleType(request.vehicleType())
                .vehicleMake(request.vehicleMake())
                .vehicleModel(request.vehicleModel())
                .vehicleYear(request.vehicleYear())
                .vehicleColor(request.vehicleColor())
                .policyNumber(request.policyNumber())
                .coverageType(request.coverageType())
                .isFleet(request.isFleet())
                .incidentDate(LocalDate.parse(request.incidentDate()))
                .incidentTime(request.incidentTime() != null ? LocalTime.parse(request.incidentTime()) : null)
                .incidentLocation(request.incidentLocation())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .description(request.description())
                .isDrivable(request.isDrivable())
                .hasInjuries(request.hasInjuries())
                .thirdPartyInvolved(request.thirdPartyInvolved())
                .policeReportNumber(request.policeReportNumber())
                .preferredLanguage(request.preferredLanguage() != null ? request.preferredLanguage() : "EN")
                .attachments(attachments)
                .build();
    }

    /**
     * Convert status result to response DTO.
     */
    private FnolStatusResponse toStatusResponse(FnolStatusResult result) {
        return new FnolStatusResponse(
                result.fnolId(),
                result.status(),
                result.severityLevel(),
                result.route(),
                result.createdAt(),
                result.updatedAt()
        );
    }

    /**
     * Convert detail result to response DTO.
     */
    private FnolDetailResponse toDetailResponse(FnolDetailResult result) {
        List<FnolDetailResponse.AttachmentResponse> attachments = result.attachments().stream()
                .map(a -> new FnolDetailResponse.AttachmentResponse(a.url(), a.type(), a.description()))
                .toList();

        return new FnolDetailResponse(
                result.fnolId(),
                result.processInstanceKey(),
                result.countryCode(),
                result.mobileNumber(),
                result.nationalId(),
                result.reporterName(),
                result.reporterEmail(),
                result.plateNumber(),
                result.vehicleType(),
                result.vehicleMake(),
                result.vehicleModel(),
                result.vehicleYear(),
                result.vehicleColor(),
                result.policyNumber(),
                result.coverageType(),
                result.isFleet(),
                result.incidentDate(),
                result.incidentTime(),
                result.incidentLocation(),
                result.latitude(),
                result.longitude(),
                result.description(),
                result.isDrivable(),
                result.hasInjuries(),
                result.thirdPartyInvolved(),
                result.policeReportNumber(),
                result.preferredLanguage(),
                attachments,
                result.status(),
                result.severityLevel(),
                result.route(),
                result.createdAt(),
                result.updatedAt()
        );
    }
}
