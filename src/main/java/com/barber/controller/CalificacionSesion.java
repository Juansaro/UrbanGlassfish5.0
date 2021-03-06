/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.controller;

import com.barber.EJB.CalificacionFacadeLocal;
import com.barber.EJB.CitaFacadeLocal;
import com.barber.EJB.UsuarioFacadeLocal;
import com.barber.model.Calificacion;
import com.barber.model.Cita;
import com.barber.model.EstadoAsignacion;
import com.barber.model.TipoRol;
import com.barber.model.Usuario;
import com.barber.utilidades.MailBajaCalificacion;
import com.barber.utilidades.MailBuenaCalificacion;
import com.barber.utilidades.MailMediaCalificacion;
import java.io.Serializable;
import java.util.ArrayList;
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
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;

    private Calificacion calificacion;

    @Inject
    private Cita cita;
    @Inject
    private EstadoAsignacion asignacionTemporal;
    @Inject
    private EstadoAsignacion asignacionCompletado;
    @Inject
    private UsuarioSesion usuSesion;
    @Inject
    private Usuario usuTemporal;
    @Inject
    private Usuario barbero;
    @Inject
    private TipoRol rol;

    private Cita citaTemporal;

    private List<Calificacion> calificaciones;
    private List<Calificacion> calificacionesTemporales;
    private List<Calificacion> calificacionesRanking;
    private List<Cita> citas;
    private List<Usuario> barberos;
    private Calificacion cal = new Calificacion();
    private Calificacion calTemporal = new Calificacion();

    @PostConstruct
    public void init() {
        barbero = new Usuario();
        usuTemporal = new Usuario();
        calificacionesTemporales = new ArrayList<>();
        calificacionesRanking = new ArrayList<>();
        barberos = new ArrayList<>();
        calificacionesRanking = new ArrayList<>();
        calificaciones = calificacionFacadeLocal.leerTdos();
        asignacionCompletado = new EstadoAsignacion();
        asignacionTemporal = new EstadoAsignacion();
        rol = new TipoRol();
        rol.setNumeroRol(3);
        asignacionTemporal.setIdEstadoAsignacion(2);
        asignacionCompletado.setIdEstadoAsignacion(5);
        citas = citaFacadeLocal.leerCitas(asignacionTemporal);
        calificacion = new Calificacion();
        citaTemporal = new Cita();
        rankingBarberos();
    }

    public void rankingBarberos() {
        calificacionesRanking.clear();
        int it = 0, itCal = 0, cantidadCalificaciones = 0, promedio = 0, acum = 0;
        barberos = usuarioFacadeLocal.leerBarberos(rol);
        for (Usuario Iteradorusu : barberos) {
            usuTemporal = barberos.get(it);
            calificacionesTemporales = calificacionFacadeLocal.leerCalificacionesBarbero(usuTemporal);
            for (Calificacion iteradorCal : calificacionesTemporales) {
                calTemporal = calificacionesTemporales.get(itCal);
                cantidadCalificaciones = calificacionesTemporales.size();
                acum = acum + Integer.parseInt(calificacionesTemporales.get(itCal).getPuntaje());
                itCal++;
            }
            calTemporal.setIdCalificacion(it);
            if (calificacionesTemporales.isEmpty()) {
                citaTemporal.setIdBarbero(usuTemporal);
                calTemporal.setCitaTerminada(citaTemporal);
                calTemporal.setPuntaje("0");
                calificacionesRanking.add(calTemporal);
                calTemporal = new Calificacion();
                citaTemporal = new Cita();
            } else {
                promedio = acum / cantidadCalificaciones;
                calTemporal.setPuntaje(String.valueOf(promedio));
                calificacionesRanking.add(calTemporal);
                calTemporal = new Calificacion();
                calificacionesTemporales.clear();
            }
            cantidadCalificaciones = 0;
            acum = 0;
            promedio = 0;
            itCal = 0;
            it++;
        }
        usuTemporal = new Usuario();
        barberos.clear();
        calTemporal = new Calificacion();
        cantidadCalificaciones = 0;
        promedio = 0;
        acum = 0;
        it = 0;
    }

    public Calificacion rankingBarbero() {
        int it = 0, itCal = 0, cantidadCalificaciones = 0, promedio = 0, acum = 0;
        barbero = new Usuario();
        calTemporal = new Calificacion();
        barbero = usuarioFacadeLocal.encontrarUsuarioCorreo(usuSesion.getUsuLog().getCorreo());
        calificacionesTemporales = calificacionFacadeLocal.leerCalificacionesBarbero(barbero);
        for (Calificacion iteradorCal : calificacionesTemporales) {
            calTemporal = calificacionesTemporales.get(itCal);
            cantidadCalificaciones = calificacionesTemporales.size();
            acum = acum + Integer.parseInt(calificacionesTemporales.get(itCal).getPuntaje());
            itCal++;
        }
        itCal = 0;
        if (calificacionesTemporales.isEmpty()) {
            calTemporal.setPuntaje("0");
            return calTemporal;
        }
        promedio = acum / cantidadCalificaciones;
        calTemporal.setPuntaje(String.valueOf(promedio));
        it++;
        return calTemporal;
    }

    public void guardarCitaTemporal(Cita c) {
        citaTemporal = c;
    }

    public void enviarCalificacionBarbero(Calificacion calificacionIn) {

        if (Integer.parseInt(calificacionIn.getPuntaje()) >= 4) {

            MailBuenaCalificacion.buenaCalificaci??n(calificacionIn.getCitaTerminada().getIdBarbero().getNombre(), calificacionIn.getCitaTerminada().getIdBarbero().getCorreo());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correo env??ado", "Correo env??ado"));

        } else if (Integer.parseInt(calificacionIn.getPuntaje()) == 3) {

            MailMediaCalificacion.mediaCalificaci??n(calificacionIn.getCitaTerminada().getIdBarbero().getNombre(), calificacionIn.getCitaTerminada().getIdBarbero().getCorreo());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correo env??ado", "Correo env??ado"));

        } else if (Integer.parseInt(calificacionIn.getPuntaje()) == 2 || Integer.parseInt(calificacionIn.getPuntaje()) == 1) {

            MailBajaCalificacion.bajaCalificaci??n(calificacionIn.getCitaTerminada().getIdBarbero().getNombre(), calificacionIn.getCitaTerminada().getIdBarbero().getCorreo());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correo env??ado", "Correo env??ado"));

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error de env??o de mail", "Error de env??o de mail"));
        }

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
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Calificaci??n registrada", "Calificaci??n registrada"));
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

    //Editar calificaci??n
    public void editarCalificacion() {
        try {
            calificacionFacadeLocal.edit(calTemporal);
            calTemporal = new Calificacion();
            calificaciones = calificacionFacadeLocal.leerTdos();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Calificaci??n editada", "Calificaci??n editada"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Calificaci??n editada", "Calificaci??n editada"));
        }
    }

    //eliminar
    public void eliminarCalificacion(Calificacion c) {
        try {
            calificacionFacadeLocal.remove(c);
            calificaciones = calificacionFacadeLocal.leerTdos();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Se elimin?? la calificaci??n", "Se elimin?? la calificaci??n"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error de eliminaci??n", "Error de eliminaci??n"));
        }
    }

    public List<Calificacion> leerCalificacionCliente() {
        return calificacionFacadeLocal.leerCalificacionesCliente(usuSesion.getUsuLog());
    }

    public List<Calificacion> leerCalificacionBarbero() {
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

    public List<Calificacion> getCalificacionesRanking() {
        return calificacionesRanking;
    }

    public void setCalificacionesRanking(List<Calificacion> calificacionesRanking) {
        this.calificacionesRanking = calificacionesRanking;
    }
}
