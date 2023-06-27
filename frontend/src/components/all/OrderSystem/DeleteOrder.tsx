import React from 'react';
import {useNavigate, useParams} from "react-router-dom";

import axios from "axios";

function DeleteOrder() {
    const navigate = useNavigate();
    let {id} = useParams();

    function handleDeletion() {
        axios.delete('/api/orderSystem/' + id)
            .then(response => console.log(response.data))
            .then(() => navigate("/orderHub"))
            .catch(error => console.log(error))
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