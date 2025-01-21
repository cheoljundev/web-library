package com.weblibrary.domain.admin.controller;

import com.weblibrary.core.dto.response.ErrorResponse;
import com.weblibrary.core.dto.response.JsonResponse;
import com.weblibrary.domain.admin.model.RoleType;
import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.user.model.SetUserDto;
import com.weblibrary.domain.user.model.User;
import jakarta.servlet.http.HttpSession;
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
    public String adminUserPage(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null || !adminService.isAdmin(user.getId())) {
            return "redirect:/access-denied";
        }
        return "admin/user";
    }

    @ResponseBody
    @PatchMapping("/users/{id}/role")
    public ResponseEntity<JsonResponse> setRole(HttpSession session, @PathVariable("id") Long id, @RequestBody RoleType roleType) {


        if (isDefault(session)) {
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
    public ResponseEntity<JsonResponse> deleteUser(HttpSession session, @PathVariable("id") Long id) {
        if (isDefault(session)) {
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

    private boolean isDefault(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return !adminService.isAdmin(user.getId());
    }
}
