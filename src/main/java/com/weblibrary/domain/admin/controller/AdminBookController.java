package com.weblibrary.domain.admin.controller;

import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.model.dto.ModifyBookDto;
import com.weblibrary.domain.book.model.dto.NewBookDto;
import com.weblibrary.domain.user.model.User;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class AdminBookController {

    private final AdminService adminService;

    @PostMapping("/add")
    public ResponseEntity<String> addBook(HttpSession session, @RequestBody NewBookDto newBookDto) {
        if (isDefault(session)) {
            return new ResponseEntity<>("권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        log.debug("newBookInfo={}", newBookDto);
        log.debug("newBookInfo.bookName={}", newBookDto.getBookName());
        log.debug("newBookInfo.isbn={}", newBookDto.getIsbn());

        adminService.addBook(newBookDto);
        return new ResponseEntity<>("정상 등록되었습니다.", HttpStatus.OK);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> deleteBook(HttpSession session, @PathVariable("bookId") Long bookId) {
        if (isDefault(session)) {
            return new ResponseEntity<>("권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        Book removed = adminService.deleteBook(bookId);

        if (removed == null) {
            return new ResponseEntity<>("삭제되지 않았습니다.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("정상 삭제되었습니다.", HttpStatus.OK);

    }

    @PutMapping("/{bookId}")
    public ResponseEntity<String> modifyBook(HttpSession session, HttpServletResponse response, @PathVariable("bookId") Long bookId, @RequestBody ModifyBookDto modifyBookDto) {
        if (isDefault(session)) {
            return new ResponseEntity<>("권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        Book oldBook = adminService.modifyBook(bookId, modifyBookDto);

        if (oldBook == null) {
            return new ResponseEntity<>("수정되지 않았습니다.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("정상 수정되었습니다.", HttpStatus.OK);

    }

    private boolean isDefault(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return !adminService.isAdmin(user.getId());
    }

}
