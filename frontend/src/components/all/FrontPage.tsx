import React from 'react';
import {useNavigate} from "react-router-dom";

type Props = {
    logout: () => void
}

function FrontPage(props: Props) {
    const navigate = useNavigate();

    function handleLogout() {
        props.logout()
        navigate("/login")
    }

    return (
        <div>
            <h1>Order System</h1>
            <button onClick={() => navigate("/orderHub")}>Manage Orders</button>
            <button onClick={() => navigate("/productHub")}>Manage Products</button>
            <button onClick={handleLogout}>Logout</button>
        </div>
    );
}

export default FrontPage;