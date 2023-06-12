import './App.css';
import AddProduct from "./components/purchase/AddProduct";
import ProductHub from "./components/purchase/ProductHub";
import {Route, Routes} from "react-router-dom";

function App() {

    return (
        <div className="App">
            <Routes>
                <Route path={"/"} element={<ProductHub/>}/>
                <Route path={"/add_product"} element={<AddProduct/>}/>
            </Routes>
            <AddProduct/>
        </div>
  );
}

export default App;
