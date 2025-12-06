package org.duckdns.ahamike.rollbook.process.agent.menu;

import org.duckdns.ahamike.rollbook.config.security.endpoint.EndpointOrder;
import org.duckdns.ahamike.rollbook.process.GlobalResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/menu")
@Slf4j
public class MenuController {

    private final MenuService menuService;

    @EndpointOrder(value0 = 100100, value1 = 100)
    @PostMapping
    public ResponseEntity<?> registerMenu(@RequestBody MenuDomain request) {
        GlobalResponse<?> response = menuService.registerMenu(request);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }

    @EndpointOrder(value0 = 100100, value1 = 300)
    @GetMapping
    public ResponseEntity<?> getRecursiveMenu(@RequestParam(name = "userId", required = true) Long userId) {
        GlobalResponse<?> response = menuService.getRecursiveMenu(userId);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }
}
