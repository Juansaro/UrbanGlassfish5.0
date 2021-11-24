/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author juan
 */
@Entity
@Table(name = "detalle_compra")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleCompra.findAll", query = "SELECT d FROM DetalleCompra d"),
    @NamedQuery(name = "DetalleCompra.findByNumeroDetalle", query = "SELECT d FROM DetalleCompra d WHERE d.numeroDetalle = :numeroDetalle"),
    @NamedQuery(name = "DetalleCompra.findByCantidadSolicitada", query = "SELECT d FROM DetalleCompra d WHERE d.cantidadSolicitada = :cantidadSolicitada"),
    @NamedQuery(name = "DetalleCompra.findByFechaRecibido", query = "SELECT d FROM DetalleCompra d WHERE d.fechaRecibido = :fechaRecibido"),
    @NamedQuery(name = "DetalleCompra.findByCostoTotal", query = "SELECT d FROM DetalleCompra d WHERE d.costoTotal = :costoTotal")})
public class DetalleCompra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "numero_detalle")
    private Integer numeroDetalle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad_solicitada")
    private int cantidadSolicitada;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_recibido")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRecibido;
    @Basic(optional = false)
    @NotNull
    @Column(name = "costo_total")
    private float costoTotal;
    @JoinColumn(name = "numero_compra", referencedColumnName = "numero_compra")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Compra numeroCompra;
    @JoinColumn(name = "producto_id_producto", referencedColumnName = "id_producto")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Producto productoIdProducto;

    public DetalleCompra() {
    }

    public DetalleCompra(Integer numeroDetalle) {
        this.numeroDetalle = numeroDetalle;
    }

    public DetalleCompra(Integer numeroDetalle, int cantidadSolicitada, Date fechaRecibido, float costoTotal) {
        this.numeroDetalle = numeroDetalle;
        this.cantidadSolicitada = cantidadSolicitada;
        this.fechaRecibido = fechaRecibido;
        this.costoTotal = costoTotal;
    }

    public Integer getNumeroDetalle() {
        return numeroDetalle;
    }

    public void setNumeroDetalle(Integer numeroDetalle) {
        this.numeroDetalle = numeroDetalle;
    }

    public int getCantidadSolicitada() {
        return cantidadSolicitada;
    }

    public void setCantidadSolicitada(int cantidadSolicitada) {
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public Date getFechaRecibido() {
        return fechaRecibido;
    }

    public void setFechaRecibido(Date fechaRecibido) {
        this.fechaRecibido = fechaRecibido;
    }

    public float getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(float costoTotal) {
        this.costoTotal = costoTotal;
    }

    public Compra getNumeroCompra() {
        return numeroCompra;
    }

    public void setNumeroCompra(Compra numeroCompra) {
        this.numeroCompra = numeroCompra;
    }

    public Producto getProductoIdProducto() {
        return productoIdProducto;
    }

    public void setProductoIdProducto(Producto productoIdProducto) {
        this.productoIdProducto = productoIdProducto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numeroDetalle != null ? numeroDetalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleCompra)) {
            return false;
        }
        DetalleCompra other = (DetalleCompra) object;
        if ((this.numeroDetalle == null && other.numeroDetalle != null) || (this.numeroDetalle != null && !this.numeroDetalle.equals(other.numeroDetalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.barber.model.DetalleCompra[ numeroDetalle=" + numeroDetalle + " ]";
    }
    
}
