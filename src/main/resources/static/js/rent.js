import {fetchRequest, handleError} from "./util.js";

export const rent = async (id, errorContainer) => {
    try {
        const data = await fetchRequest(`/books/${id}/rent`, "POST");
        if (data.redirected) {
            console.log("페이지가 리다이렉트되었습니다.");
            return;
        }
        alert("결과 : " + data.message);
    } catch (e) {
        handleError(e, errorContainer);
    }
};

export const unRent = async (id, errorContainer) => {
    try {
        const data = await fetchRequest(`/books/${id}/return`, "POST");
        if (data.redirected) {
            console.log("페이지가 리다이렉트되었습니다.");
            return;
        }
        alert("결과 : " + data.message);
    } catch (e) {
        handleError(e, errorContainer);
    }
};