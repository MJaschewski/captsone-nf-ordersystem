import axios from "axios";
import secureLocalStorage from "react-secure-storage";

export function useHookLogout() {

    function logout() {
        return axios.get("api/userSystem/logout")
            .then(() => secureLocalStorage.removeItem("username"))
            .then(() => secureLocalStorage.removeItem("authorities"))
            .catch(error => console.log(error))
    }

    return {logout};
}