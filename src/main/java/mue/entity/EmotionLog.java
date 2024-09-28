package mue.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "emotion_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmotionLog {

    @Id
    @Column(name = "emotion_log_id", nullable = false, unique = true)
    private String emotionLogId;

    // Music 엔티티와 다대일 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "music_id", nullable = false) // Music 테이블의 music_id를 외래키로 설정
    private Music music;

    // EmotionTag 엔티티와 다대일 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emotion_tag_id", nullable = false)
    private EmotionTag emotionTag;

    @Column(name = "contents", columnDefinition = "TEXT")
    private String contents;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;
}