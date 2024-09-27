package mue.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mue.dto.ApiResponseDto;
import mue.dto.EmotionLogDto;
import mue.entity.EmotionLog;
import mue.entity.EmotionTag;
import mue.entity.Music;
import mue.service.EmotionLogService;
import mue.service.EmotionTagService;
import mue.service.MusicService;

// 
@RestController
@RequestMapping("/api/emotion-logs")
public class EmotionLogController {

    @Autowired
    private EmotionLogService emotionLogService; // EmotionLogService 주입

    @Autowired
    private MusicService musicService; // MusicService 주입
    
    @Autowired
    private EmotionTagService emotionTagService; // EmotionTagService 주입
    
    @Autowired
    private UserService userService; // UserService 주입
    
    // 감정 로그 추가
    @PostMapping
    public ResponseEntity<ApiResponseDto> createEmotionLog(
            @RequestParam String userId,
            @RequestParam String musicId,
            @RequestParam String emotionTagId,
            @RequestParam String contents) {

        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Music music = musicService.getMusicById(musicId);
        EmotionTag emotionTag = emotionTagService.findById(emotionTagId)
            .orElseThrow(() -> new RuntimeException("EmotionTag not found"));

    EmotionLogDto emotionLogDto = new EmotionLogDto();
    emotionLogDto.setMusic(music);
    emotionLogDto.setEmotionTag(emotionTag);
    emotionLogDto.setContents(contents);
    emotionLogDto.setCreatedAt(new Date());

    EmotionLog savedEmotionLog = emotionLogService.createEmotionLog(userId, musicId, emotionTagId, contents);

    return ResponseEntity.status(HttpStatus.CREATED)
    .body(new ApiResponseDto(true, "Emotion log created successfully", savedEmotionLog));
    }

    @GetMapping("/user/{userId}")
public ResponseEntity<ApiResponseDto> getEmotionLogsByUser(@PathVariable String userId) {
    User user = userService.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    List<EmotionLog> emotionLogs = emotionLogService.getEmotionLogsByUser(user);
    
    // emotionLog -> DTO 변환 (매핑과정)
    List<EmotionLogDto> emotionLogDtos = emotionLogs.stream()
            .map(log -> new EmotionLogDto(
                    log.getEmotionLogId(),
                    log.getMusic(),
                    log.getEmotionTag(),
                    log.getUser(),
                    log.getContents(),
                    log.getCreatedAt()))
            .collect(Collectors.toList());

    return ResponseEntity.ok(new ApiResponseDto(true, "Emotion logs retrieved successfully", emotionLogDtos));
}
}
