package com.example.unimeeting.config.jwt;

import java.io.IOException;
import java.util.Date;

import com.example.unimeeting.config.auth.MyUserDetails;
import com.example.unimeeting.dto.LoginRequestDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {//UsernamePasswordAuthenticationFilter{

	private final AuthenticationManager authenticationManager;
	private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/user/login",
			"POST");/////////////

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
		this.authenticationManager = authenticationManager;
	}

	// Authentication 객체 만들어서 리턴 => 의존 : AuthenticationManager
	// 인증 요청시에 실행되는 함수 => /login
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException {
		System.out.println("JwtAuthenticationFilter : 진입");

		try {
			ObjectMapper om = new ObjectMapper();
			LoginRequestDto loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);

			System.out.println("JwtAuthenticationFilter : " + loginRequestDto);

			// 유저네임패스워드 토큰 생성
			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(loginRequestDto.getUserId(), loginRequestDto.getPassword());

			System.out.println("JwtAuthenticationFilter : 토큰 생성 완료");

			// 문제가 발생하는 부분 주변에 로그를 추가하여 어느 부분에서 문제가 발생하는지 확인하세요.
			Authentication authentication = authenticationManager.authenticate(authenticationToken);

			MyUserDetails principalDetails = (MyUserDetails) authentication.getPrincipal();
			System.out.println("Authentication : " + principalDetails.getUser().getUserId());
			return authentication;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	// JWT Token 생성해서 response에 담아주기
	// attemptAuthentication()의 호출 결과로 Authentication 객체 리턴시 successfulAuthentication() 의 호출 결과를 리턴함
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
											Authentication authResult) throws IOException, ServletException {
		System.out.println("successfulAuthentication ");

		MyUserDetails principalDetails = (MyUserDetails) authResult.getPrincipal();

		String jwtToken = JWT.create()
				.withSubject(principalDetails.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
				.withClaim("idx", principalDetails.getUser().getIdx())
				.withClaim("nickname", principalDetails.getUser().getNickname())
				.withClaim("userId", principalDetails.getUser().getUserId())
				.sign(Algorithm.HMAC512(JwtProperties.SECRET));

		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);
	}

}