import React, {ChangeEvent, FormEvent, useEffect, useState} from 'react';
import {useNavigate, useParams} from "react-router-dom";
import useHandleGetProductById from "./hooks/useHandleGetProductById";
import {ProductDTO} from "./ProductDTOType";
import axios from "axios";
import ProductOpticalElement from "./ProductOpticalElement";

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
            .then(() => navigate("/"))
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
            <form onSubmit={handleProductSubmit}>
                <label htmlFor="productName">
                    <input type="text" name="productName" value={productName} onChange={handleChangeProductName}/>
                </label>
                <label htmlFor="productPrice">
                    <input type="number" min="0.00" inputMode="numeric" pattern="\?" step="0.01" name="productPrice"
                           value={productPrice}
                           onChange={handleChangeProductPrice}/>
                </label>
                <label>
                    {accessLevel.map((level: React.Key) => (
                        <div key={level}>
                            <input type="radio"
                                   id={level.toString()}
                                   name="productAccessLevel"
                                   value={level.toString()}
                                   onChange={handleChangeProductAccessLevel}
                                   checked={productAccessLevel.toString() === level.toString()}
                            />
                            <label htmlFor={level.toString()}>{level.toString()}</label>
                        </div>
                    ))}
                </label>
                <button>Edit Product</button>
            </form>
            <button onClick={() => navigate("/productHub")}>Cancel</button>
        </div>
    );
}

export default EditProduct;