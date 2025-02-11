package com.weblibrary.web.admin.controller;

import com.weblibrary.domain.account.service.AccountService;
import com.weblibrary.domain.user.model.RoleType;
import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.admin.service.UserInfo;
import com.weblibrary.domain.user.repository.UserSearchCond;
import com.weblibrary.domain.user.service.UserService;
import com.weblibrary.web.response.ErrorResponse;
import com.weblibrary.web.response.JsonResponse;
import com.weblibrary.web.util.PageBlock;
import com.weblibrary.web.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminUsersController {

    private final AdminService adminService;
    private final AccountService accountService;

    @ModelAttribute("roleTypes")
    public RoleType[] roleTypes() {
        return RoleType.values();
    }

    @GetMapping("/admin/user")
    public String adminUserPage(@ModelAttribute("cond") UserSearchCond cond, Pageable pageable, Model model) {
        Page<UserInfo> userPage = adminService.findAllUsers(cond, pageable);
        int blockSize = 10; // 한 블록에 표시할 페이지 수
        PageBlock pageBlock = PaginationUtil.createPageBlock(userPage, blockSize);

        model.addAttribute("userPage", userPage);
        model.addAttribute("pageBlock", pageBlock);

        return "admin/user";
    }

    @ResponseBody
    @PatchMapping("/users/{id}/role")
    public ResponseEntity<JsonResponse> setRole(@PathVariable("id") Long id, @RequestBody RoleType roleType) {

        log.debug("roleType={}", roleType);

        if (roleType == RoleType.DEFAULT) {
            if (!adminService.setUserAsDefault(id)) {
                return new ResponseEntity<>(ErrorResponse.builder()
                        .message("권한 변경에 실패했습니다. 이미 일반 유저입니다.")
                        .build(), HttpStatus.BAD_REQUEST);
            }
        } else {
            if (!adminService.setUserAsAdmin(id)) {
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
