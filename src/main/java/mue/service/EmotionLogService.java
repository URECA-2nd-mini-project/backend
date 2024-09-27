package mue.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mue.entity.EmotionLog;
import mue.entity.EmotionTag;
import mue.entity.Music;
import mue.repository.EmotionLogRepository;

//감정 로그 추가, 조회 기능
  
@Service
public class EmotionLogService {

    private final EmotionLogRepository emotionLogRepository;
    private final MusicService musicService; 
    private final EmotionTagService emotionTagService; 

    @Autowired // 생성자 주입
    public EmotionLogService(EmotionLogRepository emotionLogRepository,
                             MusicService musicService,
                             EmotionTagService emotionTagService) {
        this.emotionLogRepository = emotionLogRepository;
        this.musicService = musicService;
        this.emotionTagService = emotionTagService;
    }
    public EmotionLog createEmotionLog(String userId, String musicId, String emotionTagId, String contents) {
        // Music music = musicService.getMusicById(musicId); // 음악 정보 가져오기
          // List<EmotionTag>에서 단일 EmotionTag를 가져오기
    List<EmotionTag> emotionTags = emotionTagService.getEmotionTagsByUserId(emotionTagId);

    // 수정: EmotionTag가 없을 경우 예외를 던짐
    if (emotionTags.isEmpty()) {
        throw new RuntimeException("EmotionTag를 찾을 수 없습니다."); // 다른 예외 클래스를 사용할 수도 있음
    }

    EmotionTag emotionTag = emotionTags.get(0); // 첫 번째 요소를 사용

        //객체 생성 및 속성 설정
        EmotionLog emotionLog = new EmotionLog();
        emotionLog.setEmotionLogId(generateEmotionLogId()); // 고유 ID 설정 (감정로그 식별)
        emotionLog.setUserId(userId); // 사용자 ID 설정
        // emotionLog.setMusic(music);
        emotionLog.setEmotionTag(emotionTag);
        emotionLog.setContents(contents);
        emotionLog.setCreatedAt(new Date());

        return emotionLogRepository.save(emotionLog); // 로그 저장
    }
    

    // 사용자 감정 로그 조회
    public List<EmotionLog> getEmotionLogsByUser(String userId) {
        return emotionLogRepository.findByUserId(userId); // 사용자 ID로 조회
    }


    // ID 생성 로직 
    private String generateEmotionLogId() {
        return "eLog-" + System.currentTimeMillis(); // 밀리초로 고유한 ID 생성 로직
    }
}
