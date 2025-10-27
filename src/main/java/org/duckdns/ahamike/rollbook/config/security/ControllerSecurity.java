package org.duckdns.ahamike.rollbook.config.security;

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
@RequestMapping("/v1/admin/security")
@Slf4j
public class ControllerSecurity {
    private final ServiceSecurity serviceSecurity;

    @PostMapping("/endpoint/{endpointId}/role/{roleId}")
    public ResponseEntity<?> addRoleToEndpoint(@PathVariable(name = "endpointId") Long endpointId, @PathVariable(name = "roleId") Long roleId) {
        GlobalResponse<?> response = serviceSecurity.addRoleToEndpoint(endpointId, roleId);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }

    @DeleteMapping("/endpoint/{endpointId}/role/{roleId}")
    public ResponseEntity<?> removeRoleFromEndpoint(@PathVariable(name = "endpointId") Long endpointId, @PathVariable(name = "roleId") Long roleId) {
        GlobalResponse<?> response = serviceSecurity.removeRoleFromEndpoint(endpointId, roleId);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }

    @PostMapping("/user/{userId}/role/{roleId}")
    public ResponseEntity<?> addRoleToUser(@PathVariable(name = "userId") Long userId, @PathVariable(name = "roleId") Long roleId) {
        GlobalResponse<?> response = serviceSecurity.addRoleToUser(userId, roleId);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }

    @DeleteMapping("/user/{userId}/role/{roleId}")
    public ResponseEntity<?> removeRoleFromUser(@PathVariable(name = "userId") Long userId, @PathVariable(name = "roleId") Long roleId) {
        GlobalResponse<?> response = serviceSecurity.removeRoleFromUser(userId, roleId);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }
}
