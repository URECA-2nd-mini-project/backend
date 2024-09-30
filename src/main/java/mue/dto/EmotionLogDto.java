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
    private String emotionLogId; // 감정 로그 ID
    private Music music; // 음악 정보
    private EmotionTag emotionTag; // 감정 태그 정보
    private String contents; // 로그 내용
    private Date createdAt; // 생성일시

    // DTO -> Entity 변환 메서드 (toEntity)
    public static EmotionLog toEntity(EmotionLogDto dto) {
        return EmotionLog.builder()
                .emotionLogId(UUID.randomUUID().toString())
                .music(dto.getMusic())
                .emotionTag(dto.getEmotionTag())
                .contents(dto.getContents())
                .createdAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : new Date()) // 생성일시 설정
                .build();
    };

    // Entity -> DTO 변환 메서드 (fromEntity)
    public static EmotionLogDto fromEntity(EmotionLog entity) {
        return EmotionLogDto.builder()
                .emotionLogId(entity.getEmotionLogId())
                .music(entity.getMusic())
                .emotionTag(entity.getEmotionTag())
                .contents(entity.getContents())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
