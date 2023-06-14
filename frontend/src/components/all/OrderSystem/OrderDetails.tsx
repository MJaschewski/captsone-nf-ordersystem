import React, {useEffect, useState} from 'react';
import {NavigateFunction, useParams} from "react-router-dom";
import axios from "axios";
import {OrderBodyType} from "./OrderBodyType";
import {Simulate} from "react-dom/test-utils";
import error = Simulate.error;
import OrderOpticalElement from "./OrderOpticalElement";

type Props = {
    navigate:NavigateFunction
}

function OrderDetails(props:Props) {
    const [orderBody,setOrderBody] = useState<OrderBodyType>()
    let {id} = useParams();

    useEffect(()=>handleGetById(id),[])

    function handleGetById(id:string|undefined){
        if(id!==undefined){
            axios.get('/api/orderSystem/'+id)
                .then(response => response.data)
                .then(data => {
                    setOrderBody(data);
                })
                .catch(error => console.log(error))
        }
    }

    return (
        <div>
            {orderBody!==undefined?
            <OrderOpticalElement orderBody={orderBody}/>
            :<></>}
            <button onClick={()=>props.navigate("/orderHub")}>Cancel</button>
        </div>
    );
}

export default OrderDetails;