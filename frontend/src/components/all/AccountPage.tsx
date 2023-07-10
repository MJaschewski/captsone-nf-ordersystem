import React, {ChangeEvent, FormEvent, useEffect, useState} from 'react';
import {useNavigate} from "react-router-dom";
import {UserSimpleBody} from "../UserSimpleBodyType";
import secureLocalStorage from "react-secure-storage";
import axios from "axios";
import {toast} from "react-toastify";

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
            .then(response =>
                toast.success(response.data))
            .then(() => setShowPasswordChange(false))
            .catch(error => toast.error(error.message))

    }

    return (
        <div>
            <h1>Manage Account:</h1>
            <div>
                <h2>Your account</h2>
                <p>Username: {ownUser.username} </p>
                <h3>Authorities:</h3>
                <p>{ownUser.authorities.map(auth => <p key={"p" + auth}>{auth}</p>)} </p>
            </div>
            {!showPasswordChange
                ? <button onClick={() => setShowPasswordChange(true)}>Change Password</button>
                : <div className="passwordChange-Wrapper">
                    <h2>Password Change:</h2>
                    <form onSubmit={handleChangePasswordSubmit}>
                        <h4>Old Password:</h4>
                        <label htmlFor="oldPassword">
                            <input type="password" name="oldPassword" value={oldPassword}
                                   onChange={handleOldPasswordChange}/>
                        </label>
                        <h4>New Password:</h4>
                        <label htmlFor="newPassword">
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