/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.controller;

import com.barber.EJB.BodegaFacadeLocal;
import com.barber.EJB.ProductoFacadeLocal;
import com.barber.model.Bodega;
import com.barber.model.Producto;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.primefaces.PrimeFaces;
import org.primefaces.shaded.commons.io.FilenameUtils;

/**
 *
 * @author juan
 */
@Named(value = "productoView")
@ViewScoped
public class ProductoView implements Serializable{
    
    @EJB
    private ProductoFacadeLocal productoFacadeLocal;
    @EJB
    private BodegaFacadeLocal bodegaFacadeLocal;
    
    private Producto producto;
    
    //Format
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private Part archivoFoto;
    
    @Inject
    private Bodega bodega;
    
    private List<Producto> productos;
    private List<Bodega> bodegas;
    
    private Producto pro;
    private Producto proTemporal;
    private Bodega bodTemporal;
    private Producto proFoto;
    
    @PostConstruct
    private void init(){
        productos = productoFacadeLocal.leerTodos();
        bodegas = bodegaFacadeLocal.leerTodos();
        pro = new Producto();
        producto = new Producto();
        proTemporal = new Producto();
        bodTemporal = new Bodega();
        proFoto = new Producto();
    }
    
    //Registrar producto
    public void registrarProducto(){
        try {
            pro.setBodegaIdBodega(bodega);
            pro.setCantidad(0);
            productoFacadeLocal.create(pro);
            productos = productoFacadeLocal.leerTodos();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Producto registrado", "Producto registrado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error de registro", "Error de registro"));
        }
    }
    
    //Recupera datos del producto al cual se va a editar
     public void guardarTemporal(Producto p) {
        proTemporal = p;
    }

    //Editar usuario (En el modal)
    public void editarProducto() {
        try {
            this.proTemporal.setBodegaIdBodega(bodega);
            productoFacadeLocal.edit(proTemporal);
            productos = productoFacadeLocal.findAll();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Producto editado", "Producto editado"));
        } catch (Exception e) {
            
        }
        //return null;
    }
    
    //Eliminar
    public void eliminarProducto(Producto p){
        try{
            this.productoFacadeLocal.remove(p);
            this.producto = new Producto();
            productos = productoFacadeLocal.findAll();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Producto eliminado", "Producto eliminado"));
        }catch(Exception e){
        }
    }
    
    public void guardarFotoTemporal(Producto p){
        proFoto = p;
    }
    
    public void cargarFotoProducto() {
        if (archivoFoto != null) {
            if (archivoFoto.getSize() > 70000) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "El archivo es muy grande", "El archivo es muy grande"));
            } else if (archivoFoto.getContentType().equalsIgnoreCase("image/png") || archivoFoto.getContentType().equalsIgnoreCase("image/jpeg")) {
                try (InputStream is = archivoFoto.getInputStream()) {
                    File carpeta = new File("C:\\ubs\\productos\\fotoProductos");
                    //Comprobación si no exite
                    if (!carpeta.exists()) {
                        //Creación de carpeta
                        carpeta.mkdirs();
                    }
                    Calendar hoy = Calendar.getInstance();
                    //Guardar la fecha con la fecha exacta
                    String nuevoNombre = sdf.format(hoy.getTime()) + ".";
                    //Recargue (concatenación) filesnameutils me permite traer la extención
                    nuevoNombre += FilenameUtils.getExtension(archivoFoto.getSubmittedFileName());
                    //**parámetros** Fuente , salida, sobreescribir
                    Files.copy(is, (new File(carpeta, nuevoNombre)).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    //Recoger la imagen y actualizar el usuario con el nombre de la imagen
                    proFoto.setProductoFoto(nuevoNombre);
                    productoFacadeLocal.edit(proFoto);
                    //Resetear
                    PrimeFaces.current().executeScript("document.getElementById('resetform').click()");
                    proFoto = new Producto();
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/UrbanBarberShop/faces/recepcionista/consultarProducto.xhtml");
                } catch (Exception e) {
                    System.out.println("error");
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "El formato no esta permitido", "El formato no esta permitido"));
            }
        } else {
            //Mensaje
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error de carga", "Error de carga"));
        }
    }
    
    //Getters y Setters
    
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Bodega getBodega() {
        return bodega;
    }

    public void setBodega(Bodega bodega) {
        this.bodega = bodega;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public List<Bodega> getBodegas() {
        return bodegas;
    }

    public void setBodegas(List<Bodega> bodegas) {
        this.bodegas = bodegas;
    }

    public Producto getPro() {
        return pro;
    }

    public void setPro(Producto pro) {
        this.pro = pro;
    }

    public Producto getProTemporal() {
        return proTemporal;
    }

    public void setProTemporal(Producto proTemporal) {
        this.proTemporal = proTemporal;
    }

    public Bodega getBodTemporal() {
        return bodTemporal;
    }

    public void setBodTemporal(Bodega bodTemporal) {
        this.bodTemporal = bodTemporal;
    }

    public Part getArchivoFoto() {
        return archivoFoto;
    }

    public void setArchivoFoto(Part archivoFoto) {
        this.archivoFoto = archivoFoto;
    }

    public Producto getProFoto() {
        return proFoto;
    }

    public void setProFoto(Producto proFoto) {
        this.proFoto = proFoto;
    }
    
}

