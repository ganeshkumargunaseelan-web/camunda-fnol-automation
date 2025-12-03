/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.infrastructure.zeebe;

import io.camunda.community.fnol.gcc.motor.application.exception.ProcessStartException;
import io.camunda.community.fnol.gcc.motor.application.port.out.ProcessStarterPort;
import io.camunda.community.fnol.gcc.motor.domain.model.MotorFnolCase;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "camunda.client.mode", havingValue = "self-managed", matchIfMissing = false)
public class SelfManagedProcessStarter implements ProcessStarterPort {

    private static final Logger log = LoggerFactory.getLogger(SelfManagedProcessStarter.class);
    private static final String PROCESS_ID = "gcc-motor-fnol-process";

    private final ZeebeClient zeebeClient;

    public SelfManagedProcessStarter(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    @Override
    public String startFnolProcess(MotorFnolCase fnolCase) {
        log.info("Starting FNOL process (self-managed) for case: {}", fnolCase.getFnolId());

        try {
            Map<String, Object> variables = buildProcessVariables(fnolCase);

            ProcessInstanceEvent event = zeebeClient.newCreateInstanceCommand()
                    .bpmnProcessId(PROCESS_ID)
                    .latestVersion()
                    .variables(variables)
                    .send()
                    .join();

            String processInstanceKey = String.valueOf(event.getProcessInstanceKey());
            log.info("Process started successfully (self-managed). FNOL: {}, ProcessInstanceKey: {}",
                    fnolCase.getFnolId(), processInstanceKey);

            return processInstanceKey;

        } catch (Exception e) {
            log.error("Failed to start FNOL process (self-managed) for case: {}",
                    fnolCase.getFnolId(), e);
            throw new ProcessStartException(fnolCase.getFnolId(),
                    "Failed to start Zeebe process: " + e.getMessage(), e);
        }
    }

    /**
     * Build process variables from FNOL case.
     */
    private Map<String, Object> buildProcessVariables(MotorFnolCase fnolCase) {
        Map<String, Object> variables = new HashMap<>();

        variables.put("fnolId", fnolCase.getFnolId());
        variables.put("correlationId", fnolCase.getCorrelationId());
        variables.put("country", fnolCase.getCountry() != null ? fnolCase.getCountry().name() : null);
        variables.put("mobileNumber", fnolCase.getMobileNumber());
        variables.put("injuries", fnolCase.hasInjuries());
        variables.put("drivable", fnolCase.isDrivable());
        variables.put("coverageType", fnolCase.getCoverageType() != null ? fnolCase.getCoverageType().name() : null);
        variables.put("isFleet", fnolCase.isFleetFlag());
        variables.put("severityLevel", fnolCase.getSeverityLevel());
        variables.put("route", fnolCase.getRoute());
        variables.put("insuredName", fnolCase.getInsuredName());
        variables.put("nationalId", fnolCase.getNationalId());
        variables.put("preferredLanguage", fnolCase.getLanguageCode() != null ? fnolCase.getLanguageCode().name() : null);
        variables.put("plateNumber", fnolCase.getPlateNumber());
        variables.put("plateCountry", fnolCase.getPlateCountry() != null ? fnolCase.getPlateCountry().name() : null);
        variables.put("vehicleType", fnolCase.getVehicleType() != null ? fnolCase.getVehicleType().name() : null);
        variables.put("policyNumber", fnolCase.getPolicyNumber());
        variables.put("lossDateTime", fnolCase.getLossDateTime() != null ?
                fnolCase.getLossDateTime().toString() : null);
        variables.put("lossLocation", fnolCase.getLossLocationTextOriginal());
        variables.put("description", fnolCase.getAccidentDescriptionOriginal());
        variables.put("policeReportNumber", fnolCase.getPoliceReportNumber());
        variables.put("attachmentCount", fnolCase.getAttachments().size());

        return variables;
    }
}
