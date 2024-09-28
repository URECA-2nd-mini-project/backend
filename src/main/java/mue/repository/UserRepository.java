package mue.repository;

import mue.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// User 엔티티에 대한 CRUD 작업을 처리

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // userId로 User 엔티티 조회
    Optional<User> findByUserId(String userId);
}