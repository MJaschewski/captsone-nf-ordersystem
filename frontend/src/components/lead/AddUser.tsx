import React, {ChangeEvent, FormEvent, useState} from 'react';
import {useNavigate} from "react-router-dom";
import axios from "axios";
import {toast} from "react-toastify";

type RegisterDTO = {
    username: string,
    password: string,
    authorities: string[]
}

function AddUser() {
    const navigate = useNavigate();
    const accessLevel = ["ALL", "PURCHASE", "LEAD"]
    const [newUsername, setNewUsername] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [newAuthorities, setNewAuthorities] = useState<string[]>(["ALL"]);

    function handleUserSubmit(event: FormEvent) {
        event.preventDefault();
        const registerDTO: RegisterDTO = {username: newUsername, password: newPassword, authorities: newAuthorities};
        axios.post('/api/userSystem/register', registerDTO)
            .then(response =>
                toast.success("User: " + response.data.username + " added."))
            .then(() => navigate("/userHub"))
            .catch(error => toast.error(error.message))
    }

    function handleNewAuthorities(authority: string) {
        const index = newAuthorities.indexOf(authority);
        if (index > -1) {
            newAuthorities.splice(index, 1)
        } else {
            let newAuthorityList = newAuthorities;
            newAuthorityList.push(authority);
            setNewAuthorities(newAuthorityList);
        }
    }

    return (
        <div>
            <h1>Add User</h1>
            <form onSubmit={handleUserSubmit}>
                <h3>Enter Username</h3>
                <label htmlFor="username">
                    <input type="text" name="newUsername" value={newUsername}
                           onChange={(event: ChangeEvent<HTMLInputElement>) => setNewUsername(event.target.value)}/>
                </label>
                <h3>Enter Password</h3>
                <label htmlFor="username">
                    <input type="password" name="newPassword" value={newPassword}
                           onChange={(event: ChangeEvent<HTMLInputElement>) => setNewPassword(event.target.value)}/>
                </label>
                <label>
                    <h3>Select Access Level</h3>
                    {accessLevel.map((currentLevel) => (
                        currentLevel === "ALL"
                            ?
                            <label key={currentLevel} className="checkbox-Wrapper">
                                {currentLevel}
                                <input type="checkbox"
                                       name="AccesLevel"
                                       value={currentLevel}
                                       onClick={() => handleNewAuthorities(currentLevel)}
                                       defaultChecked
                                />
                                <span className="checkmark"></span>
                            </label>
                            :
                            <label key={currentLevel} className="checkbox-Wrapper">
                                {currentLevel}
                                <input type="checkbox"
                                       name="AccesLevel"
                                       value={currentLevel}
                                       onClick={() => handleNewAuthorities(currentLevel)}
                                />
                                <span className="checkmark"></span>
                            </label>
                    ))}
                </label>
                <button className="button-submit-wrapper">Create User</button>
            </form>
            <button className="button-cancel-wrapper" onClick={() => navigate("/userHub")}> Cancel</button>
        </div>
    );
}

export default AddUser;