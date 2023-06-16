import React, {FormEvent, useEffect} from 'react';
import axios from "axios";
import {useNavigate} from "react-router-dom";
import {OrderDTO} from "./OrderDTO";
import useHandleOrderProductList from "./hooks/useHandleOrderProductList";
import useHandleValidProductList from "./hooks/useHandleValidProductList";

function AddOrder() {
    const navigate = useNavigate();
    const {validProductList, handleValidProductList} = useHandleValidProductList()
    const {orderProductList, handleOrderProductList} = useHandleOrderProductList()

    useEffect(handleValidProductList, [])

    function handleOrderSubmit(event: FormEvent) {
        event.preventDefault()
        const orderDTO: OrderDTO = {productBodyList: orderProductList}
        axios.post('/api/orderSystem', orderDTO)
            .then(response => console.log(response.data))
            .then(() => navigate("/api/orderHub"))
            .catch(error => console.log(error))
    }

    return (

        <div>
            <h1>Add Order</h1>
            <h2>List of Products:</h2>
            <form onSubmit={handleOrderSubmit}>
                <label>
                    {validProductList.map((currentProduct) => (
                        <div key={currentProduct.name}>
                            <input type="checkbox"
                                   id={currentProduct.id}
                                   name="ProductOption"
                                   value={currentProduct.name}
                                   onClick={() => handleOrderProductList(currentProduct)}
                            />
                            <label>{currentProduct.name}</label>
                        </div>
                    ))}
                </label>
                <button>Add Order</button>
            </form>
            <button onClick={() => navigate("api/orderHub")}>Cancel</button>
        </div>
    );
}

export default AddOrder;