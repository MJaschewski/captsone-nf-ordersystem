import {useState} from "react";
import {OrderBodyType} from "../OrderBodyType";
import axios from "axios";
import {toast} from "react-toastify";

export default function useHandleGetOwnOrderById() {
    const [orderBody, setOrderBody] = useState<OrderBodyType>()

    function handleGetOwnOrderById(id: string | undefined) {
        if (id !== undefined) {
            axios.get('/api/orderSystem/own/' + id)
                .then(response => response.data)
                .then(data => {
                    setOrderBody(data);
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
    }

    return {orderBody, handleGetOwnOrderById}
}
