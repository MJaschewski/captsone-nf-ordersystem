import React from 'react';
import {UserSimpleBody} from "../UserSimpleBodyType";
import {useNavigate} from "react-router-dom";

type Props = {
    userList: UserSimpleBody[]
    handleShowChangeAuthority: (users: UserSimpleBody) => void
}

function UserList(props: Props) {
    const navigate = useNavigate();
    return (
        <div className="Left-Align-Wrapper">
            <h1>User Hub</h1>
            <h2>List of Users:</h2>
            <ul className="userHub-UserList-Wrapper">{props.userList.map(users => (
                <li key={users.username} className="userList-Element-Wrapper">
                    <h3>Username: {users.username}</h3>
                    <h4>Authorities: </h4>
                    <ul> {users.authorities.map(auth => (
                        <li key={auth}>{auth}</li>
                    ))}
                    </ul>
                    <button onClick={() => props.handleShowChangeAuthority(users)}>Change User</button>
                </li>
            ))}
            </ul>
            <button onClick={() => navigate("/userHub/addUser")}> Add User</button>
            <button className="button-cancel-wrapper" onClick={() => navigate("/")}> Back</button>
        </div>
    );
}

export default UserList;