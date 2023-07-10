import React, {ChangeEvent, FormEvent, useEffect, useState} from 'react';
import {useNavigate, useParams} from "react-router-dom";
import useHandleGetProductById from "./hooks/useHandleGetProductById";
import {ProductDTO} from "./ProductDTOType";
import axios from "axios";
import ProductOpticalElement from "./ProductOpticalElement";
import FormProductSubmit from "./FormProductSubmit";
import {toast} from "react-toastify";


function EditProduct() {
    const navigate = useNavigate();
    let {id} = useParams();
    const {productBody, handleGetProductById} = useHandleGetProductById();
    const accessLevel = ["ALL", "PURCHASE", "LEAD"]
    const [productName, setProductName] = useState<string>("")
    const [productPrice, setProductPrice] = useState<number>(0.00)
    const [productAccessLevel, setProductAccessLevel] = useState<string>("")
    const [productImageURL, setProductImageURL] = useState<string>("")

    // eslint-disable-next-line
    useEffect(() => handleGetProductById(id), [])

    function handleProductSubmit(event: FormEvent) {
        event.preventDefault()
        const productDTO: ProductDTO = {
            name: productName,
            price: productPrice,
            accessLevel: productAccessLevel,
            imageURL: productImageURL
        }
        axios.put('/api/productSystem/' + id, productDTO)
            .then(response => {
                toast.success("Product " + response.data.name + " edited.")
            })
            .then(() => navigate("/productHub/details/" + id))
            .catch(error => toast.error(error.response.status + ": " + error.response.data))
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

    function handleChangeProductImageURL(event: ChangeEvent<HTMLInputElement>) {
        setProductImageURL(event.target.value);
    }

    return (
        <div>
            <h1>Edit product</h1>
            <h2>Previous:</h2>
            {productBody !== undefined ?
                <ProductOpticalElement productBody={productBody}/>
                : <></>}
            <h2>Edit Product:</h2>
            <FormProductSubmit handleSubmit={handleProductSubmit}
                               handleChangeProductName={handleChangeProductName}
                               handleChangeProductPrice={handleChangeProductPrice}
                               handleChangeProductAccessLevel={handleChangeProductAccessLevel}
                               handleChangeProductImageURL={handleChangeProductImageURL}
                               productName={productName}
                               productPrice={productPrice}
                               accessLevel={accessLevel}
                               productImageURL={productImageURL}
                               productAccessLevel={productAccessLevel}
                               buttonDescription={"Edit Product"}/>
            <button className="button-cancel-wrapper" onClick={() => navigate("/productHub")}>Cancel</button>
        </div>
    );
}

export default EditProduct;