package org.finalpjt.hraccoon.global.security;

import java.io.IOException;

import org.finalpjt.hraccoon.domain.auth.data.PayLoad;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String jwt = jwtProvider.resolveToken(request);
		String url = request.getRequestURI();

		try {
			if (jwt != null) {
				PayLoad payLoad = jwtProvider.getPayLoad(jwt);

				if (payLoad.getType().equals("RTK") && !url.equals("/api/v1/auth/re-issuance") && !url.equals(
					"/api/v1/auth/sign-out")) {
					throw new JwtException(SecurityMessageConstants.WRONG_TOKEN);
				}

				String userId = payLoad.getUserId();
				String authority = "ROLE_" + payLoad.getAuthority();

				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
					userId, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authority));

				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		} catch (Exception e) {
			log.error("Exception from JwtAuthenticationFilter : {}", e.getMessage());
			request.setAttribute("exception", e);
		}

		filterChain.doFilter(request, response);
	}

	/**
	 * 로그인의 경우에는 필터를 타지 않도록 설정하는 함수
	 * @param request HttpServletRequest
	 * @return boolean
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return request.getServletPath().equals("/api/v1/auth/sign-in");
	}

}
