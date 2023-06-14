import React from 'react';
import {NavigateFunction} from "react-router-dom";

type Props = {
    navigate: NavigateFunction
}

function FrontPage(props: Props) {
    return (
        <div>
            <h1>Order System</h1>
            <button onClick={() => props.navigate("/orderHub")}>Manage Orders</button>
            <button onClick={() => props.navigate("/productHub")}>Manage Products</button>
        </div>
    );
}

export default FrontPage;