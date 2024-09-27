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

    @Autowired
    private EmotionLogRepository emotionLogRepository;
    @Autowired
    private MusicService musicService; // MusicService 주입
    @Autowired
    private EmotionTagService emotionTagService; // EmotionTagService 주입
    @Autowired
    // private UserService userService; // UserService 주입

    public EmotionLog createEmotionLog(String userId, String musicId, String emotionTagId, String contents) {
        // User user = userService.findById(userId); // 사용자 정보 가져오기
        Music music = musicService.getMusicById(musicId); // 음악 정보 가져오기
        EmotionTag emotionTag = emotionTagService.findById(emotionTagId); // 감정 태그 정보 가져오기

        //객체 생성 및 속성 설정
        EmotionLog emotionLog = new EmotionLog();
        emotionLog.setEmotionLogId(generateEmotionLogId()); // 고유 ID 설정 (감정로그 식별)
        emotionLog.setUser(user);
        emotionLog.setMusic(music);
        emotionLog.setEmotionTag(emotionTag);
        emotionLog.setContents(contents);
        emotionLog.setCreatedAt(new Date());

        return emotionLogRepository.save(emotionLog); // 로그 저장
    }
    

    // 사용자 감정 로그 조회
    public List<EmotionLog> getEmotionLogsByUser(User user) {
        return emotionLogRepository.findByUser(user);
    }

    // ID 생성 로직 
    private String generateEmotionLogId() {
        return "eLog-" + System.currentTimeMillis(); // 밀리초로 고유한 ID 생성 로직
    }
}
