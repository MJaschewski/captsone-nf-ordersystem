import React, {ChangeEvent, FormEvent, useState} from 'react';
import axios from "axios";
import {useNavigate} from "react-router-dom";

type ProductDTO = {
    name:string,
    price:number,
    accessLevel:string
}
function AddProduct() {
    const navigate = useNavigate();
    const accessLevel = ["ALL", "PURCHASE", "LEAD"]
    const [productName, setProductName] = useState<string>("")
    const [productPrice,setProductPrice]= useState<number>(0.00)
    const [productAccessLevel,setProductAccessLevel]= useState<string>("")

    function handleChangeProductName(event:ChangeEvent<HTMLInputElement>){
        setProductName(event.target.value);
    }
    function handleChangeProductPrice(event:ChangeEvent<HTMLInputElement>){
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
            <form onSubmit={handleSubmit}>
                <label htmlFor="productName">
                    <input type="text" name="productName" value={productName} onChange={handleChangeProductName}/>
                </label>
                <label htmlFor="productPrice">
                    <input type="number" min="0" inputMode="numeric" pattern="\?" name="productPrice"
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
                <button>Add Product</button>
            </form>
            <button onClick={() => navigate("/")}>Cancel</button>
        </div>
    );
}

export default AddProduct;