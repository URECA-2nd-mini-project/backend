package mue.repository;

import mue.entity.*;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// EmotionTag 엔티티에 대한 CRUD 작업을 처리
public interface EmotionTagRepository extends JpaRepository<EmotionTag, String> {
  // 특정 유저의 모든 감정 태그 조회
  @Query("SELECT e FROM EmotionTag e WHERE e.user.userId = :userId")
  List<EmotionTag> findAllByUserId(@Param("userId") String userId);

}