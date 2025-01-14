// 공통 fetch 텍스트 요청 함수
export const fetchTextRequest = async (url, method, body = null) => {
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
        const responseText = await response.text();
        throw new Error(`HTTP Error! status: ${response.status}, message: ${responseText}`);
    }

    return await response.text();
};

// 공통 에러 처리 함수
export const handleError = (error) => {
    alert(error.message);
    if (error.status === 403) {
        location.href = "/access-denied";
    }
};
