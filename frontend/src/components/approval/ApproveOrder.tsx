import React, {useEffect, useState} from 'react';
import {useNavigate} from "react-router-dom";
import {OrderBodyType} from "../all/OrderSystem/OrderBodyType";
import axios from "axios";

function ApproveOrder() {
    const navigate = useNavigate();
    const [orderList, setOrderList] = useState<OrderBodyType[]>([])

    useEffect(handleOrderList, [])

    function handleOrderList() {
        axios.get('/api/orderSystem')
            .then(response => response.data)
            .then(data => {
                setOrderList(data);
                console.log(data)
            })
            .catch(error => console.log(error))
    }

    return (
        <div>
            <h1>Approve Orders</h1>
            <h2>List of Orders:</h2>
            <ul>{orderList.map(currentOrderBody => (
                    <li key={currentOrderBody.id}>
                        <p>OrderId: {currentOrderBody.id}</p>
                        <p>Owner: {currentOrderBody.owner}</p>
                        <p>Approval Purchase: {currentOrderBody.approvalPurchase.toString()} </p>
                        <p>Approval Lead: {currentOrderBody.approvalLead.toString()} </p>
                        <p>Status: {currentOrderBody.orderStatus}</p>
                        <p>
                            <button onClick={() => navigate("/orderHub/approval/details/" + currentOrderBody.id)}>Details
                            </button>
                        </p>
                    </li>
                )
            )}
            </ul>
            <button onClick={() => navigate("/")}>Cancel</button>
        </div>
    );
}

export default ApproveOrder;