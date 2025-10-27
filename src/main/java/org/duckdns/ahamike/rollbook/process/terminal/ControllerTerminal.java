package org.duckdns.ahamike.rollbook.process.terminal;

import org.duckdns.ahamike.rollbook.process.GlobalResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/terminal")
@Slf4j
public class ControllerTerminal {
    private final ServiceTerminal serviceTerminal;

    @PostMapping("/{terminalName}")
    public ResponseEntity<?> registerTerminal(@PathVariable(name = "terminalName") String terminalName) {
        GlobalResponse<?> response = serviceTerminal.registerTerminal(terminalName);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }
}
