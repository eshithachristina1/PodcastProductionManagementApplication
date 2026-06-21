package com.podcastmanagement.config;

import com.podcastmanagement.entity.User;
import com.podcastmanagement.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;

    public AuthInterceptor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userIdStr = request.getHeader("X-User-Id");
        if (userIdStr != null) {
            try {
                Long userId = Long.parseLong(userIdStr);
                userRepository.findById(userId).ifPresent(user -> {
                    request.setAttribute("currentUser", user);
                });
            } catch (NumberFormatException ignored) {}
        }
        return true;
    }
}
