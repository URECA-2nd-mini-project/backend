package mue.repository;

import mue.entity.*;

import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// Music 엔티티에 대한 CRUD 작업을 처리
public interface MusicRepository extends JpaRepository<Music, String> {

  // 특정 유저의 모든 플레이리스트에 속한 음악을 중복을 제거하고 조회
  @Query("SELECT DISTINCT m FROM Music m " +
      "WHERE m.playlist.playlistId IN " +
      "(SELECT p.playlistId FROM Playlist p WHERE p.user.userId = :userId)")
  List<Music> findDistinctByUserPlaylists(@Param("userId") String userId);

  // 특정 플레이리스트에서 최근 재생된 음악 5개 조회
  @Query("SELECT m FROM Music m WHERE m IN :musics ORDER BY m.playedAt DESC")
  List<Music> findTop5ByMusicListOrderByPlayedAtDesc(@Param("musics") List<Music> musics, Pageable pageable);
}