import React from 'react';
import {Navigate, Outlet} from "react-router-dom";
import secureLocalStorage from "react-secure-storage";


export default function ProtectedRoutesApproval() {

    const authenticatedUser = secureLocalStorage.getItem("username") !== "Anonymous User."
        && typeof secureLocalStorage.getItem("username") === "string"

    const authenticatedAuthorities = secureLocalStorage.getItem("authorities") !== "None"
        && JSON.parse(secureLocalStorage.getItem("authorities") as string).find((auth: string) => auth === "PURCHASE" || auth === "LEAD")

    const authenticated = authenticatedUser && authenticatedAuthorities
    return (
        authenticated ? <Outlet/> : <Navigate to={"/login"}/>
    );
}

