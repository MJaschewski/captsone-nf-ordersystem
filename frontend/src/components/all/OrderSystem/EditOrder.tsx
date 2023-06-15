import {NavigateFunction, useParams} from "react-router-dom";
import useHandleValidProductList from "./hooks/useHandleValidProductList";
import useHandleOrderProductList from "./hooks/useHandleOrderProductList";
import {useEffect} from "react";

type Props = {
    navigate: NavigateFunction
}

function EditOrder(props: Props) {
    let {id} = useParams();
    const {validProductList, handleValidProductList} = useHandleValidProductList()
    const {orderProductList, handleOrderProductList} = useHandleOrderProductList()

    useEffect(handleValidProductList, [])

    return (
        <div>

        </div>
    );
}

export default EditOrder;