package mue.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mue.entity.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

// 프론트와 주고받을 PlayHistory 데이터 구조를 정의
public class PlayHistoryDto {
  private String musicId;
  private String title;
  private String artist;
  private Playlist playlist;

  // PlayHistory 테이블에서 프론트와 주고받을 데이터만 따로 가져와 DTO 생성
  public static PlayHistoryDto fromEntity(PlayHistory playHistory) {
    return PlayHistoryDto.builder()
        .musicId(playHistory.getMusicId())
        .title(playHistory.getTitle())
        .artist(playHistory.getArtist())
        .build();
  }

  // EmotionTagDto를 EmotionTag 엔티티로 변환
  public static Music toEntity(PlayHistoryDto dto) {
    return Music.builder()
        .musicId(dto.getMusicId())
        .title(dto.getTitle())
        .artist(dto.getArtist())
        .playlist(dto.getPlaylist())
        .build();
  }
}
