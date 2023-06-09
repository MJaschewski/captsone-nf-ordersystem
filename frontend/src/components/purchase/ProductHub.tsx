import React, {useEffect, useState} from 'react';
import axios from "axios";
import {ProductBodyType} from "./ProductBodyType";
import ProductOpticalElement from "./ProductOpticalElement";
import {useNavigate} from "react-router-dom";
import {toast} from "react-toastify";


function ProductHub() {
    const navigate = useNavigate();
    const [productList, setProductList] = useState<ProductBodyType[]>([])

    useEffect(handleProductList, [])

    function handleProductList() {
        axios.get('/api/productSystem')
            .then(response => response.data)
            .then(data => {
                setProductList(data);
            })
            .catch(error => toast.error(error.response.status + ": " + error.response.data))
    }

    return (
        <div>
            <h1>Manage Products</h1>
            <h2>List of Products:</h2>
            <div className="productHubProductList-Wrapper">
                {productList.map(currentProductBody => {
                    return <div key={currentProductBody.id + "Wrapper"} className="productBody-Wrapper">
                        <ProductOpticalElement key={currentProductBody.id} productBody={currentProductBody}/>
                        <button onClick={() => navigate("/productHub/details/" + currentProductBody.id)}>Details
                        </button>
                    </div>
                })}
            </div>
            <button className="button-submit-wrapper" onClick={() => navigate("/add_product")}>Add Product</button>
            <button className="button-cancel-wrapper" onClick={() => navigate("/")}> Back</button>
        </div>
    );
}

export default ProductHub;