package mue.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import mue.entity.Playlist;
import mue.repository.PlaylistRepository;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    // 사용자 ID로 모든 플레이리스트 찾기
    public List<Playlist> getAllPlaylistsByUserId(String userId) {
        // 사용자 ID에 따른 플레이리스트 목록 반환
        return playlistRepository.findByUser_UserId(userId);
    }

    // 플레이리스트 ID로 플레이리스트 세부정보 찾기
    public Optional<Playlist> getPlaylistById(String playlistId) {
        // 플레이리스트 ID로 세부정보 반환
        return playlistRepository.findByPlaylistId(playlistId);
    }

    // 플레이리스트 생성
    public Playlist createPlaylist(Playlist playlist) {
        // 새 플레이리스트 저장
        return playlistRepository.save(playlist);
    }

    // 플레이리스트 수정
    public Playlist updatePlaylist(String playlistId, Playlist updatedPlaylist) {
        // 플레이리스트를 찾아서 수정하고 없으면 예외 처리
        Playlist existingPlaylist = playlistRepository.findByPlaylistId(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        // 수정하기
        existingPlaylist.setPlaylistTitle(updatedPlaylist.getPlaylistTitle()); // 플레이리스트 이름 수정
        existingPlaylist.setContents(updatedPlaylist.getContents()); // 플레이리스트 설명 수정
        return playlistRepository.save(existingPlaylist); // 수정된 플레이리스트 저장
    }

    // 여러 개의 플레이리스트 삭제
    public void deletePlaylists(List<String> playlistIds) {
        if (playlistIds == null || playlistIds.isEmpty()) {
            throw new IllegalArgumentException("삭제할 플레이리스트 ID가 없습니다.");
        }

        for (String playlistId : playlistIds) {
            // 개별 플레이리스트 삭제 처리
            deletePlaylist(playlistId);
        }
    }

    // 기존의 개별 플레이리스트 삭제 메소드
    public void deletePlaylist(String playlistId) {
        // 플레이리스트가 존재하는지 확인
        if (!playlistRepository.existsById(playlistId)) {
            throw new IllegalArgumentException("해당 플레이리스트가 존재하지 않습니다: " + playlistId);
        }
        // 플레이리스트 삭제
        playlistRepository.deleteById(playlistId);
    }
}