import React, {FormEvent} from 'react';
import secureLocalStorage from "react-secure-storage";
import {ProductBodyType} from "../../purchase/ProductBodyType";

type Props = {
    validProductList: ProductBodyType[],
    handleOrderSubmit: (event: FormEvent) => void,
    handleOrderProductList: (newProduct: ProductBodyType) => void
    buttonDescription: string
}

function FormProductListSubmit(props: Props) {
    return (
        <div>
            <form onSubmit={props.handleOrderSubmit}>
                <label>
                    {props.validProductList.map((currentProduct) => (
                        (secureLocalStorage.getItem("authorities") !== null) && JSON.parse(secureLocalStorage.getItem("authorities") as string).find((auth: string) => auth === currentProduct.accessLevel)
                            ?
                            <div key={currentProduct.name}>
                                <input type="checkbox"
                                       id={currentProduct.id}
                                       name="ProductOption"
                                       value={currentProduct.name}
                                       onClick={() => props.handleOrderProductList(currentProduct)}
                                />
                                <label>{currentProduct.name}</label>
                                <img className="productImageWrapper" src={currentProduct.imageURL}
                                     alt={"Product Image of" + currentProduct.name}/>
                            </div>
                            : <></>
                    ))}
                </label>
                <button className="button-submit-wrapper">{props.buttonDescription}</button>
            </form>
        </div>
    );
}

export default FormProductListSubmit;