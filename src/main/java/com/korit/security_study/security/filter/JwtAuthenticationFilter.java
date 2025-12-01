package com.korit.security_study.security.filter;

import com.korit.security_study.entity.User;
import com.korit.security_study.repository.UserRepository;
import com.korit.security_study.security.jwt.JwtUtils;
import com.korit.security_study.security.model.Principal;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements Filter {
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("JwtAuthenticationFilter : doFilter");
        HttpServletRequest req = (HttpServletRequest) servletRequest;

//        리스트에 해당하지 않으면 그냥 다음 필터로 넘긴다
        List<String> methodList = List.of("POST", "PUT", "GET", "PATCH", "DELETE");
        if (!methodList.contains(req.getMethod())) {
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }

//        Header의 Authorization을 가져온다
        String authorization = req.getHeader("Authorization");
        System.out.println("Bearer Token : " + authorization);

        if (jwtUtils.isBearer(authorization)) {                 // bearer 형식인지 확인
            String accessToken = jwtUtils.removeBearer(authorization);
            try {
                Claims claims = jwtUtils.getClaims(accessToken);
                String id = claims.getId();
                Integer userId = Integer.parseInt(id);
//                userId로 회원 조회 -> Principal
                Optional<User> foundUser = userRepository.findById(userId);
                foundUser.ifPresentOrElse((user -> {
                    Principal principal = Principal.builder()
                            .userId(user.getUserId())
                            .username(user.getUsername())
                            .password(user.getPassword())
                            .email(user.getEmail())
                            .userRoleList(user.getUserRoleList())
                            .build();
                    // UsernamePasswordAuthenticationToken 객체 생성
                    Authentication authentication = new UsernamePasswordAuthenticationToken(principal,"",principal.getAuthorities());
                    // spring security의 인증 컨텍스트에 인증 객체 저장 => 이후의 요청은 인증된 사용자로 간주됨
                    SecurityContextHolder.getContext().setAuthentication(authentication);       // 인증 완료
                }), () -> {
                    throw new AuthenticationServiceException("인증 실패");
                });

            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
