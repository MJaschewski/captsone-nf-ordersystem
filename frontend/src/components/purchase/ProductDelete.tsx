import React from 'react';
import {useNavigate, useParams} from "react-router-dom";
import axios from "axios";
import {toast} from "react-toastify";

function ProductDelete() {
    const navigate = useNavigate();
    let {id} = useParams();

    function handleDeletion() {
        axios.delete('/api/productSystem/' + id)
            .then(response => toast.success(response.data))
            .then(() => navigate("/productHub"))
            .catch(error => toast.error(error.message))
    }

    return (
        <div>
            <h2>Do you really want to delete product: {id}?</h2>
            <button className="button-submit-wrapper" onClick={handleDeletion}>Yes</button>
            <button className="button-cancel-wrapper" onClick={() => navigate("/productHub/details/" + id)}> No</button>
        </div>
    );
}

export default ProductDelete;