import React, {useEffect, useState} from 'react';
import {NavigateFunction} from "react-router-dom";
import {OrderBodyType} from "./OrderBodyType";
import axios from "axios";
import OrderOpticalElement from "./OrderOpticalElement";
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
            {orderList.map(currentOrderBody => {
                return <OrderOpticalElement key={currentOrderBody.id} orderBody={currentOrderBody}/>
            })}
            <button onClick={() => props.navigate("/add_order")}>Add Order</button>
            <button onClick={() => props.navigate("/")}> Cancel</button>
        </div>
    );
}

export default OrderHub;