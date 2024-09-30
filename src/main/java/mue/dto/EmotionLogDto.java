package mue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mue.entity.EmotionLog;
import mue.entity.EmotionTag;
import mue.entity.Music;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmotionLogDto {
    // POST 요청 시 필요한 필드
    private PlayHistoryDto music;
    private String emotionTagId;
    private String contents;

    // GET 요청 시 필요한 필드
    private String emotionLogId;
    private String emotionTag;
    private Date createdAt;

    // POST 요청 시 사용 (Music과 EmotionTag를 받아서 EmotionLog 엔티티로 변환)
    public static EmotionLog toEntity(EmotionLogDto dto, Music music, EmotionTag emotionTag) {
        return EmotionLog.builder()
                .emotionLogId(UUID.randomUUID().toString())
                .music(music)
                .emotionTag(emotionTag)
                .contents(dto.getContents())
                .createdAt(new Date())
                .build();
    }

    // GET 요청 시 사용 (EmotionLog 엔티티에서 DTO로 변환)
    public static EmotionLogDto fromEntity(EmotionLog emotionLog) {
        return EmotionLogDto.builder()
                .emotionLogId(emotionLog.getEmotionLogId())
                .emotionTag(emotionLog.getEmotionTag().getEmotionTag())
                .contents(emotionLog.getContents())
                .createdAt(emotionLog.getCreatedAt())
                .build();
    }
}
