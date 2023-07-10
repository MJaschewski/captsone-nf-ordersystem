import axios from "axios";
import secureLocalStorage from "react-secure-storage";
import {toast} from "react-toastify";

export function useHookLogout() {

    function logout() {
        return axios.get("api/userSystem/logout")
            .then(() => secureLocalStorage.removeItem("username"))
            .then(() => secureLocalStorage.removeItem("authorities"))
            .then(() => toast.success('Logged out.', {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            }))
            .catch(error => toast.error(error.message, {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            }))
    }

    return {logout};
}