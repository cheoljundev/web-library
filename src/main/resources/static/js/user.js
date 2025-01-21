import {fetchRequest, handleError} from "./util.js";

export const signout = async () => {
    try {
        const data = await fetchRequest("/signout", "POST");
        alert("결과 : " + data.message);
        location.href = "/";
    } catch (e) {
        handleError(e);
    }
}