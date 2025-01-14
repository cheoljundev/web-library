export const addBook = () => {
    const bookName = document.getElementById("addBookName").value;
    const isbn = document.getElementById("addBookIsbn").value;

    fetch("/books/add", {
        method : "POST",
        headers : {
            "Content-Type": "application/json",
        },
        body : JSON.stringify({
            bookName,
            isbn
        })
    })
        .then((res) => {
            if (!res.ok) {
                return res.text().then(body => {
                    const error = new Error(`HTTP Error! status : ${res.status}, message : ${body}`);
                    error.status = res.status;
                    throw error;
                })
            }
            return res.text();
        })
        .then((data) => {
            alert("결과 : " + data);
            location.reload();
        })
        .catch(error => {
            alert(error.message);
            if (error.status == 403) {
                location.href = "/access-denied";
            }
        });
};

export const deleteBook = id => {
    fetch(`/books/${id}`, {
        method : "DELETE",
        headers : {
            "Content-Type": "application/json",
        }
    })
        .then((res) => {
            if (!res.ok) {
                return res.text().then(body => {
                    const error = new Error(`HTTP Error! status : ${res.status}, message : ${body}`);
                    error.status = res.status;
                    throw error;
                })
            }
            return res.text();
        })
        .then((data) => {
            alert("결과 : " + data);
            location.reload();
        })
        .catch(error => {
            alert(error.message);
            if (error.status == 403) {
                location.href = "/access-denied";
            }
        });
};

export const setModifySection = btn => {
    const idInput = document.getElementById("bookId");
    const bookNameInput = document.getElementById("bookName");
    const isbnInput = document.getElementById("isbn");

    bookNameInput.value = btn.dataset.name;
    isbnInput.value = btn.dataset.isbn;
    idInput.value = btn.dataset.id;
};

export const modifyBook = () => {
    const id = document.getElementById("bookId").value;
    const bookName = document.getElementById("bookName").value;
    const isbn = document.getElementById("isbn").value;

    fetch(`/books/${id}`, {
        method : "PUT",
        headers : {
            "Content-Type": "application/json",
        },
        body : JSON.stringify({
            bookName,
            isbn
        })
    })
        .then((res) => {
            if (!res.ok) {
                return res.text().then(body => {
                    const error = new Error(`HTTP Error! status : ${res.status}, message : ${body}`);
                    error.status = res.status;
                    throw error;
                })
            }
            return res.text();
        })
        .then((data) => {
            alert("결과 : " + data);
            location.reload();
        })
        .catch(error => {
            alert(error.message);
            if (error.status == 403) {
                location.href = "/access-denied";
            }
        });
};