import React, {ChangeEvent, FormEvent} from 'react';

type Props = {
    handleSubmit: (event: FormEvent) => void,
    handleChangeProductName: (event: ChangeEvent<HTMLInputElement>) => void,
    handleChangeProductPrice: (event: ChangeEvent<HTMLInputElement>) => void,
    handleChangeProductAccessLevel: (event: ChangeEvent<HTMLInputElement>) => void,
    productName: string,
    productPrice: number,
    accessLevel: string[],
    productAccessLevel: string,
    buttonDescription: string
}

function FormProductSubmit(props: Props) {
    return (
        <div>
            <form onSubmit={props.handleSubmit}>
                <label htmlFor="productName">
                    <input type="text" name="productName" value={props.productName}
                           onChange={props.handleChangeProductName}/>
                </label>
                <label htmlFor="productPrice">
                    <input type="number" min="0.00" inputMode="numeric" pattern="\?" step="0.01" name="productPrice"
                           value={props.productPrice}
                           onChange={props.handleChangeProductPrice}/>
                </label>
                <label>
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
                <button>{props.buttonDescription}</button>
            </form>
        </div>
    );
}

export default FormProductSubmit;