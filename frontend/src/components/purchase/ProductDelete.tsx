import React from 'react';
import {useNavigate, useParams} from "react-router-dom";
import axios from "axios";

function ProductDelete() {
    const navigate = useNavigate();
    let {id} = useParams();

    function handleDeletion() {
        axios.delete('/api/productSystem/' + id)
            .then(response => console.log(response.data))
            .then(() => navigate("/productHub"))
            .catch(error => console.log(error))
    }

    return (
        <div>
            <h2>Do you really want to delete product: {id}?</h2>
            <button onClick={handleDeletion}>Yes</button>
            <button onClick={() => navigate("/productHub/details/" + id)}> No</button>
        </div>
    );
}

export default ProductDelete;