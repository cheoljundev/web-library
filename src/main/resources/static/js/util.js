const handleValidationError = error => {
    const errors = error.response.errors;
    const errorFieldErrors = document.querySelectorAll(".field-error");

    // 모든 .field-error 요소를 숨김 처리
    errorFieldErrors.forEach((el) => {
        el.style.display = "none";
    });

    // errors 객체에 해당하는 클래스만 표시
    Object.keys(errors).forEach((field) => {
        const fieldErrorElement = document.querySelector(`.field-error.${field}`);
        if (fieldErrorElement) {
            fieldErrorElement.style.display = "block";
            fieldErrorElement.textContent = errors[field]; // 에러 메시지 추가
        }
    });
}

// 공통 fetch 요청 함수
export const fetchRequest = async (url, method, body = null) => {
    const options = {
        method: method,
        headers: {
            "Content-Type": "application/json",
        },
    };

    if (body) {
        options.body = JSON.stringify(body);
    }

    const response = await fetch(url, options);

    if (!response.ok) {
        const responseJson = await response.json();
        const error = new Error(`HTTP Error! status: ${response.status}, message: ${responseJson}`);
        error.status = response.status;
        error.response = responseJson;
        throw error;
    }

    return await response.json();
};

// 공통 에러 처리 함수
export const handleError = (error) => {
    if (error.status === 403) {
        location.href = "/access-denied"
    } else if (error.status == 400) {
        if (error.response.code == "validation") {
            handleValidationError(error);
        } else {
            alert(error.response.message);
        }
    } else {
        console.log(error.message)
    }
};
