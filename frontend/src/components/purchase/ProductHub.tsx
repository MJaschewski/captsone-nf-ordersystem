import React, {useEffect, useState} from 'react';
import axios from "axios";
import {ProductBodyType} from "./ProductBodyType";
import ProductOpticalElement from "./ProductOpticalElement";
import {NavigateFunction} from "react-router-dom";

type Props = {
    navigate: NavigateFunction
}

function ProductHub(props: Props) {
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
            <h1>Manage Products</h1>
            <h2>List of Products:</h2>
            {productList.map(currentProductBody => {
                return <ProductOpticalElement key={currentProductBody.id} productBody={currentProductBody}/>
            })}
            <button onClick={() => props.navigate("/add_product")}>Add Product</button>
            <button onClick={() => props.navigate("/")}> Cancel</button>
        </div>
    );
}

export default ProductHub;