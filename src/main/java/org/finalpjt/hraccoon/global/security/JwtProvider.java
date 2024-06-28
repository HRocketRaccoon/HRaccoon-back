package org.finalpjt.hraccoon.global.security;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

import javax.crypto.SecretKey;

import org.finalpjt.hraccoon.domain.auth.data.PayLoad;
import org.finalpjt.hraccoon.domain.auth.data.response.TokenResponse;
import org.finalpjt.hraccoon.global.redis.RedisDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

	private final RedisDao redisDao;

	@Value("${spring.jwt.secret-key}")
	private String jwtKey;

	@Value("${spring.jwt.life.accessToken}")
	private Long atkLife;

	@Value("${spring.jwt.life.refreshToken}")
	private Long rtkLife;

	@Value("${spring.jwt.issuer}")
	private String issuer;

	/**
	 * JWT 토큰을 추출하는 함수
	 * @param request HttpServletRequest
	 * @return JWT || null
	 */
	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	/**
	 * JWT 생성 함수
	 * @param payLoad JWT에 담을 정보
	 * @return JWT
	 */
	public String createToken(PayLoad payLoad) throws JsonProcessingException {
		SecretKey secretKey = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8)); // UTF-8로 인코딩
		Long lifeTime = payLoad.getType().equals("ATK") ? atkLife : rtkLife;
		String jwt = Jwts.builder()
			.setIssuer(issuer)
			.setSubject("JWT Token")
			.claim("userNo", payLoad.getUserNo())
			.claim("userId", payLoad.getUserId())
			.claim("type", payLoad.getType())
			.claim("authority", payLoad.getAuthority())
			.setExpiration(new Date(new Date().getTime() + lifeTime))
			.signWith(secretKey).compact();

		if (payLoad.getType().equals("RTK")) {
			redisDao.setValues(payLoad.getUserId(), jwt, Duration.ofMillis(lifeTime));
		}

		return jwt;
	}

	/**
	 * JWT 유효성 검증 함수
	 * JWT를 검증하고, PayLoad를 반환합니다.
	 * @param jwt 검증할 JWT
	 * @return payLoad
	 */
	public PayLoad getPayLoad(String jwt) throws JsonProcessingException {
		SecretKey secretKey = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));

		Claims claims = Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(jwt)
			.getBody();

		log.info("JWT claims = {}", claims);

		PayLoad payLoad = PayLoad.builder()
			.userNo(Long.valueOf((Integer)claims.get("userNo")))
			.userId((String)claims.get("userId"))
			.type((String)claims.get("type"))
			.authority((String)claims.get("authority"))
			.build();

		return payLoad;
	}

	/**
	 * JWT 재발급 함수
	 * @param refreshToken 리프레시 JWT
	 * @return TokenResponse
	 */
	public TokenResponse reIssuanceTokens(String refreshToken) throws JsonProcessingException {
		PayLoad refreshTokenPayLoad = getPayLoad(refreshToken);

		log.info("refreshTokenPayLoad = {}", refreshTokenPayLoad);

		if (!refreshTokenPayLoad.getType().equals("RTK")) {
			throw new IllegalArgumentException(SecurityMessageConstants.WRONG_TOKEN_TYPE);
		}

		String storedRefreshToken = redisDao.getValues(refreshTokenPayLoad.getUserId());
		log.info("storedRefreshToken = {}", storedRefreshToken);

		if (!refreshToken.equals(storedRefreshToken)) {
			throw new IllegalArgumentException(SecurityMessageConstants.WRONG_TOKEN);
		}

		PayLoad accessTokenPayLoad = PayLoad.builder()
			.userNo(refreshTokenPayLoad.getUserNo())
			.userId(refreshTokenPayLoad.getUserId())
			.type("ATK")
			.authority(refreshTokenPayLoad.getAuthority())
			.build();

		String newAccessToken = createToken(accessTokenPayLoad);
		String newRefreshToken = createToken(refreshTokenPayLoad);

		redisDao.setValues(refreshTokenPayLoad.getUserId(), newRefreshToken, Duration.ofMillis(rtkLife));

		return TokenResponse.builder()
			.accessToken(newAccessToken)
			.refreshToken(newRefreshToken)
			.build();
	}

	public void deleteToken(String refreshToken) throws JsonProcessingException {
		PayLoad refreshTokenPayLoad = getPayLoad(refreshToken);
		redisDao.deleteValues(refreshTokenPayLoad.getUserId());
	}
}
