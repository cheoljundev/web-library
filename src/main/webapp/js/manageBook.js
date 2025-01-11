export const deleteBook = id => {
    fetch(`/site/books/${id}`, {
        method : "DELETE",
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
        .then((data) => {
            alert("결과 : " + data.message)
            location.reload();
        })
        .catch(error => alert(error.message));
};