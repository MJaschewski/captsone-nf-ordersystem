import axios from "axios";
import secureLocalStorage from "react-secure-storage";


export default function useUserHook() {

    function login(username: string, password: string) {
        return axios.post("api/userSystem/login", undefined, {auth: {username, password}})
            .then(response => {
                response.status === 200
                    ? secureLocalStorage.setItem("username", response.data)
                    : secureLocalStorage.setItem("username", "Anonymous User.")
            })
            .catch(error => {
                    console.log(error)
                    secureLocalStorage.setItem("username", "Anonymous User.")
                }
            )
    }

    return {login}
}