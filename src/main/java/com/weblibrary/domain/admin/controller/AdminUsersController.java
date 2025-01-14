package com.weblibrary.domain.admin.controller;

import com.weblibrary.domain.admin.controller.dto.RoleNameDto;
import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.user.model.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class AdminUsersController {

    private final AdminService adminService;

    @PatchMapping("/{id}/role")
    public ResponseEntity<String> setRole(HttpSession session, @PathVariable("id") Long id, @RequestBody RoleNameDto roleNameDto) {


        if (isDefault(session)) {
            return new ResponseEntity<>("권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        log.debug("변경하는 roleName={}", roleNameDto);

        if (roleNameDto.getRoleName().equals("default")) {
            if (!adminService.setUserAsDefault(id)) {
                return new ResponseEntity<>("권한 변경에 실패했습니다.", HttpStatus.BAD_REQUEST);
            }
        } else {
            if (!adminService.setUserAsAdmin(id)) {
                return new ResponseEntity<>("권한 변경에 실패했습니다.", HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>("정상 권한 변경 완료.", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(HttpSession session, @PathVariable("id") Long id) {
        if (isDefault(session)) {
            return new ResponseEntity<>("권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        User removed = adminService.deleteUser(id);

        if (removed == null) {
            return new ResponseEntity<>("찾을 수 없는 유저입니다.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("정상적으로 유저가 삭제되었습니다..", HttpStatus.OK);
    }

    private boolean isDefault(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return !adminService.isAdmin(user.getId());
    }
}
