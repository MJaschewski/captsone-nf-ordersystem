import React, {useEffect} from 'react';
import {NavigateFunction, useParams} from "react-router-dom";

import OrderOpticalElement from "./OrderOpticalElement";
import useHandleGetOrderById from "./hooks/useHandleGetOrderById";

type Props = {
    navigate:NavigateFunction
}

function OrderDetails(props:Props) {
    const {orderBody, handleGetOrderById} = useHandleGetOrderById();
    let {id} = useParams();

    useEffect(() => handleGetOrderById(id), [])

    return (
        <div>
            {orderBody!==undefined?
                <OrderOpticalElement navigate={props.navigate} orderBody={orderBody}/>
            :<></>}
            <button onClick={()=>props.navigate("/orderHub")}>Cancel</button>
        </div>
    );
}

export default OrderDetails;