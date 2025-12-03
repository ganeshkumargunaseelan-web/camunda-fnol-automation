/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.infrastructure.zeebe;

import io.camunda.community.fnol.gcc.motor.application.port.out.ProcessStarterPort;
import io.camunda.community.fnol.gcc.motor.domain.model.MotorFnolCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Component
@ConditionalOnProperty(name = "camunda.client.mode", havingValue = "demo", matchIfMissing = true)
public class DemoProcessStarter implements ProcessStarterPort {

    private static final Logger log = LoggerFactory.getLogger(DemoProcessStarter.class);
    private static final AtomicLong processCounter = new AtomicLong(1000);

    @Override
    public String startFnolProcess(MotorFnolCase fnolCase) {
        // Generate a mock process instance key
        String processInstanceKey = "DEMO-" + processCounter.incrementAndGet();

        log.info("[DEMO MODE] Simulating process start for FNOL: {}. Mock ProcessInstanceKey: {}",
                fnolCase.getFnolId(), processInstanceKey);

        // Log what would be sent to the real process
        log.debug("[DEMO MODE] Process variables that would be sent:");
        log.debug("  - fnolId: {}", fnolCase.getFnolId());
        log.debug("  - country: {}", fnolCase.getCountry() != null ? fnolCase.getCountry().name() : "null");
        log.debug("  - injuries: {}", fnolCase.hasInjuries());
        log.debug("  - drivable: {}", fnolCase.isDrivable());
        log.debug("  - coverageType: {}", fnolCase.getCoverageType() != null ? fnolCase.getCoverageType().name() : "null");
        log.debug("  - isFleet: {}", fnolCase.isFleetFlag());
        log.debug("  - severityLevel: {}", fnolCase.getSeverityLevel());
        log.debug("  - route: {}", fnolCase.getRoute());

        return processInstanceKey;
    }
}
