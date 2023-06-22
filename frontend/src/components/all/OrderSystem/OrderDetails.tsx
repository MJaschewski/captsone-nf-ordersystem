import React, {useEffect} from 'react';
import {useNavigate, useParams} from "react-router-dom";

import OrderOpticalElement from "./OrderOpticalElement";
import useHandleGetOwnOrderById from "./hooks/useHandleGetOwnOrderById";


function OrderDetails() {
    const navigate = useNavigate();
    const {orderBody, handleGetOwnOrderById} = useHandleGetOwnOrderById();
    let {id} = useParams();

    // eslint-disable-next-line
    useEffect(() => handleGetOwnOrderById(id), [])

    return (
        <div>
            {orderBody !== undefined ?
                <OrderOpticalElement orderBody={orderBody}/>
                : <></>}
            <button onClick={() => navigate("/orderHub")}>Cancel</button>
            <button onClick={() => navigate("/orderHub/delete/" + id)}>Delete Order</button>
        </div>
    );
}

export default OrderDetails;