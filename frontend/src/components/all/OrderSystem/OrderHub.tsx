import React, {useEffect, useState} from 'react';
import {NavigateFunction} from "react-router-dom";
import {OrderBodyType} from "./OrderBodyType";
import axios from "axios";

type Props = {
    navigate: NavigateFunction
}
function OrderHub(props:Props) {
    const [orderList,setOrderList] = useState<OrderBodyType[]>([])

    useEffect(handleOrderList,[])

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
            <h1>Manage Your Orders</h1>
            <h2>List of Orders:</h2>
            <ul>{orderList.map( currentOrderBody => (
                    <li key={currentOrderBody.id}>
                        <p>OrderId: {currentOrderBody.id}</p>
                        <p>Created: {currentOrderBody.created}</p>
                        <p>Status: {currentOrderBody.orderStatus}</p>
                        <p>
                            <button onClick={() => props.navigate("/orderHub/details/" + currentOrderBody.id)}>Details
                            </button>
                        </p>
                    </li>
                )
            )}
            </ul>
            <button onClick={() => props.navigate("/add_order")}>Add Order</button>
            <button onClick={() => props.navigate("/")}> Cancel</button>
        </div>
    );
}

export default OrderHub;