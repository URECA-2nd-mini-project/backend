package mue.controller;

import java.util.*;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

        @PostMapping
        public ResponseEntity<EmotionLogDto> createEmotionLog(@RequestBody EmotionLogDto emotionLogDto) {
                // 세션에서 사용자 정보를 가져옴
                SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");

                // 음악 정보와 감정 태그 정보가 올바르게 제공되었는지 확인
                Music music = musicRepository.findByMusicId(emotionLogDto.getMusic().getMusicId());
                EmotionTag emotionTag = emotionTagRepository
                                .findByEmotionTagId(emotionLogDto.getEmotionTagId());
                System.out.println(emotionTag);

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
                                .emotionTag(savedEmotionLog.getEmotionTag().getEmotionTagId())
                                .contents(savedEmotionLog.getContents())
                                .createdAt(savedEmotionLog.getCreatedAt())
                                .build();

                return new ResponseEntity<EmotionLogDto>(responseDto, HttpStatus.CREATED);
        }

        // 1. 특정 musicId에 해당하는 감정 로그 조회
        @GetMapping("/{musicId}")
        public ResponseEntity<List<EmotionLogDto>> getEmotionLogsByMusicId(@PathVariable String musicId) {
                // 해당 musicId로 emotionLog 리스트 조회
                List<EmotionLog> emotionLogs = emotionLogService.findByMusicId(musicId);

                // EmotionLog 엔티티 리스트를 EmotionLogDto로 변환
                List<EmotionLogDto> emotionLogDtos = emotionLogs.stream()
                                .map(EmotionLogDto::fromEntity) // EmotionLog -> EmotionLogDto 변환
                                .collect(Collectors.toList());

                return new ResponseEntity<>(emotionLogDtos, HttpStatus.OK);
        }
}
