package mue.service;

import lombok.RequiredArgsConstructor;
import mue.entity.User;
import mue.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * ID로 유저 정보를 조회하는 메소드
     * @param id 서버에서 발급된 유저 ID
     * @return Optional<User> 조회된 유저 정보가 존재하면 User 객체를 반환하고, 없으면 빈 Optional 반환
     */
    public Optional<User> findById(String id) {
        // UserRepository를 사용해 ID로 유저를 조회
        return userRepository.findById(id);
    }
    
    /**
     * 유저 정보를 조회하고, 없을 경우 예외를 던질 수 있는 메소드
     * @param id 서버에서 발급된 유저 ID
     * @return User 유저 정보가 없을 경우 IllegalArgumentException 예외 발생
     */
    public User findUserByIdOrThrow(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다. ID: " + id));
    }

    // 기타 유저 관련 서비스 메소드들 추가 가능
}
