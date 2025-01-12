import {getPrevSelectValue} from "./util.js";

export const setRole = btn => {
    // btn의 이전 요소(select)의 선택값을 가져와야 함
    const roleValue = getPrevSelectValue(btn);
    const id = btn.value;

    if (roleValue == null) {
        return;
    }

    fetch(`/users/${id}/role`, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            roleName: roleValue
        })
    })
        .then((res) => {
            if (!res.ok) {
                return res.json().then(body => {
                    const error = new Error(`HTTP Error! status : ${body.status}, message : ${body.message}`);
                    error.status = res.status;
                    throw error;
                })
            }
            return res.json();
        })
        .then((data) => {
            alert("결과 : " + data.message);
        })
        .catch(error => {
            alert(error.message);
            if (error.status == 403) {
                location.href = "/access-denied";
            }
        });
};

export const deleteUser = id => {
    fetch(`/users/${id}`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
        },
    })
        .then((res) => {
            if (!res.ok) {
                return res.json().then(body => {
                    const error = new Error(`HTTP Error! status : ${body.status}, message : ${body.message}`);
                    error.status = res.status;
                    throw error;
                })
            }
            return res.json();
        })
        .then((data) => {
            alert("결과 : " + data.message);
            location.reload();
        })
        .catch(error => {
            alert(error.message);
            if (error.status == 403) {
                location.href = "/access-denied";
            }
        });
}