import React, {FormEvent, useState} from 'react';
import {useNavigate} from "react-router-dom";

type Props = {
    login: (username: string, password: string) => Promise<void>
}

function LoginPage(props: Props) {
    const navigate = useNavigate();
    const [username, setUsername] = useState<string>("")
    const [password, setPassword] = useState<string>("")

    function loginOnSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault()
        props.login(username, password).then(() => navigate("/"))
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