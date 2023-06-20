import axios from "axios";
import secureLocalStorage from "react-secure-storage";


export default function useHookLogin() {

    function login(username: string, password: string) {
        return axios.post("api/userSystem/login", undefined, {auth: {username, password}})
            .then(response => {
                if (response.status === 200) {
                    secureLocalStorage.setItem("username", response.data.username)
                    secureLocalStorage.setItem("authorities", JSON.stringify(response.data.authorities))
                } else {
                    secureLocalStorage.setItem("username", "Anonymous User.")
                    secureLocalStorage.setItem("authorities", "None")
                }
            })
            .catch(error => {
                    console.log(error)
                secureLocalStorage.setItem("username", "Anonymous User.")
                secureLocalStorage.setItem("authorities", "None")
                }
            )
    }

    return {login}
}