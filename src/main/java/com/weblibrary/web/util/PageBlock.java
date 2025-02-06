package com.weblibrary.web.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class PageBlock {
    private final int startPage;
    private final int endPage;
    private final List<Integer> pageNumbers;
    private final int currentPage;
    private final int totalPages;
}
