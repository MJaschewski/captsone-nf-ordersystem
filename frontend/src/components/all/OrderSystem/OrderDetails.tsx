import React, {useEffect} from 'react';
import {useNavigate, useParams} from "react-router-dom";

import OrderOpticalElement from "./OrderOpticalElement";
import useHandleGetOwnOrderById from "./hooks/useHandleGetOwnOrderById";
import axios from "axios";
import {toast} from "react-toastify";


function OrderDetails() {
    const navigate = useNavigate();
    const {orderBody, handleGetOwnOrderById} = useHandleGetOwnOrderById();
    let {id} = useParams();

    // eslint-disable-next-line
    useEffect(() => handleGetOwnOrderById(id), [])

    function handleSendOrder() {
        axios.put("/api/orderSystem/own/send/" + id)
            .then(response => toast.success(response.data))
            .then(() => handleGetOwnOrderById(id))
            .catch(error => toast.error(error.message))
    }

    return (
        <div className="Left-Align-Wrapper">
            <h1>Order Details:</h1>
            {orderBody !== undefined ?
                <OrderOpticalElement orderBody={orderBody}/>
                : <></>}
            {orderBody?.orderStatus === "APPROVED"
                ? <button className="button-submit-wrapper" onClick={handleSendOrder}> Send Order</button>
                : <></>}
            <button className="button-cancel-wrapper" onClick={() => navigate("/orderHub")}>Cancel</button>
            <button className="button-cancel-wrapper" onClick={() => navigate("/orderHub/delete/" + id)}>Delete</button>
        </div>
    );
}

export default OrderDetails;