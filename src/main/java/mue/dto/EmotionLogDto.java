package mue.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mue.entity.EmotionTag;
import mue.entity.Music;
import mue.entity.User;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmotionLogDto {
    private String emotionLogId; // 감정 로그 ID
    private Music music; // 음악 정보
    private EmotionTag emotionTag; // 감정 태그 정보
    private User user; // 사용자 정보 (필요 시)
    private String contents; // 로그 내용
    private Date createdAt; // 생성일시
}
