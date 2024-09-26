package mue.dto;

import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mue.entity.EmotionTag;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmotionTagDto {
  private String emotionTag;
  private String userId;

  public static EmotionTagDto fromEntity(EmotionTag emotionTag) {
    return EmotionTagDto.builder()
        .emotionTag(emotionTag.getEmotionTag())
        .userId(emotionTag.getUserId())
        .build();
  }

  public static EmotionTag toEntity(EmotionTagDto dto) {
    return new EmotionTag(
        null,
        dto.getEmotionTag(),
        dto.getUserId(),
        List.of());
  }
}