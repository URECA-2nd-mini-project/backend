package mue.repository;

import mue.entity.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

// EmotionLog 엔티티에 대한 CRUD 작업을 처리
public interface EmotionLogRepository extends JpaRepository<EmotionLog, String> {

    // 사용자 ID에 따른 감정 로그 조회
    List<EmotionLog> findByUserId(String userId);
}
