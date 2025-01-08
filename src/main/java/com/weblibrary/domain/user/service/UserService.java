package com.weblibrary.domain.user.service;

import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.repository.MemoryUserRepository;
import com.weblibrary.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserService {
    @Getter
    private static final UserService instance = new UserService();
    private final UserRepository userRepository = MemoryUserRepository.getInstance();

    /**
     * 가입 처리 서비스 계층 메서드
     * @param paramMap : 파라미터를 담은 Map
     */
    public void join(Map<String, String> paramMap) {
        String username = paramMap.get("username");
        String password = paramMap.get("password");
        User user = new User(MemoryUserRepository.lastId++, username, password);
        userRepository.save(user);
    }

    /**
     * 로그인 처리를 담은 서비스 계층 메서드
     *
     * @param request : session을 이용하기 위해 request를 받는다.
     * @param paramMap : 파라미터를 담은 Map
     */
    public void login(HttpServletRequest request, Map<String, String> paramMap) {
        String username = paramMap.get("username");
        String password = paramMap.get("password");

        User user = userRepository.findByUsername(username);

        if (user.getPassword().equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
        }

    }
}
