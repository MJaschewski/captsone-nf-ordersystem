import React, {useEffect, useState} from 'react';
import axios from "axios";
import {ProductBodyType} from "./ProductBodyType";
import ProductOpticalElement from "./ProductOpticalElement";
import {useNavigate} from "react-router-dom";

function ProductHub() {
    const navigate = useNavigate();
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
            {productList.map(currentProductBody => {
                return <ProductOpticalElement key={currentProductBody.id} productBody={currentProductBody}/>
            })}
            <button onClick={() => navigate("/add_product")}>Add Product</button>
        </div>
    );
}

export default ProductHub;