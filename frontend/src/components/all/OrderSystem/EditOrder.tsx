import {useNavigate, useParams} from "react-router-dom";
import useHandleValidProductList from "./hooks/useHandleValidProductList";
import useHandleOrderProductList from "./hooks/useHandleOrderProductList";
import React, {FormEvent, useEffect} from "react";
import useHandleGetOrderById from "./hooks/useHandleGetOrderById";
import {OrderDTO} from "./OrderDTO";
import axios from "axios";

function EditOrder() {
    const navigate = useNavigate();
    let {id} = useParams();
    const {orderBody, handleGetOrderById} = useHandleGetOrderById();
    const {validProductList, handleValidProductList} = useHandleValidProductList()
    const {orderProductList, handleOrderProductList} = useHandleOrderProductList()

    useEffect(handleUseEffect, [])

    function handleUseEffect() {
        handleValidProductList()
        handleGetOrderById(id)
    }

    function handleOrderSubmit(event: FormEvent) {
        event.preventDefault()
        const orderDTO: OrderDTO = {productBodyList: orderProductList}
        axios.put('/api/orderSystem/' + id, orderDTO)
            .then(response => console.log(response.data))
            .then(() => navigate("/api/orderHub"))
            .catch(error => console.log(error))
    }

    return (
        <div>
            <h1>Edit Order: {id}</h1>
            <h2>Previous Product List:</h2>
            <ul>
                {orderBody?.productBodyList.map(currentProduct => (
                    <li key={currentProduct.id}>
                        <p>{currentProduct.name}</p>
                    </li>
                ))}
            </ul>
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
                <button>Edit Order</button>
            </form>
            <button onClick={() => navigate("/orderHub/details/" + id)}>Cancel</button>
        </div>
    );
}

export default EditOrder;