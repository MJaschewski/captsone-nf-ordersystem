import React, {FormEvent, useState} from 'react';
import {UserSimpleBody} from "../UserSimpleBodyType";
import axios from "axios";

type Props = {
    user: UserSimpleBody
    handleShowChangeUser: (userBody: UserSimpleBody) => void
}

function UserChange(props: Props) {
    const accessLevel = ["ALL", "PURCHASE", "LEAD"]
    const [newAuthorities, setNewAuthorities] = useState<string[]>(["ALL"]);

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
            .then(response => console.log(response.data))
            .then(() => props.handleShowChangeUser(userSimpleBody))
            .catch(error => console.log(error))
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
                    {accessLevel.map((currentLevel) => (
                        currentLevel === "ALL"
                            ?
                            <div key={currentLevel}>
                                <input type="checkbox"
                                       name="AccesLevel"
                                       value={currentLevel}
                                       onClick={() => handleNewAuthorities(currentLevel)}
                                       defaultChecked
                                />
                                <label>{currentLevel}</label>
                            </div>
                            :
                            <div key={currentLevel}>
                                <input type="checkbox"
                                       name="AccesLevel"
                                       value={currentLevel}
                                       onClick={() => handleNewAuthorities(currentLevel)}
                                />
                                <label>{currentLevel}</label>
                            </div>
                    ))}
                </label>
                <button className="button-submit-wrapper">Change Authorities</button>
            </form>
            <button className="button-cancel-wrapper"
                    onClick={() => props.handleShowChangeUser({username: "", authorities: []})}> Back
            </button>
        </div>
    );
}

export default UserChange;