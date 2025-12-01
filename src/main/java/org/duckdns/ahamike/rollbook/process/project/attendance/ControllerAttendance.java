package org.duckdns.ahamike.rollbook.process.project.attendance;

import java.time.LocalDate;

import org.duckdns.ahamike.rollbook.config.security.endpoint.EndpointOrder;
import org.duckdns.ahamike.rollbook.process.GlobalResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/attendance")
@Slf4j
public class ControllerAttendance {

    private final ServiceAttendance serviceAttendance;

    @EndpointOrder(value0 = 100200, value1 = 100)
    @PostMapping
    public ResponseEntity<?> saveAttender(@RequestBody RequestAttend request) {
        GlobalResponse<?> response = serviceAttendance.saveAttender(request);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }

    @EndpointOrder(value0 = 100200, value1 = 600)
    @GetMapping
    public ResponseEntity<?> getAttenders(@RequestParam(name = "userId", required = false) Long userId, @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(name = "from", required = false) LocalDate from, @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(name = "to", required = false) LocalDate to) {
        GlobalResponse<?> response = serviceAttendance.getAttenders(userId, from, to);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }
}
