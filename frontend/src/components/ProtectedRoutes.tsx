import React from 'react';
import {Navigate, Outlet} from "react-router-dom";

type Props = {
    username: string
}

function ProtectedRoutes(props: Props) {

    const authenticated = props.username !== undefined && props.username !== "Anonymous User."

    return (
        authenticated ? <Outlet/> : <Navigate to={"/login"}/>
    );
}

export default ProtectedRoutes;