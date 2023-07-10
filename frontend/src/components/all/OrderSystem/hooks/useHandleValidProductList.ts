import axios from "axios";
import {useState} from "react";
import {ProductBodyType} from "../../../purchase/ProductBodyType";
import {toast} from "react-toastify";

export default function useHandleValidProductList() {
    const [validProductList, setValidProductList] = useState<ProductBodyType[]>([])

    function handleValidProductList() {
        axios.get('/api/productSystem')
            .then(response => response.data)
            .then(data => {
                setValidProductList(data);
            })
            .catch(error => toast.error(error.message))
    }

    return {validProductList, handleValidProductList}

}