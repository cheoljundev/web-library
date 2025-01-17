import {fetchTextRequest, handleError} from "./util.js";

export const addBook = async () => {
    const bookName = document.getElementById("addBookName").value;
    const isbn = document.getElementById("addBookIsbn").value;

    try {
        const data = await fetchTextRequest(`/books/add`, "POST", {bookName, isbn});
        alert("결과 : " + data);
        location.reload();
    } catch (e) {
        handleError(e);
    }
};

export const deleteBook = async id => {

    try {
        const data = await fetchTextRequest(`/books/${id}`, "DELETE");
        alert("결과 : " + data);
        location.reload();
    } catch(e) {
        handleError(e);
    }

};

export const setModifySection = btn => {
    const idInput = document.getElementById("bookId");
    const bookNameInput = document.getElementById("bookName");
    const isbnInput = document.getElementById("isbn");

    bookNameInput.value = btn.dataset.name;
    isbnInput.value = btn.dataset.isbn;
    idInput.value = btn.dataset.id;
};

export const modifyBook = async () => {
    const id = document.getElementById("bookId").value;
    const bookName = document.getElementById("bookName").value;
    const isbn = document.getElementById("isbn").value;

    try {
        const data = await fetchTextRequest(`/books/${id}`, "PUT", {bookName, isbn});
        alert("결과 : " + data);
        location.reload();
    } catch (e) {
        handleError(e);
    }
};