package mue.service;

import java.util.*;
import mue.entity.*;
import mue.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MusicService {

    private final MusicRepository musicRepository;
    private final PlaylistRepository playlistRepository;

    @Autowired
    public MusicService(MusicRepository musicRepository, PlaylistRepository playlistRepository) {
        this.musicRepository = musicRepository;
        this.playlistRepository = playlistRepository;
    }

    // 1. 특정 유저의 특정 플레이리스트에 포함된 모든 음악 조회
    public List<Music> getMusicByUserAndPlaylist(String userId, String playlistId) {
        // 해당 플레이리스트가 특정 유저의 것인지 확인
        Optional<Playlist> playlist = playlistRepository.findByPlaylistId(playlistId);

        if (playlist.isPresent() && playlist.get().getUser().getUserId().equals(userId)) {
            // 특정 플레이리스트에 포함된 모든 음악 조회
            return musicRepository.findByPlaylist(playlist.get());
        } else {
            throw new IllegalArgumentException("해당 유저의 플레이리스트가 아닙니다.");
        }
    }

    // 2. 특정 유저의 특정 플레이리스트에 곡 추가
    public Music addMusicToUserPlaylist(String musicId, String userId, String playlistId, String title, String artist) {
        // 해당 플레이리스트가 특정 유저의 것인지 확인
        Optional<Playlist> playlist = playlistRepository.findById(playlistId);

        if (playlist.isPresent() && playlist.get().getUser().getUserId().equals(userId)) {
            // 현재 시간으로 playedAt 설정
            Date currentPlayedAt = new Date();

            // 빈 감정 로그 리스트 초기화
            List<EmotionLog> emptyEmotionLogs = List.of();

            // 새로운 음악 생성
            Music newMusic = new Music(
                    UUID.randomUUID().toString(), // 재생 기록 고유 ID 생성
                    musicId, // 음악 ID
                    title, // 음악 제목
                    artist, // 아티스트 이름
                    currentPlayedAt, // playedAt은 현재 시간으로 설정
                    playlist.get(), // 플레이리스트 객체 전달 (Optional로 감싸져 있다면 get()으로 추출)
                    emptyEmotionLogs // 초기에는 빈 감정 로그 리스트 전달
            );

            // 음악 저장 및 플레이리스트에 추가
            return musicRepository.save(newMusic);
        } else {
            throw new IllegalArgumentException("해당 유저의 플레이리스트가 아닙니다.");
        }
    }

    // 3. 특정 유저의 특정 감정을 기록한 모든 음악 조회
    public List<Music> getMusicByUserAndEmotionTag(String emotionTagId) {
        return musicRepository.findByEmotionTagId(emotionTagId);
    }

    // 4. 특정 유저의 특정 감정을 기록한 음악 정보 등록
    public Music createMusicWithEmotion(
            String musicId, String title, String artist) {

        // Music 엔티티 생성
        Music music = Music.builder()
                .musicId(musicId) // 음악 ID
                .title(title) // 음악 제목
                .artist(artist) // 아티스트 이름
                .playedAt(new Date()) // 재생된 시간 (생성 시간)
                .playlist(null) // 플레이리스트와 연결하는 것이 아니므로 null 처리
                .emotionLogs(List.of()) // 초기 감정 로그 리스트는 비어 있음
                .build();

        // Music 저장
        return musicRepository.save(music);
    }
}