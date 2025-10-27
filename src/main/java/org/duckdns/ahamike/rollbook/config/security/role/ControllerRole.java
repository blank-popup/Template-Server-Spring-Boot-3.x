package org.duckdns.ahamike.rollbook.config.security.role;

import org.duckdns.ahamike.rollbook.process.GlobalResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/admin/role")
@Slf4j
public class ControllerRole {
    private final ServiceRole serviceRole;

    @PostMapping("/{roleName}")
    public ResponseEntity<?> registerRole(@PathVariable(name = "roleName") String roleName) {
        GlobalResponse<?> response = serviceRole.registerRole(roleName);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<?> removeRole(@PathVariable(name = "roleId") Long roleId) {
        GlobalResponse<?> response = serviceRole.removeRole(roleId);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }
}
