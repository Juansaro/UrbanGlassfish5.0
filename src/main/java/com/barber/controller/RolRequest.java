/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.controller;

import com.barber.EJB.TipoRolFacadeLocal;
import com.barber.model.TipoRol;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named(value = "rolRequest")
@RequestScoped
public class RolRequest implements Serializable{
    
    @EJB
    private TipoRolFacadeLocal tipoRolFacadeLocal;
    
    @Inject
    private TipoRol rol;
    
    private List<TipoRol> roles;
    
    private TipoRol rolBarbero = new TipoRol();
    
    @PostConstruct
    private void init(){
        roles = tipoRolFacadeLocal.findAll();
        rol = new TipoRol();
    }
    
    public TipoRol asignacionRolCliente(){
        return tipoRolFacadeLocal.encontrarUsuarioRol();
    }

    public TipoRol getRol() {
        return rol;
    }

    public void setRol(TipoRol rol) {
        this.rol = rol;
    }

    public List<TipoRol> getRoles() {
        return roles;
    }

    public void setRoles(List<TipoRol> roles) {
        this.roles = roles;
    }

    public TipoRol getRolBarbero() {
        rolBarbero.setNumeroRol(3);
        return rolBarbero;
    }

    public void setRolBarbero(TipoRol rolBarbero) {
        this.rolBarbero = rolBarbero;
    }
    
}
