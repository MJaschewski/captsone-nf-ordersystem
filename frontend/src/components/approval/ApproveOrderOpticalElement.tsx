import React from 'react';
import {OrderBodyType} from '../all/OrderSystem/OrderBodyType';
import {useNavigate} from "react-router-dom";

type Props = {
    orderBody: OrderBodyType
}

function ApproveOrderOpticalElement(props: Props) {
    const navigate = useNavigate();
    return (
        <div>
            <h3>OrderId: {props.orderBody.id}</h3>
            <ul>
                <li>Id: {props.orderBody.id}</li>
                <li>Created: {props.orderBody.created}</li>
                <li>Arrival: {props.orderBody.arrival}</li>
                <li>Approval Purchase: {props.orderBody.approvalPurchase.toString()}</li>
                <li>Approval Lead: {props.orderBody.approvalPurchase.toString()}</li>
                <li>Order Status: {props.orderBody.orderStatus} </li>
                <li>Product List:
                    <ul>{props.orderBody.productBodyList.map(currentProduct => (
                            <li key={currentProduct.id}>  {currentProduct.name} </li>
                        )
                    )}
                    </ul>
                </li>
            </ul>
            <button onClick={() => console.log("test")}>Approve Order</button>
        </div>
    );
}

export default ApproveOrderOpticalElement;