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
            proveedores = proveedorFacadeLocal.leerTodos();
            pro = new Proveedor();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Proveedor registrado", "Proveedor registrado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error de registro", "Error de registro"));
        }
    }
 
    //Recupera datos del proveedor al cual se va a editar
     public void guardarTemporal(Proveedor p) {
        proTemporal = p;
    }

    //Editar proveedor (En el modal)
    public void editarProveedor() {
        try {
            proveedorFacadeLocal.edit(proTemporal);
            proveedor = new Proveedor();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Proveedor editado", "Proveedor editado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error de edición", "Error de edición"));
        }

    }
    
    //Eliminar
    public void eliminarProveedor(Proveedor p){
        try{
            proveedorFacadeLocal.remove(p);
            proveedorFacadeLocal.leerTodos();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Proveedor eliminado", "Proveedor eliminado"));
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error de eliminación", "Error de eliminación"));
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

