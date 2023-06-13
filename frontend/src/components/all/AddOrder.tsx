import React, { FormEvent, useEffect, useState} from 'react';
import {ProductBodyType} from "../purchase/ProductBodyType";
import axios from "axios";
import {NavigateFunction} from "react-router-dom";

type Props = {
    navigate: NavigateFunction
}

type OrderDTO = {
    productBodyList:ProductBodyType[]
}

function AddOrder(props: Props) {

    const [productList,setProductList] = useState<ProductBodyType[]>([])
    const [validProductList, setValidProductList] = useState<ProductBodyType[]>([])

    useEffect(handleProductList, [])

    function handleProductList() {
        axios.get('/api/productSystem')
            .then(response => response.data)
            .then(data => {
                setValidProductList(data);
                console.log(data)
            })
            .catch(error => console.log(error))
    }

    function handleAddProduct(newProduct:ProductBodyType){
         const index = productList.indexOf(newProduct);
        if (index > -1) {
            productList.splice(index, 1)
        } else {
            let newProductList = productList;
            newProductList.push(newProduct);
            setProductList(newProductList);
        }
    }

    function handleSubmit (event: FormEvent){
        event.preventDefault()
        const orderDTO:OrderDTO = {productBodyList:productList}
        axios.post('/api/orderSystem',orderDTO)
            .then(response => console.log(response.data))
            .then(()=>props.navigate("/"))
            .catch(error => console.log(error))
    }


    return (

        <div>
            <h1>Add Order</h1>
            <h2>List of Products:</h2>
            <form onSubmit={handleSubmit}>
                <label>
                    {validProductList.map( (currentProduct) => (
                        <div key={currentProduct.name}>
                            <input type="checkbox"
                                   id={currentProduct.id}
                                   name="ProductOption"
                                   value={currentProduct.name}
                                   onClick={()=>handleAddProduct(currentProduct)}
                            />
                            <label>{currentProduct.name}</label>
                        </div>
                    ) )}
                </label>
                <button>Add Order</button>
            </form>
            <button onClick={() => props.navigate("/")}>Cancel</button>
        </div>
    );
}

export default AddOrder;