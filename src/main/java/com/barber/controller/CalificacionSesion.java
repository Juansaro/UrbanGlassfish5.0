/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.controller;

import com.barber.EJB.CalificacionFacadeLocal;
import com.barber.EJB.CitaFacadeLocal;
import com.barber.model.Calificacion;
import com.barber.model.Cita;
import com.barber.model.EstadoAsignacion;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author juan
 */
@Named(value = "calificacionSesion")
@SessionScoped
public class CalificacionSesion implements Serializable {

    @EJB
    private CalificacionFacadeLocal calificacionFacadeLocal;
    @EJB
    private CitaFacadeLocal citaFacadeLocal;

    private Calificacion calificacion;

    @Inject
    private Cita cita;
    @Inject
    private EstadoAsignacion asignacionTemporal;
    @Inject
    private EstadoAsignacion asignacionCompletado;
    @Inject
    private UsuarioSesion usuSesion;

    private Cita citaTemporal;

    private List<Calificacion> calificaciones;
    private List<Cita> citas;

    private Calificacion cal = new Calificacion();
    private Calificacion calTemporal = new Calificacion();

    @PostConstruct
    private void init() {
        calificaciones = calificacionFacadeLocal.leerTdos();
        asignacionCompletado = new EstadoAsignacion();
        asignacionTemporal = new EstadoAsignacion();
        asignacionTemporal.setIdEstadoAsignacion(2);
        asignacionCompletado.setIdEstadoAsignacion(5);
        citas = citaFacadeLocal.leerCitas(asignacionTemporal);
        calificacion = new Calificacion();
        citaTemporal = new Cita();
    }

    public void guardarCitaTemporal(Cita c) {
        citaTemporal = c;
    }

    //Registrar
    public void registrarCalificacion() {
        try {
            if (citaFacadeLocal.calificarCita(citaTemporal.getIdCita())) {
                cal.setCitaTerminada(citaTemporal);
                calificacionFacadeLocal.create(cal);
                cal = new Calificacion();
                citaTemporal = new Cita();
                calificaciones = calificacionFacadeLocal.leerTdos();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Calificación registrada", "Calificación registrada"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error de registro", "Error de registro"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error de registro", "Error de registro"));
        }
    }

    //Guardar temporal
    public void guardarTemporal(Calificacion c) {
        calTemporal = c;
    }

    //Editar calificación
    public void editarCalificacion() {
        try {
            calificacionFacadeLocal.edit(calTemporal);
            calTemporal = new Calificacion();
            calificaciones = calificacionFacadeLocal.leerTdos();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Calificación editada", "Calificación editada"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Calificación editada", "Calificación editada"));
        }
    }
    
    //eliminar
    public void eliminarCalificacion(Calificacion c) {
        try {
            calificacionFacadeLocal.remove(c);
            calificaciones = calificacionFacadeLocal.leerTdos();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Se eliminó la calificación", "Se eliminó la calificación"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error de eliminación", "Error de eliminación"));
        }
    }
    
    public List<Calificacion> leerCalificacionCliente(){
        return calificacionFacadeLocal.leerCalificacionesCliente(usuSesion.getUsuLog());
    }
    
    public List<Calificacion> leerCalificacionBarbero(){
        return calificacionFacadeLocal.leerCalificacionesBarbero(usuSesion.getUsuLog());
    }

    public Calificacion getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Calificacion calificacion) {
        this.calificacion = calificacion;
    }

    public List<Calificacion> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(List<Calificacion> calificaciones) {
        this.calificaciones = calificaciones;
    }

    public Calificacion getCal() {
        return cal;
    }

    public void setCal(Calificacion cal) {
        this.cal = cal;
    }

    public Calificacion getCalTemporal() {
        return calTemporal;
    }

    public void setCalTemporal(Calificacion calTemporal) {
        this.calTemporal = calTemporal;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }

}
