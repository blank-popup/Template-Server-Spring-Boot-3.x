package org.duckdns.ahamike.rollbook.process.project.terminal;

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
@RequestMapping("/v1/terminal")
@Slf4j
public class TerminalController {

    private final TerminalService terminalService;

    @EndpointOrder(value0 = 100100, value1 = 100)
    @PostMapping
    public ResponseEntity<?> registerTerminal(@RequestBody TerminalDomain request) {
        GlobalResponse<?> response = terminalService.registerTerminal(request);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }

    @EndpointOrder(value0 = 100100, value1 = 500)
    @DeleteMapping("/{terminalId}")
    public ResponseEntity<?> removeTerminal(@PathVariable(name = "terminalId") Long terminalId) {
        GlobalResponse<?> response = terminalService.removeTerminal(terminalId);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }
}
