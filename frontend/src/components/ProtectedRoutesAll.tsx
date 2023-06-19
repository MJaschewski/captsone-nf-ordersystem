import React from 'react';
import {Navigate, Outlet} from "react-router-dom";
import secureLocalStorage from "react-secure-storage";


export default function ProtectedRoutesAll() {

    const authenticated = secureLocalStorage.getItem("username") !== "Anonymous User." && typeof secureLocalStorage.getItem("username") === "string"

    return (
        authenticated ? <Outlet/> : <Navigate to={"/login"}/>
    );
}

