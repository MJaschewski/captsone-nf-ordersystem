import axios from "axios";
import {useState} from "react";

export default function useUserHook() {

    const [username, setUsername] = useState("")

    function login(username: string, password: string) {
        return axios.post("api/userSystem/login", undefined, {auth: {username, password}})
            .then(response => setUsername(response.data))
            .catch(error => console.log(error))
    }

    return {login, username}
}