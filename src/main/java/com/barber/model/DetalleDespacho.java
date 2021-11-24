/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.model;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author juan
 */
@Entity
@Table(name = "detalle_despacho")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleDespacho.findAll", query = "SELECT d FROM DetalleDespacho d"),
    @NamedQuery(name = "DetalleDespacho.findByIdDetalleDespacho", query = "SELECT d FROM DetalleDespacho d WHERE d.idDetalleDespacho = :idDetalleDespacho"),
    @NamedQuery(name = "DetalleDespacho.findByCantidadSolicitada", query = "SELECT d FROM DetalleDespacho d WHERE d.cantidadSolicitada = :cantidadSolicitada"),
    @NamedQuery(name = "DetalleDespacho.findByCostoTotal", query = "SELECT d FROM DetalleDespacho d WHERE d.costoTotal = :costoTotal")})
public class DetalleDespacho implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_detalle_despacho")
    private Integer idDetalleDespacho;
    @Column(name = "cantidad_solicitada")
    private Integer cantidadSolicitada;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "costo_total")
    private Float costoTotal;
    @JoinColumn(name = "despacho_id_despacho", referencedColumnName = "id_despacho")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Despacho despachoIdDespacho;
    @JoinColumn(name = "producto_id_producto", referencedColumnName = "id_producto")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Producto productoIdProducto;

    public DetalleDespacho() {
    }

    public DetalleDespacho(Integer idDetalleDespacho) {
        this.idDetalleDespacho = idDetalleDespacho;
    }

    public Integer getIdDetalleDespacho() {
        return idDetalleDespacho;
    }

    public void setIdDetalleDespacho(Integer idDetalleDespacho) {
        this.idDetalleDespacho = idDetalleDespacho;
    }

    public Integer getCantidadSolicitada() {
        return cantidadSolicitada;
    }

    public void setCantidadSolicitada(Integer cantidadSolicitada) {
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public Float getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(Float costoTotal) {
        this.costoTotal = costoTotal;
    }

    public Despacho getDespachoIdDespacho() {
        return despachoIdDespacho;
    }

    public void setDespachoIdDespacho(Despacho despachoIdDespacho) {
        this.despachoIdDespacho = despachoIdDespacho;
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
        hash += (idDetalleDespacho != null ? idDetalleDespacho.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleDespacho)) {
            return false;
        }
        DetalleDespacho other = (DetalleDespacho) object;
        if ((this.idDetalleDespacho == null && other.idDetalleDespacho != null) || (this.idDetalleDespacho != null && !this.idDetalleDespacho.equals(other.idDetalleDespacho))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.barber.model.DetalleDespacho[ idDetalleDespacho=" + idDetalleDespacho + " ]";
    }
    
}
