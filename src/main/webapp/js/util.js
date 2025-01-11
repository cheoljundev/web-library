export const getPrevSelectValue = btn => {
    // btn의 이전 형제 요소를 찾습니다
    const select = btn.previousElementSibling;

    // 선택된 값을 가져옵니다
    if (select && select.tagName === "SELECT") {
        return select.value;
    } else {
        return null;
    }
}

