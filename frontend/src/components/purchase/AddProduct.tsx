import React, {ChangeEvent, FormEvent, useState} from 'react';
import axios from "axios";
import {useNavigate} from "react-router-dom";
import {ProductDTO} from "./ProductDTOType";
import FormProductSubmit from "../FormProductSubmit";


function AddProduct() {
    const navigate = useNavigate();
    const accessLevel = ["ALL", "PURCHASE", "LEAD"]
    const [productName, setProductName] = useState<string>("")
    const [productPrice, setProductPrice] = useState<number>(0.00)
    const [productAccessLevel, setProductAccessLevel] = useState<string>("")

    function handleChangeProductName(event: ChangeEvent<HTMLInputElement>) {
        setProductName(event.target.value);
    }

    function handleChangeProductPrice(event: ChangeEvent<HTMLInputElement>) {
        setProductPrice(event.target.valueAsNumber);
    }
    function handleChangeProductAccessLevel(event:ChangeEvent<HTMLInputElement>){
        setProductAccessLevel(event.target.value);
    }

    function handleSubmit (event: FormEvent){
        event.preventDefault()
        const productDTO:ProductDTO = {name:productName,price:productPrice,accessLevel:productAccessLevel}
        axios.post('/api/productSystem', productDTO)
            .then(response => {
                console.log(response.data)
            })
            .then(() => navigate("/"))
            .catch(error => console.log(error));
    }
    return (
        <div>
            <h1>Add product</h1>
            <FormProductSubmit handleSubmit={handleSubmit}
                               handleChangeProductName={handleChangeProductName}
                               handleChangeProductPrice={handleChangeProductPrice}
                               handleChangeProductAccessLevel={handleChangeProductAccessLevel}
                               productName={productName}
                               productPrice={productPrice}
                               accessLevel={accessLevel}
                               productAccessLevel={productAccessLevel}
                               buttonDescription={"Add Product"}/>
            <button onClick={() => navigate("/productHub")}>Cancel</button>
        </div>
    );
}

export default AddProduct;