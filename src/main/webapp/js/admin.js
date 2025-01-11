const getRoleValue = btn => {
  // btn의 이전 형제 요소를 찾습니다
  const select = btn.previousElementSibling;

  // 선택된 값을 가져옵니다
  if (select && select.tagName === "SELECT") {
    return select.value;
  } else {
    return null;
  }
}

const setRole = btn => {
  // btn의 이전 요소(select)의 선택값을 가져와야 함
  const roleValue = getRoleValue(btn);
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

const deleteUser = id => {
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