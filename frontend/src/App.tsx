import './App.css';
import AddProduct from "./components/purchase/AddProduct";
import ProductHub from "./components/purchase/ProductHub";
import {Route, Routes, useNavigate} from "react-router-dom";
import AddOrder from "./components/all/AddOrder";
import FrontPage from "./components/all/FrontPage";

function App() {

    const navigate = useNavigate();

    return (
        <div className="App">
            <Routes>
                <Route path={"/"} element={<FrontPage navigate={navigate}/>}/>
                <Route path={"/add_order"} element={<AddOrder navigate={navigate}/>}/>
                <Route path={"/productHub"} element={<ProductHub navigate={navigate}/>}/>
                <Route path={"/add_product"} element={<AddProduct navigate={navigate}/>}/>
            </Routes>
        </div>
  );
}

export default App;
