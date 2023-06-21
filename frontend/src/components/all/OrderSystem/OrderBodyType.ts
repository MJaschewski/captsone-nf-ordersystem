import {ProductBodyType} from "../../purchase/ProductBodyType";

export type OrderBodyType = {
    id: string,
    owner: string,
    productBodyList: ProductBodyType[],
    price: number,
    created: string,
    arrival: string,
    approvalPurchase: boolean,
    approvalLead: boolean,
    orderStatus: string
}