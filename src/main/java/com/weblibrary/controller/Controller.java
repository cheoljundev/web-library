package com.weblibrary.controller;

import java.util.Map;

public interface Controller {
    /**
     * 컨트롤러에서 요청을 처리하는 process 메서드
     *
     * @param paramMap : 파라미터를 담은 Map
     * @param model : model을 담은 Map
     * @return : ModelView 생성할 때 넘겨줄 위한 논리적 주소를 반환합니다.
     */
    String process(Map<String, String> paramMap, Map<String, Object> model);
}
