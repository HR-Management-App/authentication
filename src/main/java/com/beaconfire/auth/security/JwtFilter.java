package com.beaconfire.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private JwtProvider jwtProvider;

    @Autowired
    public void setJwtProvider(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<AuthUserDetail> authUserDetailOptional = jwtProvider.resolveToken(request); // extract jwt from request, generate a userdetails object

        if (!authUserDetailOptional.isPresent()){
//            response.setStatus(403);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    "tourist",
                    null,
                    null
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } else {
            AuthUserDetail authUserDetail = authUserDetailOptional.get();

            System.out.println("IN JWTFILTER");
            System.out.println("AuthUserDetail username: " + authUserDetail.getUsername());
            System.out.println("AuthUserDetail user_id: " + authUserDetail.getUser_id());
            System.out.println("AuthUserDetail email: " + authUserDetail.getEmail());
            System.out.println("authUserDetail authorities:");
            System.out.println(authUserDetail.getAuthorities());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    authUserDetail.getUsername(),
                    null,
                    authUserDetail.getAuthorities()
            ); // generate authentication object

            authentication.setDetails(TokenUserDetail.builder()
                    .user_id(authUserDetail.getUser_id())
                    .username(authUserDetail.getUsername())
                    .email(authUserDetail.getEmail())
                    .build());

            SecurityContextHolder.getContext().setAuthentication(authentication); // put authentication object in the secruitycontext
            filterChain.doFilter(request, response);
        }
    }
}

