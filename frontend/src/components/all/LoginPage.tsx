import React, {FormEvent, useState} from 'react';
import axios from "axios";
import {useNavigate} from "react-router-dom";

function LoginPage() {
    const navigate = useNavigate();
    const [username, setUsername] = useState<string>("")
    const [password, setPassword] = useState<string>("")

    function loginOnSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault()
        axios.post("api/userSystem/login", undefined, {auth: {username, password}})
            .then(response => console.log(response.data))
            .then(() => navigate("/"))
            .catch(error => console.log(error))
    }

    return (
        <div>
            <form onSubmit={loginOnSubmit}>
                <input type="text" onChange={event => setUsername(event.target.value)}/>
                <input type="password" onChange={event => setPassword(event.target.value)}/>
                <button>Login</button>
            </form>

        </div>
    );
}

export default LoginPage;