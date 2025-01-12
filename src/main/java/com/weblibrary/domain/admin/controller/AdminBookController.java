package com.weblibrary.domain.admin.controller;

import com.weblibrary.AppConfig;
import com.weblibrary.core.controller.dto.response.ErrorResponse;
import com.weblibrary.core.controller.dto.response.JsonResponse;
import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.model.dto.ModifyBookInfo;
import com.weblibrary.domain.book.model.dto.NewBookInfo;
import com.weblibrary.domain.user.model.User;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class AdminBookController {

    private final AppConfig appConfig = AppConfig.getInstance();
    private final AdminService adminService = appConfig.adminService();

    @PostMapping("/add")
    public JsonResponse addBook(HttpSession session, HttpServletResponse response, @RequestBody NewBookInfo newBookInfo) {
        if (isDefault(session)) {
            return getForbiddenResponse(response);
        }

        System.out.println("newBookInfo = " + newBookInfo);
        System.out.println("newBookInfo.getBookName() = " + newBookInfo.getBookName());
        System.out.println("newBookInfo.getIsbn() = " + newBookInfo.getIsbn());

        adminService.addBook(newBookInfo);
        return new JsonResponse(HttpServletResponse.SC_OK, "정상 등록되었습니다.");
    }

    @DeleteMapping("/{bookId}")
    public JsonResponse deleteBook(HttpSession session, HttpServletResponse response, @PathVariable("bookId") Long bookId) {
        if (isDefault(session)) {
            return getForbiddenResponse(response);
        }

        Book removed = adminService.deleteBook(bookId);

        if (removed == null) {
            return new ErrorResponse(HttpServletResponse.SC_BAD_REQUEST, "삭제되지 않았습니다.");
        }

        return new JsonResponse(HttpServletResponse.SC_OK, "정상 삭제되었습니다.");

    }

    @PutMapping("/{bookId}")
    public JsonResponse modifyBook(HttpSession session, HttpServletResponse response, @PathVariable("bookId") Long bookId, @RequestBody ModifyBookInfo modifyBookInfo) {
        if (isDefault(session)) {
            return getForbiddenResponse(response);
        }

        Book oldBook = adminService.modifyBook(bookId, modifyBookInfo);

        if (oldBook == null) {
            return new ErrorResponse(HttpServletResponse.SC_BAD_REQUEST, "수정되지 않았습니다.");
        }

        return new JsonResponse(HttpServletResponse.SC_OK, "수정되었습니다.");

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
