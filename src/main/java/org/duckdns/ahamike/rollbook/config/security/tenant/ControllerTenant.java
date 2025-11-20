package org.duckdns.ahamike.rollbook.config.security.tenant;

import org.duckdns.ahamike.rollbook.config.security.endpoint.EndpointOrder;
import org.duckdns.ahamike.rollbook.process.GlobalResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/admin/tenant")
@Slf4j
public class ControllerTenant {

    private final ServiceTenant serviceTenant;

    @EndpointOrder(value0 = 700, value1 = 100)
    @PostMapping
    public ResponseEntity<?> registerTenant(@RequestBody RequestRegisterTenant request) {
        GlobalResponse<?> response = serviceTenant.registerTenant(request);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }

    @EndpointOrder(value0 = 700, value1 = 200)
    @GetMapping
    public ResponseEntity<?> getTenants() {
        GlobalResponse<?> response = serviceTenant.getTenants();
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }

    @EndpointOrder(value0 = 700, value1 = 300)
    @DeleteMapping("/{tenantId}")
    public ResponseEntity<?> removeTenant(@PathVariable(name = "tenantId") Long tenantId) {
        GlobalResponse<?> response = serviceTenant.removeTenant(tenantId);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }
}
