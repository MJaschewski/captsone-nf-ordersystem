import React, {useEffect, useState} from 'react';
import axios from "axios";
import {UserSimpleBody} from "../UserSimpleBodyType";
import UserList from "./UserList";
import UserChange from "./UserChange";


function UserHub() {
    const [userList, setUserList] = useState<UserSimpleBody[]>([])
    const [singleUser, setSingleUser] = useState<UserSimpleBody>({username: "", authorities: []});
    const [showChangeUser, setShowChangeUser] = useState(false);

    useEffect(handleUserList, [])

    function handleUserList() {
        axios.get("/api/userSystem/users")
            .then(response => response.data)
            .then(data => {
                setUserList(data)
            })
            .catch(error => console.log(error))
    }

    function handleShowChangeUser(userBody: UserSimpleBody) {
        setSingleUser(userBody)
        handleUserList()
        setShowChangeUser(!showChangeUser)
    }

    return (
        <div>
            {!showChangeUser
                ? <UserList userList={userList} handleShowChangeAuthority={handleShowChangeUser}/>
                : <UserChange user={singleUser} handleShowChangeUser={handleShowChangeUser}/>
            }
        </div>
    );
}

export default UserHub;