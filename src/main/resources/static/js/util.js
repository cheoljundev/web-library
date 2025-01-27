const resetErrorFields = () => {
    const errorFieldErrors = document.querySelectorAll(".field-error");
    errorFieldErrors?.forEach((el) => {
        el.style.display = "none";
    });
}

const handleValidationError = (error, errorContainer) => {
    const errors = error.response.errors;

    resetErrorFields();

    // errors 객체에 해당하는 클래스만 표시
    Object.keys(errors).forEach((field) => {
        const fieldErrorElement = document.querySelector(`#${errorContainer} .field-error.${field}`);
        if (fieldErrorElement) {
            fieldErrorElement.style.display = "block";
            fieldErrorElement.textContent = errors[field]; // 에러 메시지 추가
        }
    });
}

export const fetchRequest = async (url, method, body = null) => {
    resetErrorFields();

    const options = {
        method: method,
        headers: {
            "Content-Type": "application/json",
        },
        redirect : "m"
    };

    if (body) {
        options.body = JSON.stringify(body);
    }

    const response = await fetch(url, options);

    // 페이지가 리다이렉트된 경우 페이지 이동
    const contentType = response.headers.get("Content-Type");
    if (contentType.startsWith("text/html")) {
        location.href = response.url;
    }

    // 리디렉션이 아닌 다른 응답 처리
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
export const handleError = (error, errorContainer) => {
    if (error.status === 403) {
        location.href = "/access-denied"
    } else if (error.status == 400) {
        if (error.response.code == "validation") {
            handleValidationError(error, errorContainer);
        } else {
            alert(error.response.message);
        }
    } else {
        console.log(error.message);
    }
};
