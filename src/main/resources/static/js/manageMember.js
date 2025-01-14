export const setRole = btn => {
    const id = btn.value;

    // id가 roleType + id인 select의 선택값을 가지고 온다.
    const roleValue = document.getElementById("roleType" + id).value;

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
                return res.text().then(body => {
                    const error = new Error(`HTTP Error! status : ${res.status}, message : ${body}`);
                    error.status = res.status;
                    throw error;
                })
            }
            return res.text();
        })
        .then((data) => {
            alert("결과 : " + data);
            location.reload();
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
                return res.text().then(body => {
                    const error = new Error(`HTTP Error! status : ${res.status}, message : ${body}`);
                    error.status = res.status;
                    throw error;
                })
            }
            return res.text();
        })
        .then((data) => {
            alert("결과 : " + data);
            location.reload();
        })
        .catch(error => {
            alert(error.message);
            if (error.status == 403) {
                location.href = "/access-denied";
            }
        });
}