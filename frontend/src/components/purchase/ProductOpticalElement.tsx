import React from 'react';
import {ProductBodyType} from "./ProductBodyType";

type Props = {
    productBody: ProductBodyType
}

function ProductOpticalElement(props: Props) {
    return (
        <div>
            <h3>Name: {props.productBody.name}</h3>
            <ul>
                <li>Id: {props.productBody.id}</li>
                <li>name: {props.productBody.name}</li>
                <li>price: {props.productBody.price}</li>
                <li>accessLevel: {props.productBody.accessLevel}</li>
            </ul>
        </div>
    );
}

export default ProductOpticalElement;