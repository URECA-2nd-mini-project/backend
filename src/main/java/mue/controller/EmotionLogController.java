package mue.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mue.dto.ApiResponseDto;
import mue.dto.EmotionLogDto;
import mue.dto.SessionUser;
import mue.entity.EmotionLog;
import mue.entity.EmotionTag;
import mue.entity.Music;
import mue.service.CustomOAuth2UserService;
import mue.service.EmotionLogService;
import mue.service.EmotionTagService;
import mue.service.MusicService;


@RestController
@RequestMapping("/api/emotion-logs")
public class EmotionLogController {

    private final EmotionLogService emotionLogService;
    private final MusicService musicService;
    private final EmotionTagService emotionTagService;
    private final HttpSession httpSession;

    @Autowired //생성자 주입
    public EmotionLogController(EmotionLogService emotionLogService,
                                MusicService musicService,
                                EmotionTagService emotionTagService,
                                HttpSession httpSession) {
        this.emotionLogService = emotionLogService;
        this.musicService = musicService;
        this.emotionTagService = emotionTagService;
        this.httpSession = httpSession;
    }
    
    private String getUserIdFromSession() {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        if (sessionUser == null) {
            throw new RuntimeException("사용자가 인증되지 않았습니다.");
        }
        return sessionUser.getGmail();
    }

    // 감정 로그 추가
    @PostMapping
    public ResponseEntity<ApiResponseDto> createEmotionLog(
            @RequestParam String userId,
            @RequestParam String musicId,
            @RequestParam String emotionTagId,
            @RequestParam String contents) {

          
           userId = getUserIdFromSession();  // 세션에서 사용자 정보 가져오기
        Music music = musicService.getMusicById(musicId); // 음악 정보 가져오기
       // EmotionTag를 가져오고, 리스트가 비어있으면 예외 처리
    List<EmotionTag> emotionTags = emotionTagService.getEmotionTagsByUserId(emotionTagId);
    if (emotionTags.isEmpty()) {
        throw new RuntimeException("EmotionTag를 찾을 수 없습니다."); // 예외 처리
    }
    
    EmotionTag emotionTag = emotionTags.get(0); // 첫 번째 EmotionTag 사용

    // EmotionLog 객체 생성 및 설정
    EmotionLog savedEmotionLog = emotionLogService.createEmotionLog(userId, musicId, emotionTag.getEmotionTagId(), contents);

    // DTO 변환
    EmotionLogDto emotionLogDto = new EmotionLogDto();
    emotionLogDto.setMusic(music);
    emotionLogDto.setEmotionTag(emotionTag);
    emotionLogDto.setContents(contents);
    emotionLogDto.setCreatedAt(new Date());

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ApiResponseDto(true, "감정 로그가 성공적으로 생성되었습니다.", savedEmotionLog));
}

    @GetMapping("/user")
    public ResponseEntity<ApiResponseDto> getEmotionLogsByUser() {
        String currentUserId = getUserIdFromSession(); // 세션에서 사용자 ID 가져오기
        List<EmotionLog> emotionLogs = emotionLogService.getEmotionLogsByUser(currentUserId);
    
    // emotionLog -> DTO 변환 (매핑과정)
    List<EmotionLogDto> emotionLogDtos = emotionLogs.stream()
            .map(log -> new EmotionLogDto(
                    log.getEmotionLogId(),
                    log.getMusic(),
                    log.getEmotionTag(),
                    log.getUser(), // user는 이미 session에서 가져온 currentUserId로 대체 가능
                    log.getContents(),
                    log.getCreatedAt()))
            .collect(Collectors.toList());

    return ResponseEntity.ok(new ApiResponseDto(true, "감정 로그를 성공적으로 조회하였습니다.", emotionLogDtos));
}
}
