import axios from "axios";
import {useState} from "react";
import secureLocalStorage from "react-secure-storage";

export default function useUserHook() {

    const [username, setUsername] = useState("Anonymous User.")

    function login(username: string, password: string) {
        return axios.post("api/userSystem/login", undefined, {auth: {username, password}})
            .then(response => {
                response.status === 200
                    ? setUsername(response.data)
                    : setUsername("Anonymous User.")
            })
            .then(() => secureLocalStorage.setItem("username", username))
            .catch(error => console.log(error))
    }

    return {login, username}
}