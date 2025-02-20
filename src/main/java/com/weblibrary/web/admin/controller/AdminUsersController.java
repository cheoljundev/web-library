package com.weblibrary.web.admin.controller;

import com.weblibrary.domain.account.service.AccountService;
import com.weblibrary.domain.user.model.RoleType;
import com.weblibrary.domain.user.repository.UserSearchCond;
import com.weblibrary.domain.user.service.RoleTypeInfo;
import com.weblibrary.domain.user.service.UserInfo;
import com.weblibrary.domain.user.service.UserService;
import com.weblibrary.web.response.ErrorResponse;
import com.weblibrary.web.response.JsonResponse;
import com.weblibrary.web.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * AdminUsersController는 사용자 계정의 관리를 처리합니다.
 * 여기에는 사용자 페이지 보기, 사용자 역할 설정 및 사용자 삭제가 포함됩니다.
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminUsersController {

    private final UserService userService;
    private final AccountService accountService;

    /**
     * 모든 역할 정보를 반환합니다.
     *
     * @return 역할 정보 목록
     */
    @GetMapping("/users/roles")
    public ResponseEntity<List<RoleTypeInfo>> getRoles() {
        RoleType[] roleTypes = RoleType.values();
        List<RoleTypeInfo> roles = Arrays.stream(roleTypes)
                .map(roleType -> new RoleTypeInfo(roleType.name(), roleType.getDescription())).toList();
        return ResponseEntity.ok(roles);
    }

    /**
     * 사용자 페이지를 반환합니다.
     *
     * @param cond 검색 조건
     * @param pageable 페이지 정보
     * @param model 모델
     * @return 사용자 정보 페이지 응답
     */
    @GetMapping("/users")
    public ResponseEntity<PageResponse<UserInfo>> adminUserPage(@ModelAttribute("cond") UserSearchCond cond, Pageable pageable, Model model) {
        PageResponse<UserInfo> page = userService.findAll(cond, pageable);

        return ResponseEntity.ok(page);
    }

    @ResponseBody
    @PatchMapping("/users/{id}/role")
    public ResponseEntity<JsonResponse> setRole(@PathVariable("id") Long id, @RequestBody RoleType roleType) {

        log.debug("roleType={}", roleType);

        if (roleType == RoleType.DEFAULT) {
            if (!userService.setUserAsDefault(id)) {
                return new ResponseEntity<>(ErrorResponse.builder()
                        .message("권한 변경에 실패했습니다. 이미 일반 유저입니다.")
                        .build(), HttpStatus.BAD_REQUEST);
            }
        } else {
            if (!userService.setUserAsAdmin(id)) {
                return new ResponseEntity<>(ErrorResponse.builder()
                        .message("권한 변경에 실패했습니다. 이미 관리자 유저입니다.")
                        .build(), HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>(JsonResponse.builder()
                .message("정상 권한 변경 완료")
                .build(), HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping("/users/{id}")
    public ResponseEntity<JsonResponse> deleteUser(@PathVariable("id") Long id) {

        accountService.deleteUser(id);

        return ResponseEntity.ok().body(JsonResponse.builder()
                .message("정상적으로 유저가 삭제되었습니다.")
                .build());
    }
}
