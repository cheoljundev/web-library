package com.weblibrary.web.admin.controller;

import com.weblibrary.domain.admin.model.RoleType;
import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.user.dto.SetUserDto;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.web.core.dto.response.ErrorResponse;
import com.weblibrary.web.core.dto.response.JsonResponse;
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
    private final AdminUtils adminUtils;

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

        /* 찾은 유저 반복문 */
        for (User user : findUsers) {
            log.debug("user={}", user);

            /* 찾은 유저의 가장 높은 RoleType 가져오기 */
            RoleType roleType = adminService.findUserRoleType(user.getId());

            log.debug("roleType={}", roleType);
            log.debug("roleType.name()={}", roleType.name());

            SetUserDto userDto = SetUserDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .roleTypeName(roleType.name())
                    .build();

            log.debug("userDto={}", userDto);

            dtos.add(userDto);
        }
        return dtos;
    }

    @GetMapping("/admin/user")
    public String adminUserPage(@SessionAttribute(name = "user", required = false) User user) {
        if (adminUtils.isDefault(user)) {
            return "access-denied";
        }
        return "admin/user";
    }

    @ResponseBody
    @PatchMapping("/users/{id}/role")
    public ResponseEntity<JsonResponse> setRole(@SessionAttribute(name = "user", required = false) User user, @PathVariable("id") Long id, @RequestBody RoleType roleType) {

        if (adminUtils.isDefault(user)) {
            return new ResponseEntity<>(ErrorResponse.builder()
                    .code("roleError")
                    .message("권한이 없습니다.")
                    .build(), HttpStatus.FORBIDDEN);
        }

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
    public ResponseEntity<JsonResponse> deleteUser(@SessionAttribute(name = "user", required = false) User user, @PathVariable("id") Long id) {
        if (adminUtils.isDefault(user)) {
            return new ResponseEntity<>(ErrorResponse.builder()
                    .code("roleError")
                    .message("권한이 없습니다.")
                    .build(), HttpStatus.FORBIDDEN);
        }
        User removed = adminService.deleteUser(id);

        if (removed == null) {
            return new ResponseEntity<>(ErrorResponse.builder()
                    .message("찾을 수 없는 유저입니다.")
                    .build(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(JsonResponse.builder()
                .message("정상적으로 유저가 삭제되었습니다.")
                .build(), HttpStatus.OK);
    }
}
