package com.cloop.cloop.auth.repository;

import com.cloop.cloop.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByGoogleId(String googleId);

    // 닉네임 중복 체크
    Optional<User> findByNickname(String nickname);

    // userId로 user 찾기
    Optional<User> findByUserId(Long userId);
}
