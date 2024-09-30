package mue.dto;

import mue.entity.*;
import mue.dto.*;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmotionTagDto {
  private String emotionTag;
  private String emotionTagId;
  private List<PlayHistoryDto> music;

  // EmotionTag를 DTO로 변환 (music 없이)
  public static EmotionTagDto fromEntity(EmotionTag emotionTag) {
    return EmotionTagDto.builder()
        .emotionTag(emotionTag.getEmotionTag())
        .emotionTagId(emotionTag.getEmotionTagId())
        .music(Collections.emptyList()) // music 리스트는 빈 값으로 설정
        .build();
  }

  // EmotionTag를 DTO로 변환 (music 포함)
  public static EmotionTagDto fromEntity(EmotionTag emotionTag, List<PlayHistoryDto> music) {
    return EmotionTagDto.builder()
        .emotionTag(emotionTag.getEmotionTag())
        .emotionTagId(emotionTag.getEmotionTagId())
        .music(music)
        .build();
  }

  // EmotionTagDto를 EmotionTag 엔티티로 변환
  public static EmotionTag toEntity(EmotionTagDto dto, User user) {
    return EmotionTag.builder()
        .emotionTagId(UUID.randomUUID().toString()) // UUID로 emotionTagId 생성
        .emotionTag(dto.getEmotionTag()) // dto의 emotionTag 설정
        .user(user) // 매개변수로 받은 user 객체 설정
        .emotionLogs(List.of()) // 빈 리스트로 초기화 (필요에 따라 설정)
        .build();
  }
}
