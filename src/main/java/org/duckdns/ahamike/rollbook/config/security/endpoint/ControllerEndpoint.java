package org.duckdns.ahamike.rollbook.config.security.endpoint;

import java.util.Map;

import org.duckdns.ahamike.rollbook.config.constant.ReturnCode;
import org.duckdns.ahamike.rollbook.process.GlobalResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/admin/endpoint")
@Slf4j
public class ControllerEndpoint {

    public final CacheEndpoint cacheEndpoint;

    @EndpointOrder(value0 = 500, value1 = 100)
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshCache() {
        cacheEndpoint.scanEndpoints();
        Map<String, InfoEndpoint> response = cacheEndpoint.refreshCache();

        ReturnCode code = ReturnCode.OK;
        return buildResponseEntity(code, response);
    }

    private ResponseEntity<?> buildResponseEntity(ReturnCode code, Object response) {
        GlobalResponse<?> result = new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );

        return ResponseEntity
                .status(code.getHttpStatus())
                .body(result);
    }
}
