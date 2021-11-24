/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.controller;

import com.barber.EJB.ProveedorFacadeLocal;
import com.barber.model.Proveedor;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author juan
 */
@Named(value = "proveedorSesion")
@SessionScoped
public class ProveedorSesion implements Serializable{
    
    @EJB
    private ProveedorFacadeLocal proveedorFacadeLocal;
    
    private Proveedor proveedor;
    private List<Proveedor> proveedores;
            
    private Proveedor pro = new Proveedor();
    private Proveedor proTemporal = new Proveedor();
    
    @PostConstruct
    public void init(){
        proveedores = proveedorFacadeLocal.leerTodos();
        proveedor = new Proveedor();
    }
    
    //Registrar proveedor
    public void registrarProveedor(){
        try {
            proveedorFacadeLocal.create(pro);
            proveedores = proveedorFacadeLocal.findAll();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Proveedor registrado", "Proveedor registrado"));
            FacesContext.getCurrentInstance().getExternalContext().redirect("/UrbanBarberShop/faces/recepcionista/consultarProveedor.xhtml");
        } catch (Exception e) {
        }
    }
    
    //Preparar página para 
    public String prepararEliminar(){
        proveedores = proveedorFacadeLocal.findAll();
        return "/RecepProveedorConsultar.xhtml";
    }
    
    //Recupera datos del proveedor al cual se va a editar
     public String guardarTemporal(Proveedor p) {
        proTemporal = p;
        return "/RecepProveedorModificar.xhtml";
    }

    //Editar proveedor (En el modal)
    public void editarProveedor() {
        try {
            proveedorFacadeLocal.edit(proTemporal);
            this.proveedor = new Proveedor();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Proveedor editado", "Proveedor editado"));
        } catch (Exception e) {
            
        }

    }
    
    //Eliminar
    public void eliminarProveedor(Proveedor p){
        try{
            this.proveedorFacadeLocal.remove(p);
            this.proveedor = new Proveedor();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Proveedor eliminado", "Proveedor eliminado"));
            //Colocar prepararEliminar()
            prepararEliminar();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //Getters y Setters
    
    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public List<Proveedor> getProveedores() {
        return proveedores;
    }

    public void setProveedores(List<Proveedor> proveedores) {
        this.proveedores = proveedores;
    }

    public Proveedor getPro() {
        return pro;
    }

    public void setPro(Proveedor pro) {
        this.pro = pro;
    }

    public Proveedor getProTemporal() {
        return proTemporal;
    }

    public void setProTemporal(Proveedor proTemporal) {
        this.proTemporal = proTemporal;
    }
    
}

