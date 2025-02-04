import {fetchRequest, handleError} from "./util.js";

const addBook = async errorContainer => {
    const bookInfo = {
        bookName : document.getElementById("addBookName").value,
        isbn : document.getElementById("addBookIsbn").value,
    }

    const coverImage = document.getElementById("addBookCoverImage").files[0]; // 파일 선택

    const body = new FormData();

    body.append("bookData", new Blob(
        [JSON.stringify(bookInfo)], {type : "application/json"}
    ));
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
    const coverImage = document.getElementById("modifyBookPreview");
    coverImage.style.display = "block";

    bookNameInput.value = btn.dataset.name;
    isbnInput.value = btn.dataset.isbn;
    idInput.value = btn.dataset.id;
    coverImage.src = btn.dataset.cover;
};

const modifyBook = async errorContainer => {

    const bookInfo = {
        id : document.getElementById("modifyBookId").value,
        bookName : document.getElementById("modifyBookName").value,
        isbn : document.getElementById("modifyIsbn").value,
    }

    const coverImage = document.getElementById("modifyBookCoverImage").files[0]; // 파일 선택
    const body = new FormData();
    body.append("bookData", new Blob(
        [JSON.stringify(bookInfo)], {type : "application/json"}
    ))
    body.append("coverImage", coverImage);

    try {
        const data = await fetchRequest(`/books/${bookInfo.id}`, "PUT", body);
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

const addCoverPreview = () => {
    document.getElementById('addBookCoverImage').addEventListener('change', function(event) {
        const file = event.target.files[0]; // 사용자가 선택한 파일 가져오기
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                const preview = document.getElementById('addBookPreview');
                preview.src = e.target.result; // 미리보기 이미지 설정
                preview.style.display = 'block'; // 이미지 표시
            };
            reader.readAsDataURL(file); // 파일을 DataURL로 변환
        }
    });
}

const modifyCoverPreview = () => {
    document.getElementById('modifyBookCoverImage').addEventListener('change', function(event) {
        const file = event.target.files[0]; // 사용자가 선택한 파일 가져오기
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                const preview = document.getElementById('modifyBookPreview');
                preview.src = e.target.result; // 미리보기 이미지 설정
                preview.style.display = 'block'; // 이미지 표시
            };
            reader.readAsDataURL(file); // 파일을 DataURL로 변환
        }
    });
}

window.addBook = addBook;
window.deleteBook = deleteBook;
window.setModifySection = setModifySection;
window.modifyBook = modifyBook;

addCoverPreview();
modifyCoverPreview();