import {useState} from "react";
import {OrderBodyType} from "../OrderBodyType";
import axios from "axios";
import {toast} from "react-toastify";

export default function useHandleGetOrderById() {
    const [orderBody, setOrderBody] = useState<OrderBodyType>()

    function handleGetOrderById(id: string | undefined) {
        if (id !== undefined) {
            axios.get('/api/orderSystem/' + id)
                .then(response => response.data)
                .then(data => {
                    setOrderBody(data);
                })
                .catch(error => toast.error(error.message))
        }
    }

    return {orderBody, handleGetOrderById}
}
