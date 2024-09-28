package mue.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 연결 테스트용 컨트롤러

@RestController
public class HomeController {
    @GetMapping("/home")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello Docker-Spring World!");
    }
}
