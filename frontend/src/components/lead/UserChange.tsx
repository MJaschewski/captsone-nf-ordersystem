import React, {FormEvent, useState} from 'react';
import {UserSimpleBody} from "../UserSimpleBodyType";
import axios from "axios";
import {toast} from "react-toastify";

type Props = {
    user: UserSimpleBody
    handleShowChangeUser: (userBody: UserSimpleBody) => void
}

function UserChange(props: Props) {
    const authorities = ["ALL", "PURCHASE", "LEAD"]
    const [newAuthorities, setNewAuthorities] = useState<string[]>(["ALL"]);
    const [showDeleteUser, setShowDeleteUser] = useState(false);
    const [password, setPassword] = useState<string>("")

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

    function handleChangeAuthorities(event: FormEvent) {
        event.preventDefault();
        const userSimpleBody: UserSimpleBody = {username: props.user.username, authorities: newAuthorities}
        axios.put('/api/userSystem/authority', userSimpleBody)
            .then(response =>
                toast.success("User " + response.data.username + " edited."))
            .then(() => props.handleShowChangeUser(userSimpleBody))
            .catch(error => toast.error(error.message))
    }

    function handleDeleteUser(event: FormEvent<HTMLFormElement>) {
        event.preventDefault()
        const passwordDTO = {password: password};
        axios.delete('/api/userSystem/delete/' + props.user.username, {data: passwordDTO})
            .then(response =>
                toast.success(response.data))
            .then(() => props.handleShowChangeUser({username: "", authorities: []}))
            .catch(error => toast.error(error.message))
    }

    return (
        <div className="Left-Align-Wrapper">
            <h1>Change User</h1>

            <h2>User: {props.user.username}</h2>
            <h3>Authorities:</h3>
            <ul>
                {props.user.authorities.map(auth => (
                    <p key={auth}>{auth}</p>
                ))}
            </ul>
            <h2>Change Authorites:</h2>
            <form onSubmit={handleChangeAuthorities}>
                <label>
                    {authorities.map((auth) => (
                        auth === "ALL"
                            ?
                            <label key={auth} className="checkbox-Wrapper">
                                {auth}
                                <input type="checkbox"
                                       name="AccesLevel"
                                       value={auth}
                                       onClick={() => handleNewAuthorities(auth)}
                                       defaultChecked
                                />
                                <span className="checkmark"></span>
                            </label>
                            :
                            <label key={auth} className="checkbox-Wrapper">
                                {auth}
                                <input type="checkbox"
                                       name="AccesLevel"
                                       value={auth}
                                       onClick={() => handleNewAuthorities(auth)}
                                />
                                <span className="checkmark"></span>
                            </label>
                    ))}
                </label>
                <button className="button-submit-wrapper">Change Authorities</button>
            </form>
            {!showDeleteUser
                ? <button className="button-cancel-wrapper" onClick={() => setShowDeleteUser(true)}>Delete User</button>
                : <div className="Left-Align-Wrapper">
                    <h3>Do you really want to delete User {props.user.username}?</h3>
                    <form onSubmit={handleDeleteUser}>
                        <label>
                            <p>Enter Password to Delete User:</p>
                            <input type="password" onChange={event => setPassword(event.target.value)}/>
                        </label>
                        <button className="button-submit-wrapper"> Yes</button>
                    </form>
                    <button className="button-cancel-wrapper" onClick={() => setShowDeleteUser(false)}> No</button>
                </div>
            }
            <button className="button-cancel-wrapper"
                    onClick={() => props.handleShowChangeUser({username: "", authorities: []})}> Back
            </button>
        </div>
    );
}

export default UserChange;