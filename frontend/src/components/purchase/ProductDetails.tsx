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
            {productBody !== undefined ?
                <ProductOpticalElement productBody={productBody}/>
                : <></>}
            <button onClick={() => navigate("/productHub")}>Cancel</button>
            <button onClick={() => navigate("/productHub/edit/" + id)}>Edit</button>
        </div>
    );
}

export default ProductDetails;