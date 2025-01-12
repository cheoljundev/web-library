package com.weblibrary.domain.admin.controller;

import com.weblibrary.AppConfig;
import com.weblibrary.core.controller.dto.response.ErrorResponse;
import com.weblibrary.core.controller.dto.response.JsonResponse;
import com.weblibrary.domain.admin.controller.dto.RoleNameDto;
import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.user.model.User;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping("/users")
public class AdminUsersController {

    private final AppConfig appConfig = AppConfig.getInstance();
    private final AdminService adminService = appConfig.adminService();

    @PatchMapping("/{id}/role")
    public JsonResponse setRole(HttpServletResponse response, HttpSession session, @PathVariable("id") Long id, @RequestBody RoleNameDto roleNameDto) {
        if (isDefault(session)) {
            return getForbiddenResponse(response);
        }
        if (roleNameDto.getRoleName().equals("default")) {
            if (!adminService.setUserAsDefault(id)) {
                return new ErrorResponse(HttpServletResponse.SC_BAD_REQUEST, "권한 변경에 실패했습니다.");
            }
        } else {
            if (adminService.setUserAsAdmin(id)) {
                return new ErrorResponse(HttpServletResponse.SC_BAD_REQUEST, "권한 변경에 실패했습니다.");
            }
        }

        return new JsonResponse(HttpServletResponse.SC_OK, "정상적으로 권한 변경 완료");
    }

    @DeleteMapping("/{id}")
    public JsonResponse deleteUser(HttpServletResponse response, HttpSession session, @PathVariable("id") Long id) {
        if (isDefault(session)) {
            return getForbiddenResponse(response);
        }
        User removed = adminService.deleteUser(id);

        if (removed == null) {
            return new ErrorResponse(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "찾을 수 없는 유저입니다."
            );
        }

        return new JsonResponse(
                HttpServletResponse.SC_OK,
                "정상적으로 유저가 삭제되었습니다."
        );
    }

    private ErrorResponse getForbiddenResponse(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return new ErrorResponse(HttpServletResponse.SC_FORBIDDEN, "권한이 없습니다.");
    }

    private boolean isDefault(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return !adminService.isAdmin(user.getId());
    }
}
