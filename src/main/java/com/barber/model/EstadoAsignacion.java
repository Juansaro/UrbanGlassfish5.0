/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author juan
 */
@Entity
@Table(name = "estado_asignacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoAsignacion.findAll", query = "SELECT e FROM EstadoAsignacion e"),
    @NamedQuery(name = "EstadoAsignacion.findByIdEstadoAsignacion", query = "SELECT e FROM EstadoAsignacion e WHERE e.idEstadoAsignacion = :idEstadoAsignacion"),
    @NamedQuery(name = "EstadoAsignacion.findByDescripcion", query = "SELECT e FROM EstadoAsignacion e WHERE e.descripcion = :descripcion")})
public class EstadoAsignacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_estado_asignacion")
    private Integer idEstadoAsignacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoAsignacionIdEstadoAsignacion", fetch = FetchType.LAZY)
    private Collection<Cita> citaCollection;

    public EstadoAsignacion() {
    }

    public EstadoAsignacion(Integer idEstadoAsignacion) {
        this.idEstadoAsignacion = idEstadoAsignacion;
    }

    public EstadoAsignacion(Integer idEstadoAsignacion, String descripcion) {
        this.idEstadoAsignacion = idEstadoAsignacion;
        this.descripcion = descripcion;
    }

    public Integer getIdEstadoAsignacion() {
        return idEstadoAsignacion;
    }

    public void setIdEstadoAsignacion(Integer idEstadoAsignacion) {
        this.idEstadoAsignacion = idEstadoAsignacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public Collection<Cita> getCitaCollection() {
        return citaCollection;
    }

    public void setCitaCollection(Collection<Cita> citaCollection) {
        this.citaCollection = citaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstadoAsignacion != null ? idEstadoAsignacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadoAsignacion)) {
            return false;
        }
        EstadoAsignacion other = (EstadoAsignacion) object;
        if ((this.idEstadoAsignacion == null && other.idEstadoAsignacion != null) || (this.idEstadoAsignacion != null && !this.idEstadoAsignacion.equals(other.idEstadoAsignacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descripcion;
    }
    
}
