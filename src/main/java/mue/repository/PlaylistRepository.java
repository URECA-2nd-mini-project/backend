package mue.repository;

import mue.entity.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Playlist 엔티티에 대한 CRUD 작업을 처리
@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, String> {

    // 사용자 ID로 모든 플레이리스트 찾기
    List<Playlist> findByUserId(Long userId);

    // 플레이리스트 ID로 플레이리스트 세부정보 찾기
    Optional<Playlist> findByPlaylistId(String playlistId);

    // 특정 감정 태그를 가진 플레이리스트를 찾는 메소드
    List<Playlist> findByUserIdAndEmotionTag(Long userId, String emotionTag);
}
