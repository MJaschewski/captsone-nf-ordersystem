import React from 'react';
import {useNavigate} from "react-router-dom";

function FrontPage() {
    const navigate = useNavigate();
    return (
        <div>
            <h1>Order System</h1>
            <button onClick={() => navigate("/orderHub")}>Manage Orders</button>
            <button onClick={() => navigate("/productHub")}>Manage Products</button>
        </div>
    );
}

export default FrontPage;