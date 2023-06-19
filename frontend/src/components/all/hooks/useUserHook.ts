import axios from "axios";
import {useState} from "react";
import secureLocalStorage from "react-secure-storage";


export default function useUserHook() {

    const [currentUsername, setCurrentUsername] = useState("Anonymous User.")

    function login(username: string, password: string) {
        return axios.post("api/userSystem/login", undefined, {auth: {username, password}})
            .then(response => {
                response.status === 200
                    ? setCurrentUsername(response.data)
                    : setCurrentUsername("Anonymous User.")
            })
            .then(() => secureLocalStorage.setItem("username", currentUsername))
            .catch(error => console.log(error))
    }

    return {login, setCurrentUsername}
}