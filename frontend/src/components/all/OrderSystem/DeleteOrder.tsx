import React from 'react';
import {useNavigate, useParams} from "react-router-dom";

import axios from "axios";
import {toast} from "react-toastify";

function DeleteOrder() {
    const navigate = useNavigate();
    let {id} = useParams();

    function handleDeletion() {
        axios.delete('/api/orderSystem/' + id)
            .then(response =>
                toast.success(response.data, {
                    position: "top-right",
                    autoClose: 3000,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    progress: undefined,
                    theme: "light",
                }))
            .then(() => navigate("/orderHub"))
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

    return (
        <div>
            <h2>Do you really want to delete order: {id}?</h2>
            <button className="button-submit-wrapper" onClick={handleDeletion}>Yes</button>
            <button className="button-cancel-wrapper" onClick={() => navigate("/orderHub/details/" + id)}> No</button>
        </div>
    );
}

export default DeleteOrder;