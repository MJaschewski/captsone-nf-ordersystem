import {useState} from "react";
import {ProductBodyType} from "../../../purchase/ProductBodyType";

export default function useHandleOrderProductList() {
    const [orderProductList, setOrderProductList] = useState<ProductBodyType[]>([])


    function handleOrderProductList(newProduct: ProductBodyType) {
        const index = orderProductList.indexOf(newProduct);
        if (index > -1) {
            orderProductList.splice(index, 1)
        } else {
            let newProductList = orderProductList;
            newProductList.push(newProduct);
            setOrderProductList(newProductList);
        }
    }

    return {orderProductList, handleOrderProductList}
}