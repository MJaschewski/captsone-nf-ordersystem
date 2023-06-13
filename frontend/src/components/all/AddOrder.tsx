import React, {useEffect, useState} from 'react';
import {ProductBodyType} from "../purchase/ProductBodyType";
import axios from "axios";
import ProductOpticalElement from "../purchase/ProductOpticalElement";
import {NavigateFunction} from "react-router-dom";

type Props = {
    navigate: NavigateFunction
}

function AddOrder(props: Props) {

    const [productList, setProductList] = useState<ProductBodyType[]>([])

    useEffect(handleProductList, [])

    function handleProductList() {
        axios.get('/api/productSystem')
            .then(response => response.data)
            .then(data => {
                setProductList(data);
                console.log(data)
            })
            .catch(error => console.log(error))
    }

    return (
        <div>
            <h1>Add Order</h1>
            <h2>List of Products:</h2>
            {productList.map(currentProductBody => {
                return <ProductOpticalElement key={currentProductBody.id} productBody={currentProductBody}/>
            })}
            <form>
                <button>Add Order</button>
            </form>
            <button onClick={() => props.navigate("/")}>Cancel</button>
        </div>
    );
}

export default AddOrder;