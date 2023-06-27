import React, {useEffect} from 'react';
import {useNavigate, useParams} from "react-router-dom";
import useHandleGetOrderById from "../all/OrderSystem/hooks/useHandleGetOrderById";
import ApproveOrderOpticalElement from "./ApproveOrderOpticalElement";


function ApproveOrderDetails() {
    const navigate = useNavigate();
    const {orderBody, handleGetOrderById} = useHandleGetOrderById();
    let {id} = useParams();

    // eslint-disable-next-line
    useEffect(() => handleGetOrderById(id), [])

    return (
        <div>
            {orderBody !== undefined ?
                <ApproveOrderOpticalElement orderBody={orderBody}/>
                : <></>}
            <button className="button-cancel-wrapper" onClick={() => navigate("/orderHub/approval")}>Cancel</button>
        </div>
    );
}

export default ApproveOrderDetails;