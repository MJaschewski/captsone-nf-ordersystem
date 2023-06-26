import React, {FormEvent, useEffect} from 'react';
import axios from "axios";
import {useNavigate} from "react-router-dom";
import {OrderDTO} from "./OrderDTO";
import useHandleOrderProductList from "./hooks/useHandleOrderProductList";
import useHandleValidProductList from "./hooks/useHandleValidProductList";
import FormProductListSubmit from "./FormProductListSubmit";

function AddOrder() {
    const navigate = useNavigate();
    const {validProductList, handleValidProductList} = useHandleValidProductList()
    const {orderProductList, handleOrderProductList} = useHandleOrderProductList()

    // eslint-disable-next-line
    useEffect(handleValidProductList, [])

    function handleOrderSubmit(event: FormEvent) {
        event.preventDefault()
        const orderDTO: OrderDTO = {productBodyList: orderProductList}
        axios.post('/api/orderSystem', orderDTO)
            .then(response => console.log(response.data))
            .then(() => navigate("/orderHub"))
            .catch(error => console.log(error))
    }

    return (

        <div>
            <h1>Add Order</h1>
            <h2>List of Products:</h2>
            <FormProductListSubmit validProductList={validProductList} handleOrderSubmit={handleOrderSubmit}
                                   handleOrderProductList={handleOrderProductList}/>
            <button onClick={() => navigate("/orderHub")}>Cancel</button>
        </div>
    );
}

export default AddOrder;