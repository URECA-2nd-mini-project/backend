package mue.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import mue.entity.Playlist;
import mue.repository.PlaylistRepository;

@Service
public class PlaylistService {
    
    @Autowired     
    private PlaylistRepository playlistRepository;

    // 사용자 ID로 모든 플레이리스트 찾기
    public List<Playlist> getAllPlaylistsByUserId(String userId) {
        return playlistRepository.findByUserId(userId);
    }

    // 플레이리스트 ID로 플레이리스트 세부정보 찾기
    public Optional<Playlist> getPlaylistById(String playlistId) {
        return playlistRepository.findByPlaylistId(playlistId);
    }

    // 특정 감정 태그를 가진 플레이리스트를 찾는 메소드
    public List<Playlist> getPlaylistsByUserIdAndEmotionTag(String userId, String emotionTag) {
        return playlistRepository.findByUserIdAndEmotionTag(userId, emotionTag);
    }
}
