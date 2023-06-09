import React, {ChangeEvent, FormEvent} from 'react';

type Props = {
    handleSubmit: (event: FormEvent) => void,
    handleChangeProductName: (event: ChangeEvent<HTMLInputElement>) => void,
    handleChangeProductPrice: (event: ChangeEvent<HTMLInputElement>) => void,
    handleChangeProductAccessLevel: (event: ChangeEvent<HTMLInputElement>) => void,
    handleChangeProductImageURL: (event: ChangeEvent<HTMLInputElement>) => void,
    productName: string,
    productPrice: number,
    accessLevel: string[],
    productAccessLevel: string,
    productImageURL: string
    buttonDescription: string
}

function FormProductSubmit(props: Props) {
    return (
        <div>
            <form onSubmit={props.handleSubmit}>
                <h3>Enter Name</h3>
                <label htmlFor="productName">
                    <input type="text" name="productName" value={props.productName}
                           onChange={props.handleChangeProductName}/>
                </label>
                <h3>Enter Price</h3>
                <label htmlFor="productPrice">
                    <input type="number" min="0.00" inputMode="numeric" pattern="\?" step="0.01" name="productPrice"
                           value={props.productPrice}
                           onChange={props.handleChangeProductPrice}/>
                </label>
                <label>
                    <h3>Select Access Level</h3>
                    {props.accessLevel.map((level: React.Key) => (
                        <div key={level}>

                            <input type="radio"
                                   id={level.toString()}
                                   name="productAccessLevel"
                                   value={level.toString()}
                                   onChange={props.handleChangeProductAccessLevel}
                                   checked={props.productAccessLevel.toString() === level.toString()}
                            />
                            <label htmlFor={level.toString()}>{level.toString()}</label>
                        </div>
                    ))}
                </label>
                <h3>Enter Image URL</h3>
                <label htmlFor="productImageURL">
                    <input type="text" name="productImageURL" value={props.productImageURL}
                           onChange={props.handleChangeProductImageURL}/>
                </label>
                <p>
                    <button className="button-submit-wrapper">{props.buttonDescription}</button>
                </p>

            </form>
        </div>
    );
}

export default FormProductSubmit;