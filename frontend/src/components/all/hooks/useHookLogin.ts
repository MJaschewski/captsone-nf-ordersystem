import axios from "axios";
import secureLocalStorage from "react-secure-storage";
import {toast} from "react-toastify";


export default function useHookLogin() {

    function login(username: string, password: string) {
        return axios.post("api/userSystem/login", undefined, {auth: {username, password}})
            .then(response => {
                if (response.status === 200) {
                    secureLocalStorage.setItem("username", response.data.username)
                    secureLocalStorage.setItem("authorities", JSON.stringify(response.data.authorities))
                    toast.success('Logged in.', {
                        position: "top-right",
                        autoClose: 3000,
                        hideProgressBar: false,
                        closeOnClick: true,
                        pauseOnHover: true,
                        draggable: true,
                        progress: undefined,
                        theme: "light",
                    });
                } else {
                    toast.error(response.status + ": " + response.statusText, {
                        position: "top-right",
                        autoClose: 3000,
                        hideProgressBar: false,
                        closeOnClick: true,
                        pauseOnHover: true,
                        draggable: true,
                        progress: undefined,
                        theme: "light",
                    });
                }
            })
            .catch(error => {
                    toast.error(error.message, {
                        position: "top-right",
                        autoClose: 3000,
                        hideProgressBar: false,
                        closeOnClick: true,
                        pauseOnHover: true,
                        draggable: true,
                        progress: undefined,
                        theme: "light",
                    });
                    secureLocalStorage.setItem("username", "Anonymous User.")
                    secureLocalStorage.setItem("authorities", "None")
                }
            )
    }

    return {login}
}