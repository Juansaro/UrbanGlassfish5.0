/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.controller;

import com.barber.EJB.CompraFacadeLocal;
import com.barber.EJB.DetalleCompraFacadeLocal;
import com.barber.model.Compra;
import com.barber.model.DetalleCompra;
import com.barber.model.Producto;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named(value = "detalleCompraSesion")
@SessionScoped
public class DetalleCompraSesion implements Serializable{
    
    @EJB
    private DetalleCompraFacadeLocal detalleCompraFacadeLocal;
    @EJB
    private CompraFacadeLocal compraFacadeLocal;
    
    private DetalleCompra detalleCompra;
    
    @Inject
    private Compra compra;
    @Inject
    private Producto producto;
    
    private List<DetalleCompra> detalleCompras;
    private List<Compra> compras;
    
    private DetalleCompra detCom = new DetalleCompra();
    private DetalleCompra detComTemporal = new DetalleCompra();
    
    @PostConstruct
    private void init(){
        detalleCompras = detalleCompraFacadeLocal.findAll();
        compras = compraFacadeLocal.findAll();
    }

    public void registrarDetalleCompra(){
        try {
            this.detCom.setNumeroCompra(compra);
            this.detCom.setProductoIdProducto(producto);
            detalleCompraFacadeLocal.create(detCom);
            detalleCompras = detalleCompraFacadeLocal.findAll();
        } catch (Exception e) {
        }
    }
    
    public void guardarTemporal(DetalleCompra d){
        detComTemporal = d;
    }
    
    public void editarDetalleCompra(){
        try {
            this.detComTemporal.setNumeroCompra(compra);
            this.detComTemporal.setProductoIdProducto(producto);
            detalleCompraFacadeLocal.edit(detComTemporal);
            detalleCompras = detalleCompraFacadeLocal.findAll();
        } catch (Exception e) {
        }
    }
    
    public void eliminarDetalleCompra(DetalleCompra d){
        try {
            detalleCompraFacadeLocal.remove(d);
            detalleCompras = detalleCompraFacadeLocal.findAll();
        } catch (Exception e) {
        }
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public List<DetalleCompra> getDetalleCompras() {
        return detalleCompras;
    }

    public void setDetalleCompras(List<DetalleCompra> detalleCompras) {
        this.detalleCompras = detalleCompras;
    }

    public List<Compra> getCompras() {
        return compras;
    }

    public void setCompras(List<Compra> compras) {
        this.compras = compras;
    }

    public DetalleCompra getDetalleCompra() {
        return detalleCompra;
    }

    public void setDetalleCompra(DetalleCompra detalleCompra) {
        this.detalleCompra = detalleCompra;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public DetalleCompra getDetCom() {
        return detCom;
    }

    public void setDetCom(DetalleCompra detCom) {
        this.detCom = detCom;
    }

    public DetalleCompra getDetComTemporal() {
        return detComTemporal;
    }

    public void setDetComTemporal(DetalleCompra detComTemporal) {
        this.detComTemporal = detComTemporal;
    }
    
    
}
