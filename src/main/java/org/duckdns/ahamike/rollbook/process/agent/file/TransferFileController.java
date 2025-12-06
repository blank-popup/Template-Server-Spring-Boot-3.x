package org.duckdns.ahamike.rollbook.process.agent.file;

import org.duckdns.ahamike.rollbook.config.response.GlobalResponse;
import org.duckdns.ahamike.rollbook.config.security.endpoint.EndpointOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/file")
@Slf4j
public class TransferFileController {

    private final TransferFileService transferFileService;

    @EndpointOrder(value0 = 10200, value1 = 100)
    @PostMapping
    public ResponseEntity<?> upload(
        @RequestPart(value = "directory0", required = false) String directory0,
        @RequestPart(value = "directory1", required = false) String directory1,
        @RequestPart(value = "directory2", required = false) String directory2,
        @RequestPart(value = "description", required = false) String description,
        @RequestPart(value = "file", required = true) MultipartFile file) {
        GlobalResponse<?> response = transferFileService.upload(directory0, directory1, directory2, description, file);

        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }

    @EndpointOrder(value0 = 10200, value1 = 200)
    @GetMapping("/{transferFileId}")
    public void download(HttpServletResponse response, @PathVariable(value="transferFileId") Long transferFileId) {
        transferFileService.download(response, transferFileId);
    }
}
