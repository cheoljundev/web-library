package com.weblibrary.domain.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weblibrary.AppConfig;
import com.weblibrary.core.controller.JsonResponseController;
import com.weblibrary.core.controller.dto.response.ErrorResponse;
import com.weblibrary.core.controller.dto.response.JsonResponse;
import com.weblibrary.domain.admin.controller.dto.RoleNameDto;
import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.user.model.User;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AdminUsersController implements JsonResponseController {

    private final AppConfig appConfig = AppConfig.getInstance();
    private final AdminService adminService = appConfig.adminService();
    private final ObjectMapper mapper = appConfig.objectMapper();

    @Override
    public JsonResponse response(HttpServletRequest request) throws IOException {

        User admin = (User) request.getSession().getAttribute("user");

        if (!adminService.isAdmin(admin.getId())) {
            return new ErrorResponse(
                    HttpServletResponse.SC_FORBIDDEN,
                    "권한이 없습니다."
            );
        }

        String method = request.getMethod();
        String[] uriParts = cleanUrlParts(request.getRequestURI());
        Long userId = Long.parseLong(uriParts[2]);


        switch (method) {
            case "DELETE" -> {
                if (uriParts.length == 3) {
                    return deleteUser(userId);
                }
            }
            case "PATCH" -> {
                if (uriParts.length == 4 && uriParts[3].equals("role")) {
                    return setRole(request, userId);
                }
            }
        }

        return null;
    }

    private static String[] cleanUrlParts(String uri) {
        String[] uriParts = uri.split("/");
        //빈 문자열은 제거
        uriParts = Arrays.copyOfRange(uriParts, 1, uriParts.length);
        return uriParts;
    }

    private JsonResponse deleteUser(Long userId) {
        User removed = adminService.deleteUser(userId);

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

    private JsonResponse setRole(HttpServletRequest request, Long userId) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, UTF_8);

        RoleNameDto roleNameDto = mapper.readValue(messageBody, RoleNameDto.class);
        boolean result;

        if (roleNameDto.getRoleName().equals("default")) {
            result = adminService.setUserAsDefault(userId);
        } else {
            result = adminService.setUserAsAdmin(userId);
        }

        if (result) {
            return new JsonResponse(HttpServletResponse.SC_OK, "정상적으로 권한 변경 완료");
        }

        return new ErrorResponse(HttpServletResponse.SC_BAD_REQUEST, "권한 변경 실패");
    }
}
