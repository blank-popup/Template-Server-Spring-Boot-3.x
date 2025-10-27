package org.duckdns.ahamike.rollbook.config.security.user;

import org.duckdns.ahamike.rollbook.process.GlobalResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
@Slf4j
public class ControllerUser {
    private final ServiceUser serviceUser;

    @PostMapping("/user/signup")
    public ResponseEntity<?> signUp(@RequestBody RequestSignUp request) {
        GlobalResponse<?> response = serviceUser.signUp(request);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }

    @PostMapping("/user/signin")
    public ResponseEntity<?> signIn(@RequestBody RequestSignIn request) {
        GlobalResponse<?> response = serviceUser.signIn(request);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }

    @GetMapping("/admin/user")
    public ResponseEntity<?> getUsers(@RequestParam(name = "username", required = false) String username, @RequestParam(name = "name", required = false) String name, @RequestParam(name = "tag", required = false) String tag) {
        GlobalResponse<?> response = serviceUser.getUsers(username, name, tag);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }

    @PostMapping("/test/{testPathVariable}")
    public ResponseEntity<?> testReceiveVariable(@RequestBody RequestSignUp testRequest, @PathVariable(name = "testPathVariable") String testPathVariable, @RequestParam(name = "testRequestParam", required = false) String testRequestParam) {
        return ResponseEntity
                .ok()
                .build();
    }
}
