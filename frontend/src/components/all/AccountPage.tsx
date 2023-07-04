import React, {useEffect, useState} from 'react';
import {useNavigate} from "react-router-dom";
import {UserSimpleBody} from "../UserSimpleBodyType";
import secureLocalStorage from "react-secure-storage";

function AccountPage() {
    const navigate = useNavigate();
    const [ownUser, setOwnUser] = useState<UserSimpleBody>({username: "Anonymous User", authorities: []});
    const [showPasswordChange, setShowPasswordChange] = useState(false);

    useEffect(handleSetUser, [])

    function handleSetUser() {
        const userName = secureLocalStorage.getItem("username");
        const authorities = JSON.parse(secureLocalStorage.getItem("authorities") as string)
        typeof userName === "string"
            ? setOwnUser({username: userName, authorities: authorities})
            : navigate("/login")

    }

    return (
        <div>
            <h2>Manage your account</h2>
            <p>Username: {ownUser.username} </p>
            <p>Authorities: {ownUser.authorities.map(auth => <p key={"p" + auth}>{auth}</p>)} </p>

            {!showPasswordChange
                ? <button onClick={() => setShowPasswordChange(true)}>Change Password?</button>
                : <div className="passwordChange-Wrapper">
                    <form>
                        <button className="button-submit-wrapper">Change Password</button>
                    </form>
                    <button onClick={() => setShowPasswordChange(false)}>Cancel</button>
                </div>

            }
            <button className="button-cancel-wrapper" onClick={() => navigate("/")}>Back</button>
        </div>
    );
}

export default AccountPage;