import React, {FormEvent, useState} from 'react';
import {useNavigate} from "react-router-dom";
import {toast} from "react-toastify";

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
            toast.error("login failed.")
        }
    }

    return (
        <div className="Main-Page-Wrapper">
            <h2>Please Login:</h2>
            <form onSubmit={loginOnSubmit} className="form-login-wrapper">
                <label>
                    Username:
                    <input type="text" onChange={event => setUsername(event.target.value)}/>
                </label>
                <label>
                    Password:
                    <input type="password" onChange={event => setPassword(event.target.value)}/>
                </label>
                <button>Login</button>
            </form>

        </div>
    );
}

export default LoginPage;