import React from 'react';
import {useNavigate} from "react-router-dom";
import secureLocalStorage from "react-secure-storage";

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
        <div className="Main-Page-Wrapper">
            <h1>Order System</h1>
            <button onClick={() => navigate("/orderHub")}>Manage Your Orders</button>
            <button onClick={() => navigate("/account")}>Manage Your Account</button>
            {JSON.parse(secureLocalStorage.getItem("authorities") as string).find((auth: string) => auth === "LEAD")
                ? <button onClick={() => navigate("/userHub")}>Manage Users</button>
                : <></>}
            {JSON.parse(secureLocalStorage.getItem("authorities") as string).find((auth: string) => auth === "PURCHASE")
                ? <button onClick={() => navigate("/productHub")}>Manage Products</button>
                : <></>}
            {JSON.parse(secureLocalStorage.getItem("authorities") as string).find((auth: string) => auth === "PURCHASE" || auth === "LEAD")
                ? <button onClick={() => navigate("/orderHub/approval")}>Approve Orders</button>
                : <></>}
            <button className="button-cancel-wrapper" onClick={handleLogout}>Logout</button>
        </div>
    );
}

export default FrontPage;