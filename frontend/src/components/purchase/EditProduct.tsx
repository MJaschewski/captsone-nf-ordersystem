import React, {ChangeEvent, FormEvent, useEffect, useState} from 'react';
import {useNavigate, useParams} from "react-router-dom";
import useHandleGetProductById from "./hooks/useHandleGetProductById";
import {ProductDTO} from "./ProductDTOType";
import axios from "axios";
import ProductOpticalElement from "./ProductOpticalElement";
import FormProductSubmit from "../FormProductSubmit";

function EditProduct() {
    const navigate = useNavigate();
    let {id} = useParams();
    const {productBody, handleGetProductById} = useHandleGetProductById();
    const accessLevel = ["ALL", "PURCHASE", "LEAD"]
    const [productName, setProductName] = useState<string>("")
    const [productPrice, setProductPrice] = useState<number>(0.00)
    const [productAccessLevel, setProductAccessLevel] = useState<string>("")

    // eslint-disable-next-line
    useEffect(() => handleGetProductById(id), [])

    function handleProductSubmit(event: FormEvent) {
        event.preventDefault()
        const productDTO: ProductDTO = {name: productName, price: productPrice, accessLevel: productAccessLevel}
        axios.put('/api/productSystem/' + id, productDTO)
            .then(response => {
                console.log(response.data)
            })
            .then(() => navigate("/productHub/details/" + id))
            .catch(error => console.log(error));
    }

    function handleChangeProductName(event: ChangeEvent<HTMLInputElement>) {
        setProductName(event.target.value);
    }

    function handleChangeProductPrice(event: ChangeEvent<HTMLInputElement>) {
        setProductPrice(event.target.valueAsNumber);
    }

    function handleChangeProductAccessLevel(event: ChangeEvent<HTMLInputElement>) {
        setProductAccessLevel(event.target.value);
    }

    return (
        <div>
            <h1>Edit product: {productBody?.id}</h1>
            <h2>Previous:</h2>
            {productBody !== undefined ?
                <ProductOpticalElement productBody={productBody}/>
                : <></>}
            <h2>Edit Product:</h2>
            <FormProductSubmit handleSubmit={handleProductSubmit}
                               handleChangeProductName={handleChangeProductName}
                               handleChangeProductPrice={handleChangeProductPrice}
                               handleChangeProductAccessLevel={handleChangeProductAccessLevel}
                               productName={productName}
                               productPrice={productPrice}
                               accessLevel={accessLevel}
                               productAccessLevel={productAccessLevel}
                               buttonDescription={"Edit Product"}/>
            <button onClick={() => navigate("/productHub")}>Cancel</button>
        </div>
    );
}

export default EditProduct;