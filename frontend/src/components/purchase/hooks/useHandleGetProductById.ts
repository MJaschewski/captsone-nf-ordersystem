import {useState} from "react";
import {ProductBodyType} from "../ProductBodyType";
import axios from "axios";

export default function useHandleGetProductById() {
    const [productBody, setProductBody] = useState<ProductBodyType>()

    function handleGetProductById(id: string | undefined) {
        if (id !== undefined) {
            axios.get('/api/productSystem/' + id)
                .then(response => response.data)
                .then(data => {
                    setProductBody(data);
                })
                .catch(error => console.log(error))
        }
    }

    return {productBody, handleGetProductById}
}