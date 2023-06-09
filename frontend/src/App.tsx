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
import EditProduct from "./components/purchase/EditProduct";
import ProductDelete from "./components/purchase/ProductDelete";
import UserHub from "./components/lead/UserHub";
import ProtectedRoutesLead from "./components/ProtectedRoutesLead";
import AddUser from "./components/lead/AddUser";
import AccountPage from "./components/all/AccountPage";
import 'react-toastify/dist/ReactToastify.css';
import {ToastContainer} from "react-toastify";
import React from "react";


function App() {

    const {login} = useHookLogin();
    const {logout} = useHookLogout();

    return (
        <div className="App">
            <header className="App-Header">
                <h1>Order Management System</h1>
            </header>
                <Routes>
                    <Route path={"/login"} element={<LoginPage login={login}/>}/>

                    <Route element={<ProtectedRoutesAll/>}>
                        <Route path={"/"} element={<FrontPage logout={logout}/>}/>
                        <Route path={"/account"} element={<AccountPage/>}/>
                        <Route path={"/orderHub"} element={<OrderHub/>}/>
                        <Route path={"/orderHub/details/:id"} element={<OrderDetails/>}/>
                        <Route path={"/orderHub/delete/:id"} element={<DeleteOrder/>}/>
                        <Route path={"/orderHub/edit/:id"} element={<EditOrder/>}/>
                        <Route path={"/add_order"} element={<AddOrder/>}/>
                        <Route element={<ProtectedRoutesApproval/>}>
                            <Route path={"/orderHub/approval"} element={<ApproveOrder/>}/>
                            <Route path={"/orderHub/approval/details/:id"} element={<ApproveOrderDetails/>}/>
                        </Route>
                        <Route element={<ProtectedRoutesLead/>}>
                            <Route path={"/userHub"} element={<UserHub/>}/>
                            <Route path={"/userHub/addUser"} element={<AddUser/>}/>
                        </Route>
                        <Route element={<ProtectedRoutesPurchase/>}>
                            <Route path={"/productHub"} element={<ProductHub/>}/>
                            <Route path={"/productHub/details/:id"} element={<ProductDetails/>}/>
                            <Route path={"/productHub/delete/:id"} element={<ProductDelete/>}/>
                            <Route path={"/productHub/edit/:id"} element={<EditProduct/>}/>
                            <Route path={"/add_product"} element={<AddProduct/>}/>
                        </Route>
                    </Route>
                </Routes>
            <ToastContainer
                position="top-right"
                autoClose={3000}
                hideProgressBar={false}
                newestOnTop={false}
                closeOnClick
                rtl={false}
                pauseOnFocusLoss
                draggable
                pauseOnHover
                theme="light"
            />
            {/* Same as */}
            <ToastContainer/>
        </div>
  );
}

export default App;
