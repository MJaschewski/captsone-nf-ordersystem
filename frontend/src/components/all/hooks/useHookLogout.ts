import axios from "axios";
import secureLocalStorage from "react-secure-storage";

export function useHookLogout() {

    function logout() {
        return axios.get("api/userSystem/logout")
            .then(() => secureLocalStorage.setItem("username", "Anonymous User."))
            .then(() => secureLocalStorage.setItem("authorities", ""))
            .catch(error => console.log(error))
    }

    return {logout};
}