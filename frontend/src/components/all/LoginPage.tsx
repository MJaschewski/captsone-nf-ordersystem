import React, {FormEvent, useState} from 'react';
import {useNavigate} from "react-router-dom";

type Props = {
    login: (username: string, password: string) => Promise<void>
}

function LoginPage(props: Props) {
    const navigate = useNavigate();
    const [username, setUsername] = useState<string>("")
    const [password, setPassword] = useState<string>("")

    async function loginOnSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault()
        try {
            await props.login(username, password);
            navigate("/");
        } catch (error) {
            console.error("Login error", error)
        }
    }

    return (
        <div className="loginPage">
            <h2>Please Login:</h2>
            <form onSubmit={loginOnSubmit}>
                <input type="text" onChange={event => setUsername(event.target.value)}/>
                <input type="password" onChange={event => setPassword(event.target.value)}/>
                <button>Login</button>
            </form>

        </div>
    );
}

export default LoginPage;