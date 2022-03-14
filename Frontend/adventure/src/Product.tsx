import { useEffect, useRef, useState } from "react"
import { Services } from "./Services"
import { Product, World } from "./world"
import ProgressBar from "./ProgressBar"
import './Product.css'

type props = {
    prod: Product
    onProductionDone: (product: Product) => void
    services: Services
    qtmulti: number
    worldMoney: number
    onProductBuy: (cost: number, product:Product) => void
}
export default function ProductComponent({ prod, services, onProductionDone,worldMoney, qtmulti, onProductBuy  }: props) {
    const [progress, setProgress] = useState(0)
    calcMaxCanBuy()
    const [quantite, setQuantite] = useState(prod.quantite)
    const [cost, setCost] = useState(prod.cout)
    const [disable, setDisable] = useState(false);

    function startFabrication() {
        console.log(prod.name)
        if(prod.quantite>=1){
        prod.timeleft = prod.vitesse
        prod.lastupdate = Date.now()
        }
    }

    const savedCallback = useRef(calcScore)
    useEffect(() => savedCallback.current = calcScore)
    useEffect(() => {
        let timer = setInterval(() => savedCallback.current(), 100)
        return function cleanup() {
            if (timer) clearInterval(timer)
        }
    }, [])

    function calcScore() {
        if (prod.timeleft == 0) {
            setProgress(0)
        }
        else {
            let time_since_last_update = Date.now() - prod.lastupdate
            prod.lastupdate = Date.now()
            prod.timeleft -= time_since_last_update

            if (prod.timeleft <= 0) {
                prod.timeleft = 0
                console.log(prod.name + " a été créé")
                prod.progressbarvalue = 0
                onProductionDone(prod)
                calcMaxCanBuy()
                
            }
            else {
                prod.progressbarvalue = ((prod.vitesse - prod.timeleft) / prod.vitesse) * 100

            }
            setProgress(prod.progressbarvalue)
        }
    }
    function calcMaxCanBuy(){

        let money = worldMoney
        let c = prod.croissance
        if (qtmulti == 1){
            qtmulti = 1
        }
        else if (qtmulti == 10){
            qtmulti = 10
        }
        else if (qtmulti == 100){
            qtmulti = 100
        }
        else {
            qtmulti = Math.floor(Math.log(1 - money * (1 - c) / prod.cout) / Math.log(c))
            
        }

    }
    function onBuy():void{
        prod.quantite += qtmulti
        let cost = prodCost(qtmulti)
        setCost(prod.cout)
        onProductBuy(cost, prod)
        prod.cout = prod.cout*Math.pow(prod.croissance, qtmulti)
        setQuantite(prod.quantite)
        
        setCost(prod.cout)

        
    }

    function prodCost(n: number):number{
        return prod.cout * (1 - Math.pow(prod.croissance, n))/ (1 - prod.croissance)

    }
    function canBuy(){
        if(worldMoney < prodCost(qtmulti) || qtmulti==0){
            setDisable(true)
        }
    }
    
    

    return (
        <div className="product">
            <div className="productInfo">
                <img onClick={startFabrication} className="productLogo" src={services.server + prod.logo} alt={prod.logo} />
                <div className="qte">{prod.quantite}</div>
            </div>
            <div className="p2" >
                <div className="progressBar">
                    <ProgressBar transitionDuration={"0.1s"} customLabel={" "} completed={progress} />
                </div>

                <div className="temps">Temps: {prod.vitesse / 1000}s</div>
                <div className="p3">
                    <button className="prix" onClick={onBuy} disabled={worldMoney < prodCost(qtmulti) || qtmulti==0}>x {qtmulti} Prix: {Math.floor(prodCost(qtmulti))}$ </button>
                    <div className="revenu">Revenu: {prod.revenu*prod.quantite}$</div>
                </div>
            </div>
        </div>
    )
}