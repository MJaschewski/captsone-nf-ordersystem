import axios from "axios";
import secureLocalStorage from "react-secure-storage";
import {toast} from "react-toastify";

export function useHookLogout() {

    function logout() {
        return axios.get("api/userSystem/logout")
            .then(() => secureLocalStorage.removeItem("username"))
            .then(() => secureLocalStorage.removeItem("authorities"))
            .then(() => toast.success('Logged out.'))
            .catch(error => toast.error(error.message))
    }

    return {logout};
}