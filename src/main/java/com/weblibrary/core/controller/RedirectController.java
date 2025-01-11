package com.weblibrary.core.controller;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Map;

/**
 * 리다이렉트를 위한 컨트롤러
 */
public interface RedirectController extends Controller {
    /**
     * 컨트롤러에서 요청을 처리하는 process 메서드
     *
     * @param request : request 객체. 메서드 구분등으로 사용
     * @param paramMap : 파라미터를 담은 Map
     * @return : 리다이렉트할 논리적 주소를 반환합니다.
     */
    String process(HttpServletRequest request, Map<String, String> paramMap) throws IOException;
}
