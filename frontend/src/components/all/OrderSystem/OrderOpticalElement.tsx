import React from 'react';
import {OrderBodyType} from "./OrderBodyType";
import {NavigateFunction} from "react-router-dom";

type Props = {
    navigate: NavigateFunction,
    orderBody: OrderBodyType
}

function OrderOpticalElement(props:Props) {
    return (
        <div>
            <h3>OrderId: {props.orderBody.id}</h3>
            <ul>
                <li>Id: {props.orderBody.id}</li>
                <li>Created: {props.orderBody.created}</li>
                <li>Arrival: {props.orderBody.arrival}</li>
                <li>Approval Purchase: {props.orderBody.approvalPurchase}</li>
                <li>Approval Lead: {props.orderBody.approvalPurchase}</li>
                <li>Order Status: {props.orderBody.orderStatus} </li>
                <li>Product List:
                    <ul>{props.orderBody.productBodyList.map(currentProduct => (
                            <li key={currentProduct.id}>  {currentProduct.name} </li>
                        )
                    )}
                    </ul>
                </li>
            </ul>
            <button onClick={() => props.navigate("/orderHub/edit/" + props.orderBody.id)}>Edit Order</button>
        </div>
    );
}

export default OrderOpticalElement;