import axios from "axios";
import {useState} from "react";
import {ProductBodyType} from "../../../purchase/ProductBodyType";

export default function useHandleValidProductList() {
    const [validProductList, setValidProductList] = useState<ProductBodyType[]>([])

    function handleValidProductList() {
        axios.get('/api/productSystem')
            .then(response => response.data)
            .then(data => {
                setValidProductList(data);
                console.log(data)
            })
            .catch(error => console.log(error))
    }

    return {validProductList, handleValidProductList}

}