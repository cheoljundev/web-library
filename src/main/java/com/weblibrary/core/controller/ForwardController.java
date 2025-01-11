package com.weblibrary.core.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * view를 forward 해주기 위해 사용하는 컨트롤러
 */
public interface ForwardController extends Controller {
    /**
     * 컨트롤러에서 요청을 처리하는 process 메서드
     *
     * @param request : request 객체. 메서드 구분등으로 사용
     * @param paramMap : 파라미터를 담은 Map
     * @param model : model을 담은 Map
     * @return : ModelView 생성할 때 넘겨줄 위한 논리적 주소를 반환합니다.
     */
    String process(HttpServletRequest request, HttpServletResponse response, Map<String, String> paramMap, Map<String, Object> model) throws IOException;
}
