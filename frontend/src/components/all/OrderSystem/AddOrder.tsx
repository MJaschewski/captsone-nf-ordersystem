import React, {FormEvent, useEffect} from 'react';
import axios from "axios";
import {useNavigate} from "react-router-dom";
import {OrderDTO} from "./OrderDTO";
import useHandleOrderProductList from "./hooks/useHandleOrderProductList";
import useHandleValidProductList from "./hooks/useHandleValidProductList";
import secureLocalStorage from "react-secure-storage";

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
            .then(() => navigate("/orderHub"))
            .catch(error => console.log(error))
    }

    return (

        <div>
            <h1>Add Order</h1>
            <h2>List of Products:</h2>
            <form onSubmit={handleOrderSubmit}>
                <label>
                    {validProductList.map((currentProduct) => (
                        (secureLocalStorage.getItem("authorities") !== null) && JSON.parse(secureLocalStorage.getItem("authorities") as string).find((auth: string) => auth === currentProduct.accessLevel)
                            ?
                            <div key={currentProduct.name}>
                                <input type="checkbox"
                                       id={currentProduct.id}
                                       name="ProductOption"
                                       value={currentProduct.name}
                                       onClick={() => handleOrderProductList(currentProduct)}
                                />
                                <label>{currentProduct.name}</label>
                            </div>
                            : <></>
                    ))}
                </label>
                <button>Add Order</button>
            </form>
            <button onClick={() => navigate("/orderHub")}>Cancel</button>
        </div>
    );
}

export default AddOrder;