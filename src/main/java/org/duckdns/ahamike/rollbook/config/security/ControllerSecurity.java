package org.duckdns.ahamike.rollbook.config.security;

import org.duckdns.ahamike.rollbook.config.security.endpoint.EndpointOrder;
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

    @EndpointOrder(value0 = 400, value1 = 1100)
    @PostMapping("/endpoint/{endpointId}/role/{roleId}")
    public ResponseEntity<?> addRoleToEndpoint(@PathVariable(name = "endpointId") Long endpointId, @PathVariable(name = "roleId") Long roleId) {
        GlobalResponse<?> response = serviceSecurity.addRoleToEndpoint(endpointId, roleId);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }

    @EndpointOrder(value0 = 400, value1 = 1500)
    @DeleteMapping("/endpoint/{endpointId}/role/{roleId}")
    public ResponseEntity<?> removeRoleFromEndpoint(@PathVariable(name = "endpointId") Long endpointId, @PathVariable(name = "roleId") Long roleId) {
        GlobalResponse<?> response = serviceSecurity.removeRoleFromEndpoint(endpointId, roleId);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }

    @EndpointOrder(value0 = 400, value1 = 2100)
    @PostMapping("/user/{userId}/role/{roleId}")
    public ResponseEntity<?> addRoleToUser(@PathVariable(name = "userId") Long userId, @PathVariable(name = "roleId") Long roleId) {
        GlobalResponse<?> response = serviceSecurity.addRoleToUser(userId, roleId);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }

    @EndpointOrder(value0 = 400, value1 = 2500)
    @DeleteMapping("/user/{userId}/role/{roleId}")
    public ResponseEntity<?> removeRoleFromUser(@PathVariable(name = "userId") Long userId, @PathVariable(name = "roleId") Long roleId) {
        GlobalResponse<?> response = serviceSecurity.removeRoleFromUser(userId, roleId);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }

    @EndpointOrder(value0 = 400, value1 = 3100)
    @PostMapping("/user/{userId}/group/{groupId}")
    public ResponseEntity<?> addGroupToUser(@PathVariable(name = "userId") Long userId, @PathVariable(name = "groupId") Long groupId) {
        GlobalResponse<?> response = serviceSecurity.addGroupToUser(userId, groupId);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }

    @EndpointOrder(value0 = 400, value1 = 3500)
    @DeleteMapping("/user/{userId}/group/{groupId}")
    public ResponseEntity<?> removeGroupFromUser(@PathVariable(name = "userId") Long userId, @PathVariable(name = "groupId") Long groupId) {
        GlobalResponse<?> response = serviceSecurity.removeGroupFromUser(userId, groupId);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }

    @EndpointOrder(value0 = 400, value1 = 4100)
    @PostMapping("/group/{groupId}/role/{roleId}")
    public ResponseEntity<?> addRoleToGroup(@PathVariable(name = "groupId") Long groupId, @PathVariable(name = "roleId") Long roleId) {
        GlobalResponse<?> response = serviceSecurity.addRoleToGroup(groupId, roleId);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }

    @EndpointOrder(value0 = 400, value1 = 4500)
    @DeleteMapping("/group/{groupId}/role/{roleId}")
    public ResponseEntity<?> removeRoleFromGroup(@PathVariable(name = "groupId") Long groupId, @PathVariable(name = "roleId") Long roleId) {
        GlobalResponse<?> response = serviceSecurity.removeRoleFromGroup(groupId, roleId);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }
}
