import {fetchRequest, handleError} from "./util.js";

export const addBook = async errorContainer => {
    const bookName = document.getElementById("addBookName").value;
    const isbn = document.getElementById("addBookIsbn").value;

    try {
        const data = await fetchRequest(`/books/add`, "POST", {bookName, isbn});
        alert("결과 : " + data.message);
        location.reload();
    } catch (e) {
        handleError(e, errorContainer);
    }
};

export const deleteBook = async id => {

    try {
        const data = await fetchRequest(`/books/${id}`, "DELETE");
        alert("결과 : " + data.message);
        location.reload();
    } catch(e) {
        handleError(e);
    }

};

export const setModifySection = btn => {
    const idInput = document.getElementById("modifyBookId");
    const bookNameInput = document.getElementById("modifyBookName");
    const isbnInput = document.getElementById("modifyIsbn");

    bookNameInput.value = btn.dataset.name;
    isbnInput.value = btn.dataset.isbn;
    idInput.value = btn.dataset.id;
};

export const modifyBook = async errorContainer => {
    const id = document.getElementById("modifyBookId").value;
    const bookName = document.getElementById("modifyBookName").value;
    const isbn = document.getElementById("modifyIsbn").value;

    try {
        const data = await fetchRequest(`/books/${id}`, "PUT", {id, bookName, isbn});
        alert("결과 : " + data.message);
        location.reload();
    } catch (e) {
        handleError(e, errorContainer);
    }
};