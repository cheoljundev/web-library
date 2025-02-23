package com.weblibrary.web.users.controller;

import com.weblibrary.domain.rental.repository.RentalSearchCond;
import com.weblibrary.domain.rental.service.RentalInfo;
import com.weblibrary.domain.rental.service.RentalService;
import com.weblibrary.domain.user.exception.NotFoundUserException;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.service.UserInfo;
import com.weblibrary.domain.user.service.UserService;
import com.weblibrary.web.account.controller.LoginUser;
import com.weblibrary.web.argumentresolver.Login;
import com.weblibrary.web.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UsersController는 사용자 정보와 대출 목록을 조회하는 컨트롤러입니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;
    private final RentalService rentalService;

    /**
     * 현재 로그인한 사용자의 정보를 반환합니다.
     *
     * @param loginUser 로그인한 사용자 정보
     * @return ResponseEntity<UserInfo> 사용자 정보
     */
    @GetMapping("/me")
    public ResponseEntity<UserInfo> userInfo(@Login LoginUser loginUser) {
        UserInfo user = userService.findUserInfoById(loginUser.getUserId())
                .orElseThrow(NotFoundUserException::new);
        return ResponseEntity.ok(user);
    }

    /**
     * 현재 로그인한 사용자의 대출 목록을 반환합니다.
     *
     * @param cond     검색 조건
     * @param pageable 페이지 정보
     * @param loginUser 로그인한 사용자 정보
     * @return ResponseEntity<PageResponse<RentalInfo>> 대출 목록 페이지 응답
     */
    @GetMapping("/rentals")
    public ResponseEntity<PageResponse<RentalInfo>> myRentals(@ModelAttribute("cond") RentalSearchCond cond, Pageable pageable, @Login LoginUser loginUser) {
        User user = userService.findById(loginUser.getUserId())
                .orElseThrow(NotFoundUserException::new);
        cond.setUsername(user.getUsername());

        PageResponse<RentalInfo> page = rentalService.findAll(cond, pageable);

        return ResponseEntity.ok(page);
    }
}
