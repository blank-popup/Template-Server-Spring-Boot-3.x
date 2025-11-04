package org.duckdns.ahamike.rollbook.config.security.group;

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
@RequestMapping("/v1/admin/group")
@Slf4j
public class ControllerGroup {
    private final ServiceGroup serviceGroup;

    @PostMapping("/{groupName}")
    public ResponseEntity<?> registerGroup(@PathVariable(name = "groupName") String groupName) {
        GlobalResponse<?> response = serviceGroup.registerGroup(groupName);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<?> removeGroup(@PathVariable(name = "groupId") Long groupId) {
        GlobalResponse<?> response = serviceGroup.removeGroup(groupId);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }
}
