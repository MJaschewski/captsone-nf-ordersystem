import React, {useEffect} from 'react';
import {useNavigate, useParams} from "react-router-dom";
import useHandleGetProductById from "./hooks/useHandleGetProductById";
import ProductOpticalElement from "./ProductOpticalElement";

function ProductDetails() {
    const navigate = useNavigate();
    const {productBody, handleGetProductById} = useHandleGetProductById();
    let {id} = useParams();

    // eslint-disable-next-line
    useEffect(() => handleGetProductById(id), [])

    return (
        <div>
            <h1>Product Details:</h1>
            {productBody !== undefined ?
                <ProductOpticalElement productBody={productBody}/>
                : <></>}
            <button className="button-cancel-wrapper" onClick={() => navigate("/productHub")}>Cancel</button>
            <button className="button-submit-wrapper" onClick={() => navigate("/productHub/edit/" + id)}>Edit</button>
            <button className="button-cancel-wrapper" onClick={() => navigate("/productHub/delete/" + id)}>Delete
            </button>
        </div>
    );
}

export default ProductDetails;