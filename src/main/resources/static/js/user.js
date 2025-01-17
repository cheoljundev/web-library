import {fetchTextRequest, handleError} from "./util.js";

export const signout = async () => {
    try {
        const data = await fetchTextRequest("/signout", "POST");
        alert("결과 : " + data);
        location.href = "/";
    } catch (e) {
        handleError(e);
    }
}