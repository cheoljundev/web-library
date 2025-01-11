const rent = id => {
    fetch(`site/books/${id}/rent`, {
        method : "POST",
        headers : {
            "Content-Type": "application/json",
        }
    })
        .then((res) => {
            if (!res.ok) {
                return res.json().then(body => {
                    throw new Error(`HTTP Error! status : ${body.status}, message : ${body.message}`)
                })
            }
            return res.json();
        })
        .then((data) => alert("결과 : " + data.message))
        .catch(error => alert(error.message));
};

const unRent = id => {
    fetch(`site/books/${id}/unrent`, {
        method : "POST",
        headers : {
            "Content-Type": "application/json",
        }
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