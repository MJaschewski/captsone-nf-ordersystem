import {useNavigate, useParams} from "react-router-dom";
import useHandleValidProductList from "./hooks/useHandleValidProductList";
import useHandleOrderProductList from "./hooks/useHandleOrderProductList";
import React, {FormEvent, useEffect} from "react";
import {OrderDTO} from "./OrderDTO";
import axios from "axios";
import useHandleGetOwnOrderById from "./hooks/useHandleGetOwnOrderById";
import FormProductListSubmit from "./FormProductListSubmit";
import {toast} from "react-toastify";

function EditOrder() {
    const navigate = useNavigate();
    let {id} = useParams();
    const {orderBody, handleGetOwnOrderById} = useHandleGetOwnOrderById();
    const {validProductList, handleValidProductList} = useHandleValidProductList()
    const {orderProductList, handleOrderProductList} = useHandleOrderProductList()

    // eslint-disable-next-line
    useEffect(handleUseEffect, [])

    function handleUseEffect() {
        handleValidProductList()
        handleGetOwnOrderById(id)
    }

    function handleOrderSubmit(event: FormEvent) {
        event.preventDefault()
        const orderDTO: OrderDTO = {productBodyList: orderProductList}
        axios.put('/api/orderSystem/' + id, orderDTO)
            .then(response =>
                toast.success("Order: " + response.data.id + " edited.")
            )
            .then(() => navigate("/orderHub"))
            .catch(error => toast.error(error.response.status + ": " + error.response.data))
    }

    return (
        <div className="Left-Align-Wrapper">
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
            <FormProductListSubmit validProductList={validProductList} handleOrderSubmit={handleOrderSubmit}
                                   handleOrderProductList={handleOrderProductList} buttonDescription="Edit Order"/>
            <button className="button-cancel-wrapper" onClick={() => navigate("/orderHub/details/" + id)}>Cancel
            </button>
        </div>
    );
}

export default EditOrder;