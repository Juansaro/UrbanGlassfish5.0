/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author juan
 */
@Entity
@Table(name = "cita")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cita.findAll", query = "SELECT c FROM Cita c"),
    @NamedQuery(name = "Cita.findByIdCita", query = "SELECT c FROM Cita c WHERE c.idCita = :idCita"),
    @NamedQuery(name = "Cita.findByFechaCita", query = "SELECT c FROM Cita c WHERE c.fechaCita = :fechaCita"),
    @NamedQuery(name = "Cita.findByCosto", query = "SELECT c FROM Cita c WHERE c.costo = :costo"),
    @NamedQuery(name = "Cita.findByRegistroActual", query = "SELECT c FROM Cita c WHERE c.registroActual = :registroActual")})
public class Cita implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_cita")
    private Integer idCita;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_cita")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCita;
    @Basic(optional = false)
    @NotNull
    @Column(name = "costo")
    private float costo;
    @Column(name = "registro_actual")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registroActual;
    @JoinTable(name = "cita_has_servicio", joinColumns = {
        @JoinColumn(name = "cita_id_cita", referencedColumnName = "id_cita")}, inverseJoinColumns = {
        @JoinColumn(name = "servicio_id_servicio", referencedColumnName = "id_servicio")})
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<Servicio> servicioCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "citaTerminada", fetch = FetchType.LAZY)
    private Collection<Calificacion> calificacionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "citaIdCita", fetch = FetchType.LAZY)
    private Collection<Factura> facturaCollection;
    @JoinColumn(name = "estado_asignacion_id_estado_asignacion", referencedColumnName = "id_estado_asignacion")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EstadoAsignacion estadoAsignacionIdEstadoAsignacion;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuario idCliente;
    @JoinColumn(name = "id_barbero", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuario idBarbero;

    public Cita() {
    }

    public Cita(Integer idCita) {
        this.idCita = idCita;
    }

    public Cita(Integer idCita, Date fechaCita, float costo) {
        this.idCita = idCita;
        this.fechaCita = fechaCita;
        this.costo = costo;
    }

    public Integer getIdCita() {
        return idCita;
    }

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public Date getRegistroActual() {
        return registroActual;
    }

    public void setRegistroActual(Date registroActual) {
        this.registroActual = registroActual;
    }

    @XmlTransient
    public Collection<Servicio> getServicioCollection() {
        return servicioCollection;
    }

    public void setServicioCollection(Collection<Servicio> servicioCollection) {
        this.servicioCollection = servicioCollection;
    }

    @XmlTransient
    public Collection<Calificacion> getCalificacionCollection() {
        return calificacionCollection;
    }

    public void setCalificacionCollection(Collection<Calificacion> calificacionCollection) {
        this.calificacionCollection = calificacionCollection;
    }

    @XmlTransient
    public Collection<Factura> getFacturaCollection() {
        return facturaCollection;
    }

    public void setFacturaCollection(Collection<Factura> facturaCollection) {
        this.facturaCollection = facturaCollection;
    }

    public EstadoAsignacion getEstadoAsignacionIdEstadoAsignacion() {
        return estadoAsignacionIdEstadoAsignacion;
    }

    public void setEstadoAsignacionIdEstadoAsignacion(EstadoAsignacion estadoAsignacionIdEstadoAsignacion) {
        this.estadoAsignacionIdEstadoAsignacion = estadoAsignacionIdEstadoAsignacion;
    }

    public Usuario getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Usuario idCliente) {
        this.idCliente = idCliente;
    }

    public Usuario getIdBarbero() {
        return idBarbero;
    }

    public void setIdBarbero(Usuario idBarbero) {
        this.idBarbero = idBarbero;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCita != null ? idCita.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cita)) {
            return false;
        }
        Cita other = (Cita) object;
        if ((this.idCita == null && other.idCita != null) || (this.idCita != null && !this.idCita.equals(other.idCita))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + fechaCita;
    }
    
}
