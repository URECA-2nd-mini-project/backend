package mue.repository;

import mue.entity.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

// Playlist 엔티티에 대한 CRUD 작업을 처리
public interface PlaylistRepository extends JpaRepository<Playlist, String> {

    // 사용자 ID로 모든 플레이리스트 찾기
    List<Playlist> findByUser_UserId(String userId);

    // 플레이리스트 ID로 플레이리스트 세부정보 찾기
    Optional<Playlist> findByPlaylistId(String playlistId);
}
