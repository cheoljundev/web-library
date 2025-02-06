package com.weblibrary.web.util;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class PaginationUtil {
    /**
     * 주어진 Page 객체와 블록 크기를 기반으로 페이지 블록 정보를 생성합니다.
     *
     * @param page      Spring Data Page 객체
     * @param blockSize 한 블록에 표시할 페이지 수
     * @return PageBlock 객체
     */
    public static <T> PageBlock createPageBlock(Page<T> page, int blockSize) {
        int currentPage = page.getNumber();
        int totalPages = page.getTotalPages();
        int startPage = (currentPage / blockSize) * blockSize;
        int endPage = Math.min(startPage + blockSize, totalPages);

        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = startPage; i < endPage; i++) {
            pageNumbers.add(i);
        }

        return new PageBlock(startPage, endPage, pageNumbers, currentPage, totalPages);

    }
}
