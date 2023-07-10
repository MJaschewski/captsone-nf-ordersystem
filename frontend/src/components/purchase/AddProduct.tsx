import React, {ChangeEvent, FormEvent, useState} from 'react';
import axios from "axios";
import {useNavigate} from "react-router-dom";
import {ProductDTO} from "./ProductDTOType";
import FormProductSubmit from "./FormProductSubmit";
import {toast} from "react-toastify";


function AddProduct() {
    const navigate = useNavigate();
    const accessLevel = ["ALL", "PURCHASE", "LEAD"]
    const [productName, setProductName] = useState<string>("")
    const [productPrice, setProductPrice] = useState<number>(0.00)
    const [productAccessLevel, setProductAccessLevel] = useState<string>("")
    const [productImageURL, setProductImageURL] = useState<string>("")

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

    function handleSubmit(event: FormEvent) {
        event.preventDefault()
        const productDTO: ProductDTO = {
            name: productName,
            price: productPrice,
            accessLevel: productAccessLevel,
            imageURL: productImageURL
        }
        axios.post('api/productSystem', productDTO)
            .then(response =>
                toast.success("Product " + response.data.name + " added."))
            .then(() => navigate("/productHub"))
            .catch(error => toast.error(error.message))
    }
    return (
        <div>
            <h1>Add product</h1>
            <FormProductSubmit handleSubmit={handleSubmit}
                               handleChangeProductName={handleChangeProductName}
                               handleChangeProductPrice={handleChangeProductPrice}
                               handleChangeProductAccessLevel={handleChangeProductAccessLevel}
                               handleChangeProductImageURL={handleChangeProductImageURL}
                               productName={productName}
                               productPrice={productPrice}
                               accessLevel={accessLevel}
                               productAccessLevel={productAccessLevel}
                               productImageURL={productImageURL}
                               buttonDescription={"Add Product"}/>
            <button className="button-cancel-wrapper" onClick={() => navigate("/productHub")}>Cancel</button>
        </div>
    );
}

export default AddProduct;