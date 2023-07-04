import React, {useEffect, useState} from 'react';
import {useNavigate} from "react-router-dom";
import {OrderBodyType} from "./OrderBodyType";
import axios from "axios";

function OrderHub() {
    const navigate = useNavigate();
    const [orderList, setOrderList] = useState<OrderBodyType[]>([])

    useEffect(handleOrderList, [])

    function handleOrderList() {
        axios.get("/api/orderSystem/own")
            .then(response => response.data)
            .then(data => {
                setOrderList(data);
            })
            .catch(error => console.log(error))
    }

    return (
        <div className="Left-Align-Wrapper">
            <h1>Manage Your Orders</h1>
            <h2>List of Orders:</h2>
            <ul className="orderHubProductList-Wrapper">
                {orderList.map(currentOrderBody => (
                        <li key={currentOrderBody.id} className="orderListElement-Wrapper">
                            <p>OrderId: {currentOrderBody.id}</p>
                            <p>Created: {currentOrderBody.created}</p>
                            <p>Status: {currentOrderBody.orderStatus}</p>
                            <p>
                                <button onClick={() => navigate("/orderHub/details/" + currentOrderBody.id)}>Details
                                </button>
                            </p>
                        </li>
                    )
                )}
            </ul>
            <button className="button-submit-wrapper" onClick={() => navigate("/add_order")}>Add Order</button>
            <button className="button-cancel-wrapper" onClick={() => navigate("/")}> Cancel</button>
        </div>
    );
}

export default OrderHub;