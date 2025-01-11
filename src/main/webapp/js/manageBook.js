export const deleteBook = id => {
    fetch(`/site/books/${id}`, {
        method : "DELETE",
        headers : {
            "Content-Type": "application/json",
        }
    })
        .then((res) => {
            if (!res.ok) {
                return res.json().then(body => {
                    throw new Error(`HTTP Error! status : ${body.status}, message : ${body.message}`);
                })
            }
            return res.json();
        })
        .then((data) => {
            alert("결과 : " + data.message)
            location.reload();
        })
        .catch(error => alert(error.message));
};

export const setModifySection = (id, bookName, isbn) => {
    const idInput = document.getElementById("bookId");
    const bookNameInput = document.getElementById("bookName");
    const isbnInput = document.getElementById("isbn");
    bookNameInput.value = bookName;
    isbnInput.value = isbn;
    idInput.value = id;
};

export const modifyBook = () => {
    const id = document.getElementById("bookId").value;
    const bookName = document.getElementById("bookName").value;
    const isbn = document.getElementById("isbn").value;

    fetch(`/site/books/${id}`, {
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
                return res.json().then(body => {
                    throw new Error(`HTTP Error! status : ${body.status}, message : ${body.message}`);
                })
            }
            return res.json();
        })
        .then((data) => {
            alert("결과 : " + data.message)
            location.reload();
        })
        .catch(error => alert(error.message));
};