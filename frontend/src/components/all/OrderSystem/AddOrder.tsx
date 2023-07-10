import React, {FormEvent, useEffect} from 'react';
import axios from "axios";
import {useNavigate} from "react-router-dom";
import {OrderDTO} from "./OrderDTO";
import useHandleOrderProductList from "./hooks/useHandleOrderProductList";
import useHandleValidProductList from "./hooks/useHandleValidProductList";
import FormProductListSubmit from "./FormProductListSubmit";
import {toast} from "react-toastify";

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
            .then(response =>
                toast.success("Order: " + response.data.id + " added.", {
                    position: "top-right",
                    autoClose: 3000,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    progress: undefined,
                    theme: "light",
                }))
            .then(() => navigate("/orderHub"))
            .catch(error => toast.error(error.message, {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            }))
    }

    return (

        <div className="Left-Align-Wrapper">
            <h1>Add Order</h1>
            <h2>List of Products:</h2>
            <FormProductListSubmit validProductList={validProductList} handleOrderSubmit={handleOrderSubmit}
                                   handleOrderProductList={handleOrderProductList} buttonDescription="Add Order"/>
            <button className="button-cancel-wrapper" onClick={() => navigate("/orderHub")}>Cancel</button>
        </div>
    );
}

export default AddOrder;