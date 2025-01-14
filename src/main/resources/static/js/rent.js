const rent = id => {
    fetch(`/books/${id}/rent`, {
        method : "POST",
        headers : {
            "Content-Type": "application/json",
        }
    })
        .then((res) => {
            if (!res.ok) {
                return res.text().then(body => {
                    throw new Error(`HTTP Error! status : ${res.status}, message : ${body}`)
                })
            }
            return res.text();
        })
        .then((data) => alert("결과 : " + data))
        .catch(error => alert(error.message));
};

const unRent = id => {
    fetch(`/books/${id}/unrent`, {
        method : "POST",
        headers : {
            "Content-Type": "application/json",
        }
    })
        .then((res) => {
            if (!res.ok) {
                return res.text().then(body => {
                    throw new Error(`HTTP Error! status : ${res.status}, message : ${body}`)
                })
            }
            return res.text();
        })
        .then((data) => alert("결과 : " + data))
        .catch(error => alert(error.message));
};