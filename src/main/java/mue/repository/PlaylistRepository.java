package mue.repository;

import mue.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

// Playlist 엔티티에 대한 CRUD 작업을 처리
public interface PlaylistRepository extends JpaRepository<Playlist, String> {
}
