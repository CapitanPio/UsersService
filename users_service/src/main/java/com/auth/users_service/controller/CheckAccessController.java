package com.auth.users_service.controller;

import com.auth.users_service.dto.CheckAccessRequest;
import com.auth.users_service.service.CheckAccessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@lombok.RequiredArgsConstructor
public class CheckAccessController {

    private final CheckAccessService checkAccessService;

    @PostMapping("/check-access")
    public ResponseEntity<Object> checkAccess(@RequestBody CheckAccessRequest request) {
        try {
            boolean hasAccess = checkAccessService.checkAccess(request);
            return ResponseEntity.ok(Map.of("hasAccess", hasAccess));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
}
