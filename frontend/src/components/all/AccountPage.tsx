import React, {ChangeEvent, FormEvent, useEffect, useState} from 'react';
import {useNavigate} from "react-router-dom";
import {UserSimpleBody} from "../UserSimpleBodyType";
import secureLocalStorage from "react-secure-storage";
import axios from "axios";

type PasswordChangeDTO = {
    oldPassword: string,
    newPassword: string
}

function AccountPage() {
    const navigate = useNavigate();
    const [ownUser, setOwnUser] = useState<UserSimpleBody>({username: "Anonymous User", authorities: []});
    const [showPasswordChange, setShowPasswordChange] = useState(false);
    const [oldPassword, setOldPassword] = useState<string>("");
    const [newPassword, setNewPassword] = useState<string>("");

    // eslint-disable-next-line
    useEffect(handleSetUser, [])

    function handleSetUser() {
        const userName = secureLocalStorage.getItem("username");
        const authorities = JSON.parse(secureLocalStorage.getItem("authorities") as string)
        typeof userName === "string"
            ? setOwnUser({username: userName, authorities: authorities})
            : navigate("/login")

    }

    function handleOldPasswordChange(event: ChangeEvent<HTMLInputElement>) {
        setOldPassword(event.target.value)
    }

    function handleNewPasswordChange(event: ChangeEvent<HTMLInputElement>) {
        setNewPassword(event.target.value)
    }

    function handleChangePasswordSubmit(event: FormEvent) {
        event.preventDefault();
        const passwordChangeDTO: PasswordChangeDTO = {oldPassword: oldPassword, newPassword: newPassword}
        axios.put('/api/userSystem/password', passwordChangeDTO)
            .then(response => console.log(response.data))
            .then(() => setShowPasswordChange(false))
            .catch(error => console.log(error));

    }

    return (
        <div>
            <h2>Manage your account</h2>
            <p>Username: {ownUser.username} </p>
            <p>Authorities: {ownUser.authorities.map(auth => <p key={"p" + auth}>{auth}</p>)} </p>

            {!showPasswordChange
                ? <button onClick={() => setShowPasswordChange(true)}>Change Password?</button>
                : <div className="passwordChange-Wrapper">
                    <h3>Password Change:</h3>
                    <form onSubmit={handleChangePasswordSubmit}>
                        <label htmlFor="oldPassword">
                            Old Password:
                            <input type="password" name="oldPassword" value={oldPassword}
                                   onChange={handleOldPasswordChange}/>
                        </label>
                        <label htmlFor="newPassword">
                            New Password:
                            <input type="password" name="newPassword" value={newPassword}
                                   onChange={handleNewPasswordChange}/>
                        </label>

                        <button className="button-submit-wrapper">Change Password</button>
                    </form>
                    <button className="button-cancel-wrapper" onClick={() => setShowPasswordChange(false)}>Cancel
                    </button>
                </div>

            }
            <button className="button-cancel-wrapper" onClick={() => navigate("/")}>Back</button>
        </div>
    );
}

export default AccountPage;