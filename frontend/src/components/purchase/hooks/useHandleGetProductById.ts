import {useState} from "react";
import {ProductBodyType} from "../ProductBodyType";
import axios from "axios";
import {toast} from "react-toastify";

export default function useHandleGetProductById() {
    const [productBody, setProductBody] = useState<ProductBodyType>()

    function handleGetProductById(id: string | undefined) {
        if (id !== undefined) {
            axios.get('/api/productSystem/' + id)
                .then(response => response.data)
                .then(data => {
                    setProductBody(data);
                })
                .catch(error => toast.error(error.response.status + ": " + error.response.data))
        }
    }

    return {productBody, handleGetProductById}
}