package org.duckdns.ahamike.rollbook.config.security.role;

import org.duckdns.ahamike.rollbook.config.response.GlobalResponse;
import org.duckdns.ahamike.rollbook.config.security.endpoint.EndpointOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/admin/role")
@Slf4j
public class RoleController {

    private final RoleService roleService;

    @EndpointOrder(value0 = 200, value1 = 100)
    @PostMapping
    public ResponseEntity<?> registerRole(@RequestBody RoleDomain request) {
        GlobalResponse<?> response = roleService.registerRole(request);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }

    @EndpointOrder(value0 = 200, value1 = 500)
    @DeleteMapping("/{roleId}")
    public ResponseEntity<?> removeRole(@PathVariable(name = "roleId") Long roleId) {
        GlobalResponse<?> response = roleService.removeRole(roleId);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }
}
