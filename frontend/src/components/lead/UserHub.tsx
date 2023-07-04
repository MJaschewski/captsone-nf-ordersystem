import React, {useEffect, useState} from 'react';
import {useNavigate} from "react-router-dom";
import axios from "axios";
import {UserSimpleBody} from "../UserSimpleBodyType";


function UserHub() {
    const navigate = useNavigate();
    const [userList, setUserList] = useState<UserSimpleBody[]>([])

    useEffect(handleUserList, [])

    function handleUserList() {
        axios.get("/api/userSystem/users")
            .then(response => response.data)
            .then(data => {
                setUserList(data)
            })
            .catch(error => console.log(error))
    }

    return (
        <div className="Left-Align-Wrapper">
            <h1>User Hub</h1>
            <h2>List of Users:</h2>
            <ul className="userHub-UserList-Wrapper">{userList.map(users => (
                <li key={users.username} className="userList-Element-Wrapper">
                    <p>Username: {users.username}</p>
                    <p>Authorities: </p>
                    <ul> {users.authorities.map(auth => (
                        <li key={auth}>{auth}</li>
                    ))}
                    </ul>
                </li>
            ))}
            </ul>
            <button onClick={() => navigate("/userHub/addUser")}> Add User</button>
            <button className="button-cancel-wrapper" onClick={() => navigate("/")}> Back</button>
        </div>
    );
}

export default UserHub;