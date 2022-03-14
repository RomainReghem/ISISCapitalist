/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.ISISCapitalist;

import generated.PallierType;
import generated.ProductType;
import generated.World;
import java.io.FileNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import static org.glassfish.jersey.internal.inject.Bindings.service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author romai
 */
@RestController
@RequestMapping("/adventureisis/generic")
@CrossOrigin
public class Webservice {
    Services services;
    public Webservice(){
        services = new Services();
    }
    
    
    @GetMapping(value = "/world", produces={"application/json"})
    public ResponseEntity<World> getWorld(@RequestHeader(value = "X-User", required = false) String username){
        World world = services.getWorld(username);
        return ResponseEntity.ok(world);
    }
    
    @PutMapping(value = "/product", consumes = {"application/json"})
    public ProductType putProduct(@RequestHeader(value = "X-User", required = false) String username, @RequestBody ProductType p) throws JAXBException, FileNotFoundException{
    
        Boolean isProductUpdated = services.updateProduct(username, p);
        if(isProductUpdated){
            return p;
        }
        else{return null;}
    }
    
    public PallierType putManager(@RequestHeader(value = "X-User", required = false) String username, @RequestBody PallierType p) throws JAXBException, FileNotFoundException{
        Boolean isProductUpdated = services.updateManager(username, p);
        if(isProductUpdated){
            return p;
        }
        else{return null;}
    }
    
    
    /*
    @GET
    @Path("world")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<World> getWorld(@Context HttpServletRequest request){
        String username = request.getHeader("X-User");
        World world = services.getWorld(username);
        return ResponseEntity.ok(world);
    }
    
    @PUT
    @Path("product")
    @Consumes(MediaType.APPLICATION_JSON)
    public void putProduct(@Context HttpServletRequest request, ProductType p) throws JAXBException, FileNotFoundException{
        String username = request.getHeader("X-User");
        services.updateProduct(username, p);
    }
    
    @PUT
    @Path("manager")
    @Consumes(MediaType.APPLICATION_JSON)
    public void putManager(@Context HttpServletRequest request, PallierType p) throws JAXBException, FileNotFoundException{
        String username = request.getHeader("X-User");
        services.updateManager(username, p);
    }*/
}
