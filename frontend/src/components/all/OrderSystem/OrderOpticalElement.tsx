import React from 'react';
import {OrderBodyType} from "./OrderBodyType";

type Props = {
    orderBody:OrderBodyType
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
                    <ul>{props.orderBody.productBodyList.map( currentProduct => (
                           <li> key={currentProduct.id} {currentProduct.name } </li>
                        )
                    )}
                    </ul>
                </li>
            </ul>
        </div>
    );
}

export default OrderOpticalElement;