/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.ISISCapitalist;

import generated.PallierType;
import generated.ProductType;
import generated.ProductsType;
import generated.World;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author romai
 */
public class Services {

    public World readWorldFromXml(String username) {
        InputStream input;
        if (getClass().getClassLoader().getResourceAsStream(username + "-world.xml") != null) {
            input = getClass().getClassLoader().getResourceAsStream(username + "-world.xml");
        } else {
            input = getClass().getClassLoader().getResourceAsStream("world.xml");
        }

        World world = null;
        try {
            JAXBContext cont = JAXBContext.newInstance(World.class);
            Unmarshaller u = cont.createUnmarshaller();
            world = (World) u.unmarshal(input);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return world;
    }

    public void saveWorldToXml(World world, String username) throws JAXBException, FileNotFoundException {
        File file = new File(username + "-world.xml");
        OutputStream output = new FileOutputStream(file);
        JAXBContext cont = JAXBContext.newInstance(World.class);
        Marshaller m = cont.createMarshaller();
        m.marshal(world, output);
    }

    public World getWorld(String username) {
        return readWorldFromXml(username);
    }

    public Boolean updateProduct(String username, ProductType p) throws JAXBException, FileNotFoundException {
        World world = getWorld(username);
        updateScore(world);
        ProductType product = findProductById(world, p.getId());
        if (product == null) {
            return false;
        }

        int qtchange = p.getQuantite() - product.getQuantite();
        if (qtchange > 0) {
            double calculCout = p.getCout() * (1 - Math.pow(p.getCroissance(), qtchange)) / (1 - p.getCroissance());
            world.setMoney(world.getMoney() - p.getCout() * qtchange);
            world.setMoney(world.getMoney() - calculCout);
            product.setQuantite(p.getQuantite());
        } else {
            product.setTimeleft(product.getVitesse());
            world.setLastupdate(System.currentTimeMillis());
        }

        saveWorldToXml(world, username);
        return true;
    }

    public ProductType findProductById(World world, int id) {
        List<ProductType> produits = world.getProducts().getProduct();
        ProductType found_p = null;
        for (ProductType p : produits) {
            if (p.getId() == id) {
                found_p = p;
            }
        }
        return found_p;
    }

    public Boolean updateManager(String username, PallierType m) throws JAXBException, FileNotFoundException {
        // m stands for newmanager
        World world = getWorld(username);
        PallierType manager = findManagerByName(world, m.getName());
        if (manager == null) {
            return false;
        }
        ProductType product = findProductById(world, manager.getIdcible());
        if (product == null) {
            return false;
        }
        product.setManagerUnlocked(true);
        world.setMoney(world.getMoney() - manager.getSeuil());
        saveWorldToXml(world, username);
        return true;

    }

    public PallierType findManagerByName(World world, String name) {
        List<PallierType> managers = world.getManagers().getPallier();
        PallierType found_m = null;
        for (PallierType m : managers) {
            if (m.getName().equals(name)) {
                found_m = m;
            }
        }
        return found_m;
    }

    public void updateScore(World world) {
        long lastUpdate = world.getLastupdate();
        long elapsedTime = System.currentTimeMillis() - lastUpdate;
        List<ProductType> produits = world.getProducts().getProduct();
        for(ProductType p: produits){
            if(p.isManagerUnlocked()){
                if(p.getTimeleft() != 0){
                    double n = Math.floor(elapsedTime / p.getTimeleft());
                    world.setMoney(world.getMoney() + p.getRevenu() * n);             
                }
            }
            else{
                if (p.getTimeleft() != 0 && p.getTimeleft() < elapsedTime){
                    world.setMoney(world.getMoney() + p.getRevenu());                    
                }
            }
        }
        world.setLastupdate(System.currentTimeMillis());
    }
}
