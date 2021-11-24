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
@Table(name = "compra")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Compra.findAll", query = "SELECT c FROM Compra c"),
    @NamedQuery(name = "Compra.findByNumeroCompra", query = "SELECT c FROM Compra c WHERE c.numeroCompra = :numeroCompra"),
    @NamedQuery(name = "Compra.findByFechaSolicitud", query = "SELECT c FROM Compra c WHERE c.fechaSolicitud = :fechaSolicitud")})
public class Compra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "numero_compra")
    private Integer numeroCompra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_solicitud")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSolicitud;
    @JoinColumn(name = "numero_proveedor", referencedColumnName = "numero_proveedor")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Proveedor numeroProveedor;
    @JoinColumn(name = "recepcionista", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuario recepcionista;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "numeroCompra", fetch = FetchType.LAZY)
    private Collection<DetalleCompra> detalleCompraCollection;

    public Compra() {
    }

    public Compra(Integer numeroCompra) {
        this.numeroCompra = numeroCompra;
    }

    public Compra(Integer numeroCompra, Date fechaSolicitud) {
        this.numeroCompra = numeroCompra;
        this.fechaSolicitud = fechaSolicitud;
    }

    public Integer getNumeroCompra() {
        return numeroCompra;
    }

    public void setNumeroCompra(Integer numeroCompra) {
        this.numeroCompra = numeroCompra;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Proveedor getNumeroProveedor() {
        return numeroProveedor;
    }

    public void setNumeroProveedor(Proveedor numeroProveedor) {
        this.numeroProveedor = numeroProveedor;
    }

    public Usuario getRecepcionista() {
        return recepcionista;
    }

    public void setRecepcionista(Usuario recepcionista) {
        this.recepcionista = recepcionista;
    }

    @XmlTransient
    public Collection<DetalleCompra> getDetalleCompraCollection() {
        return detalleCompraCollection;
    }

    public void setDetalleCompraCollection(Collection<DetalleCompra> detalleCompraCollection) {
        this.detalleCompraCollection = detalleCompraCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numeroCompra != null ? numeroCompra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Compra)) {
            return false;
        }
        Compra other = (Compra) object;
        if ((this.numeroCompra == null && other.numeroCompra != null) || (this.numeroCompra != null && !this.numeroCompra.equals(other.numeroCompra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.barber.model.Compra[ numeroCompra=" + numeroCompra + " ]";
    }
    
}
