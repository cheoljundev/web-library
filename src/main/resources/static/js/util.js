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
    };

    // body가 FormData일 경우, JSON.stringify를 사용하지 않도록 처리
    if (body) {
        if (body instanceof FormData) {
            options.body = body; // FormData는 그대로 사용
        } else {
            options.body = JSON.stringify(body); // 그 외에는 JSON으로 변환
            options.headers = {
                'Content-Type': 'application/json', // JSON 형식인 경우 Content-Type 설정
            };
        }
    }

    const response = await fetch(url, options);

    // 페이지가 리다이렉트된 경우 페이지 이동
    const contentType = response.headers.get("Content-Type");
    if (contentType && contentType.startsWith("text/html")) {
        location.href = response.url;
        return { redirected: true }; // 리다이렉트를 명시적으로 반환
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
        console.log(error);
    }
};
