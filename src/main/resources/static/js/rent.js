import {fetchTextRequest, handleError} from "./util.js";

export const rent = async id => {
    try {
        const data = await fetchTextRequest(`/books/${id}/rent`, "POST");
        alert("결과 : " + data);
    } catch (e) {
        handleError(e);
    }
};

export const unRent = async id => {
    try {
        const data = await fetchTextRequest(`/books/${id}/unrent`, "POST");
        alert("결과 : " + data);
    } catch (e) {
        handleError(e);
    }
};