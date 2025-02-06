package com.weblibrary.web.admin.controller;

import com.weblibrary.domain.account.service.AccountService;
import com.weblibrary.domain.user.model.RoleType;
import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.user.dto.SetUserDto;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.web.response.ErrorResponse;
import com.weblibrary.web.response.JsonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @ModelAttribute("users")
    public List<SetUserDto> users() {
        /* 모든 유저를 가지고 온다 */
        List<User> findUsers = adminService.findAllUsers();

        /* dto를 담을 list */
        List<SetUserDto> dtos = new ArrayList<>();

        findUsers.forEach(user -> {
            log.debug("user={}", user);

            /* 찾은 유저의 가장 높은 RoleType 가져오기 */
            RoleType roleType = adminService.findUserRoleType(user.getUserId());

            log.debug("roleType={}", roleType);
            log.debug("roleType.name()={}", roleType.name());

            SetUserDto userDto = SetUserDto.builder()
                    .id(user.getUserId())
                    .username(user.getUsername())
                    .roleTypeName(roleType.name())
                    .build();

            log.debug("userDto={}", userDto);

            dtos.add(userDto);
        });

        return dtos;
    }

    @GetMapping("/admin/user")
    public String adminUserPage() {
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
