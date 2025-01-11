import {getPrevSelectValue} from "./util.js";

export const setRole = btn => {
  // btn의 이전 요소(select)의 선택값을 가져와야 함
  const roleValue = getPrevSelectValue(btn);
  const id = btn.value;

  if (roleValue == null) {
    return;
  }

  fetch(`/site/users/${id}/role`, {
    method : "PATCH",
    headers : {
      "Content-Type": "application/json",
    },
    body : JSON.stringify({
      roleName : roleValue
    })
  })
      .then((res) => {
        if (!res.ok) {
          return res.json().then(body => {
            throw new Error(`HTTP Error! status : ${body.status}, message : ${body.message}`);
          })
        }
        return res.json();
      })
      .then((data) => alert("결과 : " + data.message))
      .catch(error => alert(error.message));
};

export const deleteUser = id => {
  fetch(`/site/users/${id}`, {
    method : "DELETE",
    headers : {
      "Content-Type": "application/json",
    },
  })
      .then((res) => {
        if (!res.ok) {
          return res.json().then(body => {
            throw new Error(`HTTP Error! status : ${body.status}, message : ${body.message}`);
          })
        }
        return res.json();
      })
      .then((data) => alert("결과 : " + data.message))
      .catch(error => alert(error.message));
}