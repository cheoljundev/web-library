import {fetchRequest, handleError} from "./util.js";

const addBook = async errorContainer => {
    const bookName = document.getElementById("addBookName").value;
    const isbn = document.getElementById("addBookIsbn").value;
    const coverImage = document.getElementById("addBookCoverImage").files[0]; // 파일 선택
    const body = new FormData();
    body.append("bookName", bookName);
    body.append("isbn", isbn);
    body.append("coverImage", coverImage);

    try {
        const data = await fetchRequest(`/books/add`, "POST", body);
        if (data.redirected) {
            console.log("페이지가 리다이렉트되었습니다.");
            return;
        }
        alert("결과 : " + data.message);
        location.reload();
    } catch (e) {
        handleError(e, errorContainer);
    }
};

const deleteBook = async id => {

    try {
        const data = await fetchRequest(`/books/${id}`, "DELETE");
        if (data.redirected) {
            console.log("페이지가 리다이렉트되었습니다.");
            return;
        }
        alert("결과 : " + data.message);
        location.reload();
    } catch(e) {
        handleError(e);
    }

};

const setModifySection = btn => {
    const idInput = document.getElementById("modifyBookId");
    const bookNameInput = document.getElementById("modifyBookName");
    const isbnInput = document.getElementById("modifyIsbn");

    bookNameInput.value = btn.dataset.name;
    isbnInput.value = btn.dataset.isbn;
    idInput.value = btn.dataset.id;
};

const modifyBook = async errorContainer => {
    const id = document.getElementById("modifyBookId").value;
    const bookName = document.getElementById("modifyBookName").value;
    const isbn = document.getElementById("modifyIsbn").value;

    try {
        const data = await fetchRequest(`/books/${id}`, "PUT", {id, bookName, isbn});
        if (data.redirected) {
            console.log("페이지가 리다이렉트되었습니다.");
            return;
        }
        alert("결과 : " + data.message);
        location.reload();
    } catch (e) {
        handleError(e, errorContainer);
    }
};

window.addBook = addBook;
window.deleteBook = deleteBook;
window.setModifySection = setModifySection;
window.modifyBook = modifyBook;