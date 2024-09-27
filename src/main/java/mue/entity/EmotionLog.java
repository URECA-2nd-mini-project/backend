package mue.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "emotion_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmotionLog implements Serializable {

    @Id
    @Column(name = "emotion_log_id", nullable = false, unique = true)
    private String emotionLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "music_id", nullable = false) // FK로 Music 테이블의 musicId 참조
    private Music music;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emotion_tag_id", nullable = false) // FK로 EmotionTag 테이블의 emotionTagId 참조
    private EmotionTag emotionTag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // FK로 User 테이블의 userId 참조
    private String userId;

    @Column(name = "contents", columnDefinition = "TEXT", nullable = false)
    private String contents;

    @Temporal(TemporalType.TIMESTAMP) // 날짜 및 시간 데이터를 나타내기 위한 어노테이션
    @Column(name = "created_at", nullable = false)
    private Date createdAt;
}