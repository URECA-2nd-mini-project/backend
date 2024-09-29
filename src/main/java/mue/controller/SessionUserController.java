package mue.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import mue.dto.SessionUser;

@RestController
@RequestMapping("/user")
public class SessionUserController {
    @Autowired
    private HttpSession httpSession;

    /**
     * 현재 로그인된 유저 정보를 반환하는 메서드
     * - 세션 정보를 통해 현재 유저를 확인
     * - 세션 정보가 없는 경우 로그인되지 않은 상태이므로 401 Unauthorized 반환
     * - 세션에서 유저 정보를 가져와 DB에서 유저를 조회하고, 유저 정보 반환
     */
    @GetMapping
    public ResponseEntity<SessionUser> getUser() {
        // 세션에서 현재 유저의 정보를 가져옴
        Object sessionUser = httpSession.getAttribute("user"); // "user"는 세션에 저장된 유저 정보를 나타냄
        System.out.println(sessionUser);

        if (sessionUser == null) {
            // 세션에 유저 정보가 없는 경우 로그인되지 않은 상태임을 상태코드로 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        // 세션에서 유저 정보를 가져와 반환
        SessionUser loggedUser = (SessionUser) sessionUser;

        return ResponseEntity.ok(loggedUser);
    }

}
