package mue.service;

import mue.entity.Music;
import mue.entity.Playlist;
import mue.entity.EmotionLog;
import mue.repository.MusicRepository;
import mue.repository.PlaylistRepository;
import mue.repository.EmotionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MusicService {

    private final MusicRepository musicRepository;
    private final PlaylistRepository playlistRepository;
    private final EmotionLogRepository emotionLogRepository;

    @Autowired
    public MusicService(MusicRepository musicRepository, PlaylistRepository playlistRepository,
            EmotionLogRepository emotionLogRepository) {
        this.musicRepository = musicRepository;
        this.playlistRepository = playlistRepository;
        this.emotionLogRepository = emotionLogRepository;
    }

    // 1. 특정 유저의 특정 플레이리스트에 포함된 모든 음악 조회
    public List<Music> getMusicByUserAndPlaylist(String userId, String playlistId) {
        // 해당 플레이리스트가 특정 유저의 것인지 확인
        Optional<Playlist> playlist = playlistRepository.findById(playlistId);

        if (playlist.isPresent() && playlist.get().getUser().getUserId().equals(userId)) {
            // 특정 플레이리스트에 포함된 모든 음악 조회
            return musicRepository.findByPlaylist(playlistId);
        } else {
            throw new IllegalArgumentException("해당 유저의 플레이리스트가 아닙니다.");
        }
    }

    // 2. 특정 유저의 특정 플레이리스트에 곡 추가
    public Music addMusicToUserPlaylist(String userId, String playlistId, String title, String artist, int duration,
            String thumbnail, String lyrics) {
        // 해당 플레이리스트가 특정 유저의 것인지 확인
        Optional<Playlist> playlist = playlistRepository.findById(playlistId);

        if (playlist.isPresent() && playlist.get().getUser().getUserId().equals(userId)) {
            // 현재 시간으로 playedAt 설정
            Date currentPlayedAt = new Date();

            // 빈 감정 로그 리스트 초기화
            List<EmotionLog> emptyEmotionLogs = List.of();

            // 새로운 음악 생성
            Music newMusic = new Music(
                    UUID.randomUUID().toString(), // musicId는 UUID로 생성
                    title,
                    artist,
                    duration,
                    thumbnail,
                    lyrics,
                    currentPlayedAt, // playedAt은 현재 시간으로 설정
                    playlist.get(), // 플레이리스트 객체 전달
                    emptyEmotionLogs // 초기에는 빈 감정 로그 리스트 전달
            );

            // 음악 저장 및 플레이리스트에 추가
            return musicRepository.save(newMusic);
        } else {
            throw new IllegalArgumentException("해당 유저의 플레이리스트가 아닙니다.");
        }
    }

    // 3. 특정 유저의 특정 감정을 기록한 모든 음악 조회
    public List<Music> getMusicByUserAndEmotionTag(String userId, String emotionTag) {
        return musicRepository.findByEmotionTag(emotionTag);
    }
}