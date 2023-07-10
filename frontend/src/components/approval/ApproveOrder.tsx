import React, {useEffect, useState} from 'react';
import {useNavigate} from "react-router-dom";
import {OrderBodyType} from "../all/OrderSystem/OrderBodyType";
import axios from "axios";
import {toast} from "react-toastify";

function ApproveOrder() {
    const navigate = useNavigate();
    const [orderList, setOrderList] = useState<OrderBodyType[]>([])

    useEffect(handleOrderList, [])

    function handleOrderList() {
        axios.get('/api/orderSystem')
            .then(response => response.data)
            .then(data => {
                setOrderList(data);
            })
            .catch(error => toast.error(error.message, {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            }))
    }

    return (
        <div>
            <h1>Approve Orders</h1>
            <h2>List of Orders:</h2>

            <ul className="orderHubProductList-Wrapper">{orderList.map(currentOrderBody => (
                <li key={currentOrderBody.id} className="orderListElement-Wrapper">
                    <h3>OrderId: {currentOrderBody.id}</h3>
                    <p>Owner: {currentOrderBody.owner}</p>
                    <p>Approval Purchase: {currentOrderBody.approvalPurchase.toString()} </p>
                    <p>Approval Lead: {currentOrderBody.approvalLead.toString()} </p>
                    <p>Status: {currentOrderBody.orderStatus}</p>
                    <p>
                        <button
                            onClick={() => navigate("/orderHub/approval/details/" + currentOrderBody.id)}>Details
                        </button>
                    </p>
                    </li>
                )
            )}
            </ul>
            <button className="button-cancel-wrapper" onClick={() => navigate("/")}>Back</button>
        </div>
    );
}

export default ApproveOrder;