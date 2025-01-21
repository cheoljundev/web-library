package com.weblibrary.domain.admin.controller;

import com.weblibrary.domain.admin.model.RoleType;
import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.model.dto.NewBookDto;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.user.model.SetUserDto;
import com.weblibrary.domain.user.model.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminPageController {
    private final AdminService adminService;
    private final BookService bookService;

    @ModelAttribute("roleTypes")
    public RoleType[] roleTypes(){
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

    @ModelAttribute("books")
    public List<Book> books() {
        return bookService.findAll();
    }

    @ModelAttribute("book")
    public NewBookDto newBookModel() {
        return new NewBookDto();
    }

    @GetMapping("/admin")
    public String adminPage(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null || !adminService.isAdmin(user.getId())) {
            return "redirect:/access-denied";
        }

        return "admin/index";
    }
}
