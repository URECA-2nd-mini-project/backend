package mue.controller;

import java.util.*;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mue.dto.*;
import mue.entity.*;
import mue.repository.EmotionTagRepository;
import mue.repository.MusicRepository;
import mue.service.*;

@RestController
@RequestMapping("/emotionLog")
public class EmotionLogController {

    private final EmotionLogService emotionLogService;
    private final MusicService musicService;
    private final EmotionTagService emotionTagService;
    private final EmotionTagRepository emotionTagRepository;
    private final UserService userService;
    private final MusicRepository musicRepository;
    private final HttpSession httpSession;

    @Autowired // 생성자 주입
    public EmotionLogController(EmotionLogService emotionLogService,
            MusicService musicService,
            EmotionTagService emotionTagService,
            EmotionTagRepository emotionTagRepository,
            UserService userService,
            MusicRepository musicRepository,
            HttpSession httpSession) {
        this.emotionLogService = emotionLogService;
        this.musicService = musicService;
        this.musicRepository = musicRepository;
        this.emotionTagService = emotionTagService;
        this.emotionTagRepository = emotionTagRepository;
        this.userService = userService;
        this.httpSession = httpSession;
    }

    private String getUserIdFromSession() {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        if (sessionUser == null) {
            throw new RuntimeException("사용자가 인증되지 않았습니다.");
        }
        return sessionUser.getUserId();
    }

    @PostMapping
    public ResponseEntity<EmotionLogDto> createEmotionLog(@RequestBody EmotionLogDto emotionLogDto) {
        // 세션에서 사용자 정보를 가져옴
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");

        // 음악 정보와 감정 태그 정보가 올바르게 제공되었는지 확인
        Music music = musicRepository.findByMusicId(emotionLogDto.getMusic().getMusicId());
        EmotionTag emotionTag = emotionTagRepository
                .findByEmotionTagId(emotionLogDto.getEmotionTag().getEmotionTagId());

        // 음악 정보가 존재하지 않으면 새로운 음악 생성 후 테이블에 삽입
        if (music == null) {
            // 새로 생성할 음악 정보는 DTO로부터 가져옴
            Music newMusic = Music.builder()
                    .musicId(emotionLogDto.getMusic().getMusicId())
                    .title(emotionLogDto.getMusic().getTitle())
                    .artist(emotionLogDto.getMusic().getArtist())
                    .build();

            // 새로운 음악을 DB에 저장
            music = musicRepository.save(newMusic);
        }
        // 감정 로그 생성
        EmotionLog savedEmotionLog = emotionLogService.createEmotionLog(
                sessionUser.getUserId(),
                music.getMusicId(),
                emotionTag.getEmotionTagId(),
                emotionLogDto.getContents());

        // DTO 변환
        EmotionLogDto responseDto = EmotionLogDto.builder()
                .emotionLogId(savedEmotionLog.getEmotionLogId())
                .music(savedEmotionLog.getMusic())
                .emotionTag(savedEmotionLog.getEmotionTag())
                .contents(savedEmotionLog.getContents())
                .createdAt(savedEmotionLog.getCreatedAt())
                .build();

        return new ResponseEntity<EmotionLogDto>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponseDto> getEmotionLogsByUser() {
        String currentUserId = getUserIdFromSession(); // 세션에서 사용자 ID 가져오기
        EmotionTag emotionTag = emotionTagRepository.findByUser_UserId(currentUserId);
        List<EmotionLog> emotionLogs = emotionLogService.getEmotionLogsByEmotionTagId(emotionTag.getEmotionTagId());

        // emotionLog -> DTO 변환 (매핑과정)
        List<EmotionLogDto> emotionLogDtos = emotionLogs.stream()
                .map(log -> new EmotionLogDto(
                        log.getEmotionLogId(),
                        log.getMusic(),
                        log.getEmotionTag(),
                        log.getContents(),
                        log.getCreatedAt()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponseDto(true, "감정 로그를 성공적으로 조회하였습니다.", emotionLogDtos));
    }
}
