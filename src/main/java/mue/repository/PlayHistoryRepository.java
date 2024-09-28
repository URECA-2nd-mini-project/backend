package mue.repository;

import mue.entity.PlayHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayHistoryRepository extends JpaRepository<PlayHistory, String> {

  // 특정 유저의 모든 재생 기록 조회
  List<PlayHistory> findByUser_UserId(String userId);

  // 특정 유저의 최근 재생 기록 조회
  List<PlayHistory> findTop5ByUser_UserIdOrderByPlayedAtDesc(String userId);
}
