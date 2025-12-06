package org.duckdns.ahamike.rollbook.config.security.group;

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
@RequestMapping("/v1/admin/group")
@Slf4j
public class GroupController {

    private final GroupService groupService;

    @EndpointOrder(value0 = 300, value1 = 100)
    @PostMapping
    public ResponseEntity<?> registerGroup(@RequestBody GroupDomain request) {
        GlobalResponse<?> response = groupService.registerGroup(request);

        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }

    @EndpointOrder(value0 = 300, value1 = 500)
    @DeleteMapping("/{groupId}")
    public ResponseEntity<?> removeGroup(@PathVariable(name = "groupId") Long groupId) {
        GlobalResponse<?> response = groupService.removeGroup(groupId);

        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }
}
