import React from 'react';
import logo from './logo.svg';
import './App.css';
import { useState, useEffect } from 'react';
import { Services } from "./Services";
import { World, Product } from "./world";
import ProductComponent from './Product';
import { transform } from "./utils";
import ReactDOM from 'react-dom';



function App() {
  const [services, setServices] = useState(new Services(""))
  const [world, setWorld] = useState(new World())
  const [qtmulti, setQtMulti] = useState(1)

  useEffect(() => {

    let services = new Services("test")
    setServices(services)
    services.getWorld().then(response => {
      setWorld(response.data)
    }
    )

  }, [])

  function onProductionDone(p: Product): void {
    let gain = p.revenu * p.quantite;
    console.log(gain)
    addToScore(gain);
    services.putProduct(p);
    addToMoney(gain)
  }

  function addToScore(gain: number) {
    // world.score += gain;
    // transform(world.money)
    // const element = (
    //   <span dangerouslySetInnerHTML={{ __html: transform(world.money) }}></span>
    // )
    // ReactDOM.render(element, document.getElementById("balance"));
    setWorld(world => ({...world, score:world.score + gain}))
  }
  function changeCommutator():void{

    let btn = document.getElementById("btnCommutateur")
    if (btn) {
        if (btn.textContent === "x1") {
            btn.textContent = "x10"
            setQtMulti(10)           
        }
        else if (btn.textContent === "x10"){
            btn.textContent = "x100"
            setQtMulti(100)
        }
        else if (btn.textContent === "x100"){
            btn.textContent = "xMax"
            setQtMulti(-1)     
        }
        else{
            btn.textContent = "x1"
            setQtMulti(1)

            
        }
    }
}

function addToMoney(gain:number){

  setWorld(world => ({...world, money:world.money + gain}))
}
function onProductBuy(cost:number, product:Product):void{
  addToMoney(-cost)
  services.putProduct(product)
}


  console.log(world);
  return (
    <div className="App">
      <header className="App-header">
        <div id="logoMonde"><img src={services.server + world.logo} alt={"logo.png"} /></div>        
      </header>
      <div id="bodyContainer">
      <div id="balance">💸<span ></span>{Math.floor(world.money)}$</div>
      <div id="featuresContainer"><div>Managers</div><div>Angels</div><div>Unlocks</div> <div id="btnCommutateur" onClick={changeCommutator}>x1</div></div>
        <div id="ProductsContainer">
        <div id="firstRow">
            <ProductComponent prod={world.products.product[0]} services={services} onProductionDone={onProductionDone} qtmulti = {qtmulti} worldMoney = {world.money} onProductBuy={onProductBuy} />
            <ProductComponent prod={world.products.product[1]} services={services} onProductionDone={onProductionDone} qtmulti = {qtmulti} worldMoney = {world.money}onProductBuy={onProductBuy}/>
            <ProductComponent prod={world.products.product[2]} services={services} onProductionDone={onProductionDone} qtmulti = {qtmulti} worldMoney = {world.money}onProductBuy={onProductBuy}/>
          </div>
          <div id="secondRow">
            <ProductComponent prod={world.products.product[3]} services={services} onProductionDone={onProductionDone} qtmulti = {qtmulti}worldMoney = {world.money}onProductBuy={onProductBuy}/>
            <ProductComponent prod={world.products.product[4]} services={services} onProductionDone={onProductionDone} qtmulti = {qtmulti}worldMoney = {world.money}onProductBuy={onProductBuy}/>
            <ProductComponent prod={world.products.product[5]} services={services} onProductionDone={onProductionDone} qtmulti = {qtmulti}worldMoney = {world.money}onProductBuy={onProductBuy}/>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
