import axios from "axios";

// Generate By CharGPT
function getCookieValueByName(name: String) {
    let cookieArr = document.cookie.split(";"); // 将cookie字符串分割成一个数组
    for (let i = 0; i < cookieArr.length; i++) {
        let cookiePair = cookieArr[i].split("="); // 解析每个cookie的名值对
        if (name == cookiePair[0].trim()) { // 找到指定的cookie
            return decodeURIComponent(cookiePair[1]); // 返回对应的cookie属性值
        }
    }
    return null; // 如果没有找到对应的cookie，则返回null
}


const fetchUserInfo = (username: String, cb: (info: any) => void) => {
    const apiAddress = '/api/u/' + getCookieValueByName('user_id');
    axios
        .get(apiAddress)
        .then((response) => {
            const userData = response.data;
            const info = {
                avatar_url: userData.avatar_url,
                bio: userData.bio,
                username: userData.username,
                user_id: userData.user_id,
                followers: userData.followers_count,
                following: userData.following_count,
                banner: userData.banner,
                join_date: userData.join_date,
                personal_url: userData.personal_url,
                location: userData.location,
                birth_date: userData.birth
            }
            cb(info);
        })
        .catch((error) => {
            console.error(error);
        });

}

export {
    getCookieValueByName,
    fetchUserInfo
}