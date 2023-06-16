import React, {useEffect} from 'react';
import {useNavigate, useParams} from "react-router-dom";

import OrderOpticalElement from "./OrderOpticalElement";
import useHandleGetOrderById from "./hooks/useHandleGetOrderById";


function OrderDetails() {
    const navigate = useNavigate();
    const {orderBody, handleGetOrderById} = useHandleGetOrderById();
    let {id} = useParams();

    // eslint-disable-next-line
    useEffect(() => handleGetOrderById(id), [])

    return (
        <div>
            {orderBody !== undefined ?
                <OrderOpticalElement orderBody={orderBody}/>
                : <></>}
            <button onClick={() => navigate("/orderHub")}>Cancel</button>
        </div>
    );
}

export default OrderDetails;