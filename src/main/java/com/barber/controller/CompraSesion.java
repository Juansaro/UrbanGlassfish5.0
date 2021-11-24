package com.barber.controller;

import com.barber.EJB.CompraFacadeLocal;
import com.barber.EJB.DetalleCompraFacadeLocal;
import com.barber.EJB.ProductoFacadeLocal;
import com.barber.EJB.ProveedorFacadeLocal;
import com.barber.model.Compra;
import com.barber.model.DetalleCompra;
import com.barber.model.Producto;
import com.barber.model.Proveedor;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named(value = "compraSesion")
@SessionScoped
public class CompraSesion implements Serializable {

    @EJB
    private CompraFacadeLocal compraFacadeLocal;
    @EJB
    private ProveedorFacadeLocal proveedorFacadeLocal;
    @EJB
    private DetalleCompraFacadeLocal detalleCompraFacadeLocal;
    @EJB
    private ProductoFacadeLocal ProductoFacadeLocal;

    private Compra compra;
    @Inject
    private Proveedor proveedor;
    @Inject
    private DetalleCompra detalleIn;
    @Inject
    private Producto productoIn;
    @Inject
    private UsuarioSesion u;

    private List<Compra> compras;
    private List<Proveedor> proveedores;
    private List<DetalleCompra> detalles;
    private List<DetalleCompra> detallesSolicitados;
    private List<Producto> productosTemporales;
    private List<Producto> productosSolicitados;
    private List<DetalleCompra> detallesTotales;
    private List<Producto> productos;
    // Create a HashMap object called capitalCities
    private Integer indiceTemporal;


    private Compra com;
    private Compra comTemporal;

    private float acumuladorCostoTotal;

    @PostConstruct
    public void init() {
        productosSolicitados = ProductoFacadeLocal.leerTodos();
        productosTemporales = new ArrayList<>();
        detalles = new ArrayList<>();
        detallesSolicitados = new ArrayList<>();
        proveedorFacadeLocal.leerTodos();
        compras = compraFacadeLocal.leerTodos();
        proveedores = proveedorFacadeLocal.leerTodos();
        productos = ProductoFacadeLocal.leerTodos();
        com = new Compra();
        com.setFechaSolicitud(ObtenerFechaActual());
        comTemporal = new Compra();
        indiceTemporal = 0;
    }

    public void guardarProveedorTemporal(Proveedor provIn) {
        proveedor = provIn;
    }

    public void guardarProductoTemporal(Producto prodIn) {
        productoIn = prodIn;
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

    public void guardarDetallesTemporales() {
        if (productoIn.getIdProducto() != null) {
            if (detalleIn.getCantidadSolicitada() != 0) {
                indiceTemporal++;
                detalleIn.setNumeroDetalle(indiceTemporal);
                detalleIn.setProductoIdProducto(productoIn);
                detalleIn.setFechaRecibido(ObtenerFechaActual());
                //Parseo de Integer a float para poder hacer el calculo de cantidades * costo del producto
                Integer parse = detalleIn.getCantidadSolicitada();
                float cantidadParseada = parse.floatValue();
                //Cálculo
                float acum = 0;
                acum = acum + cantidadParseada * detalleIn.getProductoIdProducto().getPrecio();
                detalleIn.setCostoTotal(acum);
                //Agregación
                acumuladorCostoTotal = acumuladorCostoTotal + acum;
                
                detalles.add(detalleIn);
                detalleIn = new DetalleCompra();
                productoIn = new Producto();
            } else {
                detalleIn = new DetalleCompra();
                productoIn = new Producto();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Escribe una cantidad", "Escribe una cantidad"));
            }
        } else {
            detalleIn = new DetalleCompra();
            productoIn = new Producto();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Ingresa un producto", "Ingresa un producto"));
        }

    }

    public void eliminarTemporal(DetalleCompra detIn) {
        //Cálculo
        float acum = 0;
        acum = acum + detIn.getCostoTotal();
        //System.out.println(mapaDetalles.get(detIn));
        //Agregación
        System.out.println(detIn.hashCode());
        detalles.remove(detIn);
        acumuladorCostoTotal = acumuladorCostoTotal - acum;
    }

    public void registrarCompra() {
        try {
            int contador = 0;
            com.setRecepcionista(u.getUsuLog());
            com.setNumeroProveedor(proveedor);
            compraFacadeLocal.create(com);
            int idCompra = com.getNumeroCompra();
            for (DetalleCompra it : detalles) {
                detalleIn = detalles.get(contador);
                detalleIn.setNumeroDetalle(null);//Se establece como null porque tiene llave autoincremental
                int cantidad = detalleIn.getCantidadSolicitada();
                Date fechaRecibido = detalleIn.getFechaRecibido();
                int pIn = detalleIn.getProductoIdProducto().getIdProducto();
                float costo = detalleIn.getCostoTotal();
                detalleIn.setNumeroCompra(com);
                detalleCompraFacadeLocal.registrarDetalleCompra(idCompra, cantidad, fechaRecibido, pIn, costo);
                contador++;
            }
            contador = 0;
            acumuladorCostoTotal = 0;
            indiceTemporal = 0;
            detalles = new ArrayList<>();
            detalleIn = new DetalleCompra();
            proveedor = new Proveedor();
            detalles = new ArrayList<>();
            compras = compraFacadeLocal.leerTodos();
        } catch (Exception e) {
        
        }
    }

    public void guardarTemporal(Compra c) {
        comTemporal = c;
    }

    public void editarCompra() {
        try {
            this.com.setNumeroProveedor(proveedor);
            compraFacadeLocal.edit(comTemporal);
            compras = compraFacadeLocal.findAll();
        } catch (Exception e) {
        }
    }

    public void eliminarCompra(Compra c) {
        try {
            compraFacadeLocal.remove(c);
            compras = compraFacadeLocal.findAll();
        } catch (Exception e) {
        }
    }
    
    public void consultarDetallesCompras(Compra c) {
        detallesSolicitados = detalleCompraFacadeLocal.leerCompras(c);
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public List<Compra> getCompras() {
        return compras;
    }

    public void setCompras(List<Compra> compras) {
        this.compras = compras;
    }

    public Compra getCom() {
        return com;
    }

    public void setCom(Compra com) {
        this.com = com;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public List<Proveedor> getProveedores() {
        return proveedores;
    }

    public void setProveedores(List<Proveedor> proveedores) {
        this.proveedores = proveedores;
    }

    public Compra getComTemporal() {
        return comTemporal;
    }

    public void setComTemporal(Compra comTemporal) {
        this.comTemporal = comTemporal;
    }

    public DetalleCompra getDetalleIn() {
        return detalleIn;
    }

    public void setDetalleIn(DetalleCompra detalleIn) {
        this.detalleIn = detalleIn;
    }

    public Producto getProductoIn() {
        return productoIn;
    }

    public void setProductoIn(Producto productoIn) {
        this.productoIn = productoIn;
    }

    public List<DetalleCompra> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCompra> detalles) {
        this.detalles = detalles;
    }

    public List<DetalleCompra> getDetallesTotales() {
        return detallesTotales;
    }

    public void setDetallesTotales(List<DetalleCompra> detallesTotales) {
        this.detallesTotales = detallesTotales;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public float getAcumuladorCostoTotal() {
        return acumuladorCostoTotal;
    }

    public void setAcumuladorCostoTotal(float acumuladorCostoTotal) {
        this.acumuladorCostoTotal = acumuladorCostoTotal;
    }

    public List<Producto> getProductosSolicitados() {
        return productosSolicitados;
    }

    public void setProductosSolicitados(List<Producto> productosSolicitados) {
        this.productosSolicitados = productosSolicitados;
    }

    public List<Producto> getProductosTemporales() {
        return productosTemporales;
    }

    public void setProductosTemporales(List<Producto> productosTemporales) {
        this.productosTemporales = productosTemporales;
    }

    public List<DetalleCompra> getDetallesSolicitados() {
        return detallesSolicitados;
    }

    public void setDetallesSolicitados(List<DetalleCompra> detallesSolicitados) {
        this.detallesSolicitados = detallesSolicitados;
    }

}
