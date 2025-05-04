package com.cloop.cloop.auth.controller;

import com.cloop.cloop.auth.config.GoogleConfig;
import com.cloop.cloop.auth.config.JwtUtil;
import com.cloop.cloop.auth.domain.User;
import com.cloop.cloop.auth.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final GoogleConfig googleConfig;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    // Google OAuth 로그인
    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> requestBody) {
        String idToken = requestBody.get("id_token");
        String googleId = extractGoogleIdFromToken(idToken);
        if (googleId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "유효하지 않은 Google ID 토큰입니다."));
        }

        Optional<User> existingUser = userRepository.findByGoogleId(googleId);
        if (existingUser.isPresent()) {
            User user  = existingUser.get();
            String accessToken = jwtUtil.generateToken(user.getUserId(), user.getNickname());
            String refreshToekn = jwtUtil.generateRefreshToken(user.getUserId());

            return ResponseEntity.ok(Map.of(
                    "status", "login",
                    "userId", user.getUserId(),
                    "nickname", user.getNickname(),
                    "access_token", accessToken,
                    "refresh_token", refreshToekn
            ));
        }
        // 회원가입이 필요하다면, 201 Created 반환
        return ResponseEntity.status(201).body(Map.of(
                "status", "회원가입이 필요합니다,",
                "googleId", googleId
        ));
    }

    @PostMapping("/google/signup")
    public ResponseEntity<?> googleSignUp(@RequestBody Map<String, Object> requestBody) {
        try {
            String googleId = (String) requestBody.get("googleId");
            String nickname = (String) requestBody.get("nickname");
            String gender = (String) requestBody.get("gender");

            User newUser = new User();
            newUser.setGoogleId(googleId);
            newUser.setNickname(nickname);
            newUser.setGender(gender);

            User savedUser = userRepository.save(newUser);

            String accessToken = jwtUtil.generateToken(savedUser.getUserId(), savedUser.getNickname());
            String refreshToken = jwtUtil.generateRefreshToken(savedUser.getUserId());

            return ResponseEntity.ok(Map.of(
                    "userId", savedUser.getUserId(),
                    "nickname", savedUser.getNickname(),
                    "accessToken", accessToken,
                    "refreshToken", refreshToken
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "회원가입 처리 중 서버 오류 발생"));
        }
    }

    // Google ID 토큰에서 Google ID 추출
    private String extractGoogleIdFromToken(String idToken) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JSON_FACTORY)
                    .setAudience(googleConfig.getClientIds())       // 여러 개의 구글 클라이언트 ID 허용
                    .build();

            GoogleIdToken googleIdToken = verifier.verify(idToken);
            if (googleIdToken != null) {
                return googleIdToken.getPayload().getSubject();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
