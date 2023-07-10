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
                console.log(data)
            })
            .catch(error => toast.error(error.message, {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            }))
    }

    return {validProductList, handleValidProductList}

}