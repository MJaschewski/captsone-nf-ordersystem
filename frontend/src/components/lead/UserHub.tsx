import React from 'react';
import {useNavigate} from "react-router-dom";

function UserHub() {
    const navigate = useNavigate();
    return (
        <div>

            <button className="button-cancel-wrapper" onClick={() => navigate("/")}> Cancel</button>
        </div>
    );
}

export default UserHub;