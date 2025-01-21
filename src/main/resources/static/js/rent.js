import {fetchRequest, handleError} from "./util.js";

export const rent = async id => {
    try {
        const data = await fetchRequest(`/books/${id}/rent`, "POST");
        alert("결과 : " + data.message);
    } catch (e) {
        handleError(e);
    }
};

export const unRent = async id => {
    try {
        const data = await fetchRequest(`/books/${id}/unrent`, "POST");
        alert("결과 : " + data.message);
    } catch (e) {
        handleError(e);
    }
};