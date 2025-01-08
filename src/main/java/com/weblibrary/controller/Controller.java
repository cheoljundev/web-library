package com.weblibrary.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * 모든 요청을 받아 처리 하기 위한 web-library 프로젝트의 컨트롤러 최상위 인터페이스
 * 하위에 다른 컨트롤러 인터페이스에서 이 인터페이스를 상속받아 사용할 수 있습니다.

 @see UserController
 */
public interface Controller {
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
