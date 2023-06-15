import './App.css';
import AddProduct from "./components/purchase/AddProduct";
import ProductHub from "./components/purchase/ProductHub";
import {Route, Routes, useNavigate} from "react-router-dom";
import AddOrder from "./components/all/OrderSystem/AddOrder";
import FrontPage from "./components/all/FrontPage";
import OrderHub from "./components/all/OrderSystem/OrderHub";
import OrderDetails from "./components/all/OrderSystem/OrderDetails";
import EditOrder from "./components/all/OrderSystem/EditOrder";

function App() {

    const navigate = useNavigate();

    return (
        <div className="App">
            <Routes>
                <Route path={"/"} element={<FrontPage navigate={navigate}/>}/>
                <Route path={"/orderHub"} element={<OrderHub navigate={navigate}/>}/>
                <Route path={"/orderHub/details/:id"} element={<OrderDetails navigate={navigate}/>}/>
                <Route path={"/orderHub/edit/:id"} element={<EditOrder navigate={navigate}/>}/>
                <Route path={"/add_order"} element={<AddOrder navigate={navigate}/>}/>
                <Route path={"/productHub"} element={<ProductHub navigate={navigate}/>}/>
                <Route path={"/add_product"} element={<AddProduct navigate={navigate}/>}/>
            </Routes>
        </div>
  );
}

export default App;
