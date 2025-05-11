package com.cloop.cloop.auth.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${SECRET_KEY}")
    private String SECRET_KEY;  // 인스턴스 필드

    private Key SIGNING_KEY; // 정적 필드에서 인스턴스 필드로 변경

    private static final long EXPIRE_TIME = 1000 * 60 * 60 * 24;        // 액세스 토큰 (24시간)
    private static final long REFRESH_TIME = 1000 * 60 * 60 * 24 * 7;   // 리프레시 토큰 (7일)
    private final Map<String, String> refreshTokenStorage = new HashMap<>();        // 리프레시 토큰 저장소


    // SECRET_KEY 초기화
    public JwtUtil(@Value("${SECRET_KEY}") String secretKey) {
        this.SECRET_KEY = secretKey;
        if (SECRET_KEY == null) {
            throw new RuntimeException("Secret Key is null");
        }

        this.SIGNING_KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        System.out.println("Secret Key Loaded: " + SECRET_KEY.substring(0, 5) + "****");
    }

    // JWT 액세스 토큰 생성
    public String generateToken(Long userId, String nickname) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("nickname", nickname)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT에서 userId 추출
    public Long extractUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());     // sub에서 userId 추출
    }

    // 리프레시 토큰
    public String generateRefreshToken(Long userId) {
        String refreshToken = Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TIME))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();
        refreshTokenStorage.put(userId.toString(), refreshToken);
        return refreshToken;
    }

    // JWT 유효성 검사
    public boolean validateToken(String token) {
        try{
            Jwts.parserBuilder().setSigningKey(SIGNING_KEY).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 리프레시 토큰 사용 -> 새로운 액세스 토큰 발급
    public String refreshAccessToken(String refreshToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            String userId = claims.getSubject();
            if (refreshToken.equals(refreshTokenStorage.get(userId))) {
                return generateToken(Long.parseLong(userId), "nickname_placeholder");
            }
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
        return null;
    }

    // 로그아웃 시 리프레시 토큰 무효화
    public void revokeRefreshToken(Long userId) {
        refreshTokenStorage.remove(userId.toString());
    }
}
