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
@Table(name = "despacho")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Despacho.findAll", query = "SELECT d FROM Despacho d"),
    @NamedQuery(name = "Despacho.findByIdDespacho", query = "SELECT d FROM Despacho d WHERE d.idDespacho = :idDespacho"),
    @NamedQuery(name = "Despacho.findByFechaSolicitud", query = "SELECT d FROM Despacho d WHERE d.fechaSolicitud = :fechaSolicitud")})
public class Despacho implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_despacho")
    private Integer idDespacho;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_solicitud")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSolicitud;
    @JoinColumn(name = "recepcionista", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuario recepcionista;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "despachoIdDespacho", fetch = FetchType.LAZY)
    private Collection<DetalleDespacho> detalleDespachoCollection;

    public Despacho() {
    }

    public Despacho(Integer idDespacho) {
        this.idDespacho = idDespacho;
    }

    public Despacho(Integer idDespacho, Date fechaSolicitud) {
        this.idDespacho = idDespacho;
        this.fechaSolicitud = fechaSolicitud;
    }

    public Integer getIdDespacho() {
        return idDespacho;
    }

    public void setIdDespacho(Integer idDespacho) {
        this.idDespacho = idDespacho;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Usuario getRecepcionista() {
        return recepcionista;
    }

    public void setRecepcionista(Usuario recepcionista) {
        this.recepcionista = recepcionista;
    }

    @XmlTransient
    public Collection<DetalleDespacho> getDetalleDespachoCollection() {
        return detalleDespachoCollection;
    }

    public void setDetalleDespachoCollection(Collection<DetalleDespacho> detalleDespachoCollection) {
        this.detalleDespachoCollection = detalleDespachoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDespacho != null ? idDespacho.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Despacho)) {
            return false;
        }
        Despacho other = (Despacho) object;
        if ((this.idDespacho == null && other.idDespacho != null) || (this.idDespacho != null && !this.idDespacho.equals(other.idDespacho))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.barber.model.Despacho[ idDespacho=" + idDespacho + " ]";
    }
    
}
