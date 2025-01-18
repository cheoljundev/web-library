import { fetchTextRequest, handleError } from "./util.js";

export const setRole = async btn => {
    const id = btn.value;

    // id가 roleType + id인 select의 선택값을 가지고 온다.
    const roleType = document.getElementById("roleType" + id).value;

    if (roleType == null) {
        return;
    }

    try {
        const data = await fetchTextRequest(`/users/${id}/role`, "PATCH", roleType);
        alert("결과 : " + data);
        location.reload();
    } catch (error) {
        handleError(error);
    }
};

export const deleteUser = async (id) => {
    try {
        const data = await fetchTextRequest(`/users/${id}`, "DELETE");
        alert("결과 : " + data);
        location.reload();
    } catch (error) {
        handleError(error);
    }
};