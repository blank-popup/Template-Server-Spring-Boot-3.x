package org.duckdns.ahamike.rollbook.process.board.dynamic.writing;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.collections4.Get;
import org.duckdns.ahamike.rollbook.process.board.config.TenantContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class TenantController {

    private final UserRepository userRepository;
    private final TenantRegistrationService tenantRegistrationService;

    // Create new tenant schema
    // ### TEST - New Schema for Tenant
    // POST {{host}}/tenants/companyE HTTP/1.1
    @PostMapping("/tenants/{tenantId}")
    public String createTenant(@PathVariable String tenantId) throws SQLException {
        tenantRegistrationService.registerNewTenant(tenantId);
        return "Tenant " + tenantId + " created successfully!";
    }

    // CRUD using tenant header
    // ### TEST - Create User in Tenant Schema
    // POST {{host}}/users?username=charlie7733 HTTP/1.1
    // X-TenantID: companyA
    @PostMapping("/users")
    public EntityUser2 addUser(@RequestHeader(value = "X-TenantID", required = false) String tenant,
                              @RequestParam String username) {
        return userRepository.save(new EntityUser2(username));
    }

    // ### TEST - Get Users in Tenant Schema
    // GET {{host}}/users HTTP/1.1
    // X-TenantID: companyC
    @GetMapping("/users")
    public List<EntityUser2> getUsers(@RequestHeader("X-TenantID") String tenant) {
        return userRepository.findAll();
    }
}
