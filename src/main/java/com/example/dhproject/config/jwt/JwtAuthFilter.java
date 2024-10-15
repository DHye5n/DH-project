package com.example.dhproject.config.jwt;

import com.example.dhproject.config.auth.PrincipalDetailsService;
import com.example.dhproject.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final PrincipalDetailsService principalDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // JWT 인증 로직
        String BEARER_PREFIX = "Bearer ";
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        SecurityContext securityContext = SecurityContextHolder.getContext();

        if (!ObjectUtils.isEmpty(authorization) &&
                authorization.startsWith(BEARER_PREFIX) &&
                securityContext.getAuthentication() == null) {
            String accessToken = authorization.substring(BEARER_PREFIX.length());
            String username = jwtService.getUsername(accessToken);
            UserDetails userDetails = principalDetailsService.loadUserByUsername(username);
            // 인증 객체 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            securityContext.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(securityContext);

//            // SecurityContext에 설정
//            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }

}
