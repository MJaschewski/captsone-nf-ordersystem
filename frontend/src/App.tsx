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
import useUserHook from "./components/all/hooks/useUserHook";
import ProtectedRoutesAll from "./components/ProtectedRoutesAll";

function App() {

    const {login, username} = useUserHook();

    return (
        <div className="App">
            <Routes>
                <Route path={"/login"} element={<LoginPage login={login}/>}/>
                <Route element={<ProtectedRoutesAll username={username}/>}>
                    <Route path={"/"} element={<FrontPage/>}/>
                    <Route path={"/orderHub"} element={<OrderHub/>}/>
                    <Route path={"/orderHub/details/:id"} element={<OrderDetails/>}/>
                    <Route path={"/orderHub/edit/:id"} element={<EditOrder/>}/>
                    <Route path={"/add_order"} element={<AddOrder/>}/>
                    <Route path={"/productHub"} element={<ProductHub/>}/>
                    <Route path={"/add_product"} element={<AddProduct/>}/>
                </Route>
            </Routes>
        </div>
  );
}

export default App;
