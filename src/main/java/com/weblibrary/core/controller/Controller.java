package com.weblibrary.core.controller;

/**
 * 모든 요청을 받아 처리 하기 위한 web-library 프로젝트의 컨트롤러 최상위 인터페이스
 * 하위에 다른 컨트롤러 인터페이스에서 이 인터페이스를 상속받아 사용할 수 있습니다.
 * 이 인터페이스 자체에는 아무 메서드도 없지만, 이 인터페이스를 다른 컨트롤러 인터페이스가 상속함으로써 adapter에서 다형성을 구현할 수 있습니다.

 @see ForwardController
 */
public interface Controller {
}
