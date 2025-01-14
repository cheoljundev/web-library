import {setRole, deleteUser} from "./manageMember.js";
import {addBook, deleteBook, setModifySection, modifyBook} from "./manageBook.js";

/**
* window 전역 객체에 함수를 등록
* module로 js파일을 불러오면 스크립트가 ES 모듈로 동작하여 기본적으로 전역 스코프에 변수를 노출하지 않아서 전역 객체인 window에 등록 필요하다.
*/
window.setRole = setRole;
window.deleteUser = deleteUser;
window.addBook = addBook;
window.deleteBook = deleteBook;
window.setModifySection = setModifySection;
window.modifyBook = modifyBook;