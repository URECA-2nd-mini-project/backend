package mue.repository;

import mue.entity.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// Music 엔티티에 대한 CRUD 작업을 처리
public interface MusicRepository extends JpaRepository<Music, String> {

  // 1. 특정 플레이리스트에 포함된 음악 조회
  List<Music> findByPlaylist(String playlistId);

  // 2. 특정 감정이 기록된 음악 조회
  @Query("SELECT m FROM Music m JOIN EmotionLog el ON m.musicId = el.music.musicId " +
      "JOIN EmotionTag et ON el.emotionTag.emotionTagId = et.emotionTagId " +
      "WHERE et.emotionTag = :emotionTag")
  List<Music> findByEmotionTag(@Param("emotionTag") String emotionTag);
}