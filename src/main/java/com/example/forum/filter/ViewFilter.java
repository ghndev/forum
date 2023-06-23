package com.example.forum.filter;

import com.example.forum.service.PostService;
import com.example.forum.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ViewFilter extends OncePerRequestFilter {

    private final PostService postService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith("/posts") && !path.equals("/posts") && !path.equals("/posts/create")
                && !path.startsWith("/posts/category") && !path.startsWith("/posts/search")) {
            Long postId = Long.parseLong(path.substring(7));
            Optional<Cookie> cookies = CookieUtils.getCookie(request, "pv");
            if (cookies.isPresent()) {
                Cookie cookie = cookies.get();
                if (!CookieUtils.isPostViewed(cookie, postId)) {
                    postService.increasePostViewCount(postId);
                    cookie.setValue(cookie.getValue() + "[" + postId + "]");
                    response.addCookie(cookie);
                }
            } else {
                postService.increasePostViewCount(postId);
                CookieUtils.addCookie(response, "pv", "[" + postId + "]", 60 * 60 * 24);
            }
        }
        filterChain.doFilter(request, response);
    }
}
