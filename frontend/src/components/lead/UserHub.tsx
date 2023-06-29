import React from 'react';
import {useNavigate} from "react-router-dom";

function UserHub() {
    const navigate = useNavigate();
    return (
        <div>
            <button onClick={() => navigate("/userHub/addUser")}> Add User</button>
            <button className="button-cancel-wrapper" onClick={() => navigate("/")}> Cancel</button>
        </div>
    );
}

export default UserHub;