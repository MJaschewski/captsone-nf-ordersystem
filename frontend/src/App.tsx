import './App.css';
import AddProduct from "./components/purchase/AddProduct";
import ProductHub from "./components/purchase/ProductHub";
import {Route, Routes} from "react-router-dom";
import AddOrder from "./components/all/OrderSystem/AddOrder";
import FrontPage from "./components/all/FrontPage";
import OrderHub from "./components/all/OrderSystem/OrderHub";
import OrderDetails from "./components/all/OrderSystem/OrderDetails";
import EditOrder from "./components/all/OrderSystem/EditOrder";
import LoginPage from "./components/all/LoginPage";
import useHookLogin from "./components/all/hooks/useHookLogin";
import ProtectedRoutesAll from "./components/ProtectedRoutesAll";
import ProtectedRoutesPurchase from "./components/ProtectedRoutesPurchase"
import {useHookLogout} from "./components/all/hooks/useHookLogout";
import DeleteOrder from "./components/all/OrderSystem/DeleteOrder";
import ProtectedRoutesApproval from "./components/ProtectedRoutesApproval";
import ApproveOrder from "./components/approval/ApproveOrder";
import ApproveOrderDetails from "./components/approval/ApproveOrderDetails";
import ProductDetails from "./components/purchase/ProductDetails";

function App() {

    const {login} = useHookLogin();
    const {logout} = useHookLogout();

    return (
        <div className="App">
            <Routes>
                <Route path={"/login"} element={<LoginPage login={login}/>}/>

                <Route element={<ProtectedRoutesAll/>}>
                    <Route path={"/"} element={<FrontPage logout={logout}/>}/>
                    <Route path={"/orderHub"} element={<OrderHub/>}/>
                    <Route path={"/orderHub/details/:id"} element={<OrderDetails/>}/>
                    <Route path={"/orderHub/delete/:id"} element={<DeleteOrder/>}/>
                    <Route path={"/orderHub/edit/:id"} element={<EditOrder/>}/>
                    <Route path={"/add_order"} element={<AddOrder/>}/>
                    <Route element={<ProtectedRoutesApproval/>}>
                        <Route path={"/orderHub/approval"} element={<ApproveOrder/>}/>
                        <Route path={"/orderHub/approval/details/:id"} element={<ApproveOrderDetails/>}/>
                    </Route>
                    <Route element={<ProtectedRoutesPurchase/>}>
                        <Route path={"/productHub"} element={<ProductHub/>}/>
                        <Route path={"/productHub/details/:id"} element={<ProductDetails/>}/>
                        <Route path={"/add_product"} element={<AddProduct/>}/>
                    </Route>
                </Route>
            </Routes>
        </div>
  );
}

export default App;
