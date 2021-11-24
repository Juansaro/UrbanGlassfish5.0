/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.controller;

import com.barber.EJB.EstadoAsignacionFacadeLocal;
import com.barber.model.Ciudad;
import com.barber.model.EstadoAsignacion;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named(value = "estadoAsignacionRequest")
@RequestScoped
public class EstadoAsignacionRequest implements Serializable{
    
    @EJB
    private EstadoAsignacionFacadeLocal estadoAsignacionFacadeLocal;
    
    @Inject
    private EstadoAsignacion estadoAsignacion;
    
    private List<EstadoAsignacion> estadoAsignaciones;
    
    @PostConstruct
    public void init(){
        estadoAsignaciones = estadoAsignacionFacadeLocal.findAll();
        estadoAsignacion = new EstadoAsignacion();
    }
    
    //En espera para cita
    public EstadoAsignacion citaEspera(){
        return estadoAsignacionFacadeLocal.asignacionCitaInicial();
    }

    public EstadoAsignacion getEstadoAsignacion() {
        return estadoAsignacion;
    }

    public void setEstadoAsignacion(EstadoAsignacion estadoAsignacion) {
        this.estadoAsignacion = estadoAsignacion;
    }

    public List<EstadoAsignacion> getEstadoAsignaciones() {
        return estadoAsignaciones;
    }

    public void setEstadoAsignaciones(List<EstadoAsignacion> estadoAsignaciones) {
        this.estadoAsignaciones = estadoAsignaciones;
    }    
    
}
