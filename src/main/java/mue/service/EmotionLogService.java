package mue.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mue.entity.EmotionLog;
import mue.entity.EmotionTag;
import mue.entity.Music;
import mue.repository.EmotionLogRepository;
import mue.repository.EmotionTagRepository;
import mue.repository.MusicRepository;

//감정 로그 추가, 조회 기능

@Service
public class EmotionLogService {

    private final EmotionLogRepository emotionLogRepository;
    private final MusicRepository musicRepository;
    private final EmotionTagRepository emotionTagRepository;
    private final EmotionTagService emotionTagService;

    @Autowired // 생성자 주입
    public EmotionLogService(EmotionLogRepository emotionLogRepository,
            MusicRepository musicRepository,
            EmotionTagRepository emotionTagRepository,
            EmotionTagService emotionTagService) {
        this.emotionLogRepository = emotionLogRepository;
        this.musicRepository = musicRepository;
        this.emotionTagRepository = emotionTagRepository;
        this.emotionTagService = emotionTagService;
    }

    public EmotionLog createEmotionLog(String userId, String musicId, String emotionTagId, String contents) {
        // 음악 정보 가져오기
        Music music = musicRepository.findByMusicId(musicId);
        // EmotionTag를 EmotionTagId를 기준으로 검색
        EmotionTag emotionTag = emotionTagRepository.findByEmotionTagId(emotionTagId);

        // 수정: EmotionTag가 없을 경우 예외를 던짐
        if (emotionTag == null) {
            throw new RuntimeException("EmotionTag를 찾을 수 없습니다."); // 다른 예외 클래스를 사용할 수도 있음
        }

        // 객체 생성 및 속성 설정
        EmotionLog emotionLog = new EmotionLog();
        emotionLog.setEmotionLogId(generateEmotionLogId()); // 고유 ID 설정 (감정로그 식별)
        emotionLog.setMusic(music);
        emotionLog.setEmotionTag(emotionTag);
        emotionLog.setContents(contents);
        emotionLog.setCreatedAt(new Date());

        return emotionLogRepository.save(emotionLog); // 로그 저장
    }

    // 사용자 감정 로그 조회
    public List<EmotionLog> getEmotionLogsByEmotionTagId(String emotionTagId) {
        return emotionLogRepository.findByEmotionTag_EmotionTagId(emotionTagId); // 사용자 ID로 조회
    }

    // 2. musicId에 해당하는 모든 감정 로그 조회
    public List<EmotionLog> findByMusicId(String musicId) {
        return emotionLogRepository.findByMusic_MusicId(musicId);
    }

    // ID 생성 로직
    private String generateEmotionLogId() {
        return "eLog-" + System.currentTimeMillis(); // 밀리초로 고유한 ID 생성 로직
    }
}
