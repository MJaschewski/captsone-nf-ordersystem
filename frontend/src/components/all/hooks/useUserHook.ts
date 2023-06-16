import axios from "axios";

export default function useUserHook() {

    function login(username: string, password: string) {
        return axios.post("api/userSystem/login", undefined, {auth: {username, password}})
            .then(response => console.log(response.data))
            .catch(error => console.log(error))
    }

    return {login}
}