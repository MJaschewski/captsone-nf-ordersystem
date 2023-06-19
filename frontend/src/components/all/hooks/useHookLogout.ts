import axios from "axios";
import secureLocalStorage from "react-secure-storage";

export function useHookLogout(setUsername: (username: string) => void) {

    function logout() {
        return axios.get("api/userSystem/logout")
            .then(() => secureLocalStorage.setItem("username", "Anonymous User."))
            .then(() => setUsername("Anonymous User."))
            .catch(error => console.log(error))
    }

    return {logout};
}