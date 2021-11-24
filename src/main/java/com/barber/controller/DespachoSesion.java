/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.controller;

import com.barber.EJB.ProductoFacadeLocal;
import com.barber.model.Despacho;
import com.barber.model.Producto;
import com.barber.model.Usuario;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import com.barber.EJB.DespachoFacadeLocal;
import com.barber.EJB.DetalleDespachoFacadeLocal;
import com.barber.model.DetalleDespacho;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author juan
 */
@Named(value = "despachoSesion")
@SessionScoped
public class DespachoSesion implements Serializable {

    @EJB
    private DespachoFacadeLocal despachoFacadeLocal;
    @EJB
    private ProductoFacadeLocal productoFacadeLocal;
    @EJB
    private DetalleDespachoFacadeLocal detalleDespachoFacadeLocal;

    private Despacho despacho;

    @Inject
    private Producto producto;
    @Inject
    private Usuario usuario;
    @Inject
    private DetalleDespacho detalleDespachoIn;
    @Inject
    private UsuarioSesion usuSesion;

    private float costoTotal;
    private Integer indiceTemporal;

    private List<DetalleDespacho> detalles = new ArrayList<>();
    private List<DetalleDespacho> detallesSolicitados;
    private List<Despacho> despachoProductos;
    private List<Producto> productos;
    private List<Usuario> usuarios;

    private Despacho des;
    private Despacho desTemporal = new Despacho();

    @PostConstruct
    public void init() {
        despachoProductos = despachoFacadeLocal.leerTodos();
        productos = productoFacadeLocal.leerTodos();
        des = new Despacho();
        detalles.clear();
        detallesSolicitados = new ArrayList<>();
        despacho = new Despacho();
        indiceTemporal = 0;
    }

    public Date ObtenerFechaActual() {
        try {
            DateTimeFormatter d = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            String fechaActual = d.format(LocalDateTime.now());
            SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date fechaFormateada = formato.parse(fechaActual);
            return fechaFormateada;
        } catch (ParseException e) {
            return null;
        }
    }

    public void guardarProductoTemporal(Producto prodIn) {
        producto = prodIn;
    }
    
    public void guardarDetallesTemporales() {
        if (producto.getIdProducto() != null) {
            if (detalleDespachoIn.getCantidadSolicitada() != 0) {
                indiceTemporal++;
                detalleDespachoIn.setIdDetalleDespacho(indiceTemporal);
                detalleDespachoIn.setProductoIdProducto(producto);
                Integer parse = detalleDespachoIn.getCantidadSolicitada();
                float cantidadParseada = parse.floatValue();
                float acumulador = 0;
                float precio = detalleDespachoIn.getProductoIdProducto().getPrecio();
                acumulador = acumulador + cantidadParseada * precio;
                costoTotal = costoTotal + acumulador;
                detalleDespachoIn.setProductoIdProducto(producto);
                detalleDespachoIn.setCostoTotal(acumulador);
                detalles.add(detalleDespachoIn);
                detalleDespachoIn = new DetalleDespacho();
                producto = new Producto();
            } else {
                detalleDespachoIn = new DetalleDespacho();
                producto = new Producto();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Escribe una cantidad", "Escribe una cantidad"));
            }
        } else {
            detalleDespachoIn = new DetalleDespacho();
            producto = new Producto();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Ingresa un producto", "Ingresa un producto"));
        }
    }

    public void eliminarDetalleTemporal(DetalleDespacho detIn) {
        float acumulador = 0;
        acumulador = acumulador + detIn.getCostoTotal();
        detalles.remove(detIn);
        costoTotal = costoTotal - acumulador;
    }

    //Registrar
    public void registrarDespachoProducto() {
        try {
            int cantidadProdOld = 0, cantidadBodOld = 0, cantidadProdNew = 0, cantidadBodNew = 0, contador = 0, idDespacho = 0;
            des.setRecepcionista(usuSesion.getUsuLog());
            des.setFechaSolicitud(ObtenerFechaActual());
            despachoFacadeLocal.create(des);
            idDespacho = des.getIdDespacho();
            for (DetalleDespacho it : detalles) {
                //Se obtiene los objetos guardados en el ArrayList detalles
                detalleDespachoIn = detalles.get(contador);
                //Se valida que la cantidad solicitada no supere el stock actual de productos
                if (detalleDespachoIn.getCantidadSolicitada() <= detalleDespachoIn.getProductoIdProducto().getCantidad()) {
                    //Clave primaria por defecto
                    detalleDespachoIn.setIdDetalleDespacho(null);
                    detalleDespachoIn.setDespachoIdDespacho(despacho);
                    //Obtención de valores anteriores
                    cantidadProdOld = detalleDespachoIn.getProductoIdProducto().getCantidad();
                    cantidadBodOld = detalleDespachoIn.getProductoIdProducto().getBodegaIdBodega().getExistencias();
                    //Resta de valores antiguos con nuevos
                    cantidadProdNew = cantidadProdOld - detalleDespachoIn.getCantidadSolicitada();
                    cantidadBodNew = cantidadBodOld - detalleDespachoIn.getCantidadSolicitada();
                    //En los dos métodos a continuación se recoje con getters (todos son int) como argumento productoIdProducto().getIdProducto());
                    despachoFacadeLocal.salidaBodega(cantidadBodNew, detalleDespachoIn.getProductoIdProducto().getBodegaIdBodega().getIdBodega());
                    detalleDespachoFacadeLocal.registrarDetalle(detalleDespachoIn.getCantidadSolicitada(), detalleDespachoIn.getCostoTotal(), detalleDespachoIn.getProductoIdProducto().getIdProducto(), des.getIdDespacho());
                    //detalleDespachoFacadeLocal.create(detalleDespachoIn);
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Solicitaste un valor mayor al stock de " + detalleDespachoIn.getProductoIdProducto().getNombreProducto(), "Solicitaste un valor mayor al stock de " + detalleDespachoIn.getProductoIdProducto().getNombreProducto()));
                    cantidadProdOld = 0;
                    cantidadBodOld = 0;
                    cantidadProdNew = 0;
                    cantidadBodNew = 0;
                }
                contador++;
                cantidadProdOld = 0;
                cantidadBodOld = 0;
                cantidadProdNew = 0;
                cantidadBodNew = 0;
            }
            indiceTemporal = 0;
            detalleDespachoIn = new DetalleDespacho();
            des = new Despacho();
            detalles = new ArrayList<>();
            despachoProductos = despachoFacadeLocal.leerTodos();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error de registro", "Error de registro"));
        }
    }

    public void consultarDetallesRegistrados(Despacho d) {
        detallesSolicitados = detalleDespachoFacadeLocal.leerTodos(d);
    }
    
    //Guardar temportal
    public void guardarTemporal(Despacho d) {
        desTemporal = d;
    }

    public void editarDespacho() {
        try {
            despachoFacadeLocal.edit(desTemporal);
            this.despacho = new Despacho();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error de edición", "Error de edición"));
        }
    }

    //Eliminar
    public void eliminarDespachoProducto(Despacho d) {
        try {
            this.despachoFacadeLocal.remove(d);
            this.despacho = new Despacho();
            //Colocar prepararEliminar()
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Proveedor eliminado", "Proveedor eliminado"));
        } catch (Exception e) {
        }
    }

    //Getters y Setters
    public Despacho getDespacho() {
        return despacho;
    }

    public void setDespacho(Despacho despacho) {
        this.despacho = despacho;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Despacho> getDespachoProductos() {
        return despachoProductos;
    }

    public void setDespachoProductos(List<Despacho> despachoProductos) {
        this.despachoProductos = despachoProductos;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Despacho getDes() {
        return des;
    }

    public void setDes(Despacho des) {
        this.des = des;
    }

    public Despacho getDesTemporal() {
        return desTemporal;
    }

    public void setDesTemporal(Despacho desTemporal) {
        this.desTemporal = desTemporal;
    }

    public float getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(float costoTotal) {
        this.costoTotal = costoTotal;
    }

    public List<DetalleDespacho> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleDespacho> detalles) {
        this.detalles = detalles;
    }

    public DetalleDespacho getDetalleDespachoIn() {
        return detalleDespachoIn;
    }

    public void setDetalleDespachoIn(DetalleDespacho detalleDespachoIn) {
        this.detalleDespachoIn = detalleDespachoIn;
    }

    public List<DetalleDespacho> getDetallesSolicitados() {
        return detallesSolicitados;
    }

    public void setDetallesSolicitados(List<DetalleDespacho> detallesSolicitados) {
        this.detallesSolicitados = detallesSolicitados;
    }

}
