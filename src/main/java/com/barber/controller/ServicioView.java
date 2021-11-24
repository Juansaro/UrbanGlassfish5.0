/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.controller;

import com.barber.EJB.ServicioFacadeLocal;
import com.barber.model.Servicio;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleDocxExporterConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.primefaces.PrimeFaces;
import org.primefaces.shaded.commons.io.FilenameUtils;

@Named(value = "servicioView")
@ViewScoped
public class ServicioView implements Serializable{
    //Conexión de FacadeLocal's
    @EJB
    private ServicioFacadeLocal servicioFacadeLocal;
    
    @Resource(lookup = "jdbc/defUrban")
    DataSource dataSource;
    
    //Format
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private Part archivoFoto;
    
    private Servicio servicio;
    private List<Servicio> servicios;
    
    private Servicio ser;
    private Servicio serTemporal;
    private Part archivoCarga;
    private Servicio serFot;
    
    @PostConstruct
    public void init(){
        servicios = servicioFacadeLocal.leerTodos();
        ser = new Servicio();
        servicio = new Servicio();
        serTemporal = new Servicio();
        serFot = new Servicio();
    }
    
    //Registrar servicio
    public void registrarServicio(){
        try {
            servicioFacadeLocal.create(ser);
            ser = new Servicio();
            servicios = servicioFacadeLocal.leerTodos();
            //FacesContext.getCurrentInstance().getExternalContext().redirect("/UrbanBarberShop/faces/recepcionista/consultarServicio.xhtml");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Servicio registrado", "Servicio registrado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error de registro", "Error de registro"));
        }
    }
   
    //Recupera datos del servico al cual se va a editar
     public void guardarTemporal(Servicio s) {
        serTemporal = s;
    }
     
    //Editar
    public void editarServicio(){
        try{
            servicioFacadeLocal.edit(serTemporal);
            serTemporal = new Servicio();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Servicio editado", "Servicio editado"));
        }catch(Exception e){
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error al editar", "Error al editar"));
        }
    }
    
    //Eliminar
    public void eliminarServicio(Servicio s){
        try{
            this.servicioFacadeLocal.remove(s);
            this.servicio = new Servicio();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Servicio eliminado", "Servicio eliminado"));
            servicios = servicioFacadeLocal.findAll();
        }catch(Exception e){
        }
    }
    
    //Recupera datos del servicio al cual se va a editar
     public void prepararEditar(Servicio serIn) {
        serTemporal = serIn;
    }
     
    public void cargarInicialDatos() {
        if (archivoCarga != null) {
            if (archivoCarga.getSize() > 700000) {
                PrimeFaces.current().executeScript("Swal.fire({"
                        + "  title: 'El archivo !',"
                        + "  text: 'No se puede cargar por el tamaño !!!',"
                        + "  icon: 'error',"
                        + "  confirmButtonText: 'Ok'"
                        + "})");
            } else if (archivoCarga.getContentType().equalsIgnoreCase("application/vnd.ms-excel")) {

                try (InputStream is = archivoCarga.getInputStream()) {
                    File carpeta = new File("C:\\ubs\\recepcionista\\archivos");
                    if (!carpeta.exists()) {
                        carpeta.mkdirs();
                    }
                    Files.copy(is, (new File(carpeta, archivoCarga.getSubmittedFileName())).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    CSVParser conPuntoyComa = new CSVParserBuilder().withSeparator(';').build();
                    CSVReader reader = new CSVReaderBuilder(new FileReader("C:\\ubs\\recepcionista\\archivos\\" + archivoCarga.getSubmittedFileName())).withCSVParser(conPuntoyComa).build();
                    String[] nextline;
                    while ((nextline = reader.readNext()) != null) {
                        
                        Servicio bogObj = servicioFacadeLocal.validarSiExiste(nextline[0]);
                        if (bogObj == null) {
                            servicioFacadeLocal.crearServicio(nextline[0], nextline[1], Float.parseFloat(nextline[2]));
                        } else {        
                            servicioFacadeLocal.edit(bogObj);
                        }

                    }
                    reader.close();
                    servicios = servicioFacadeLocal.findAll();
                } catch (Exception e) {
                    PrimeFaces.current().executeScript("Swal.fire({"
                            + "  title: 'Problemas !',"
                            + "  text: 'No se puede realizar esta accion',"
                            + "  icon: 'error',"
                            + "  confirmButtonText: 'Ok'"
                            + "})");
                }
            } else {
                PrimeFaces.current().executeScript("Swal.fire({"
                        + "  title: 'El archivo !',"
                        + "  text: 'No es una imagen .png o .jpeg !!!',"
                        + "  icon: 'error',"
                        + "  confirmButtonText: 'Ok'"
                        + "})");
            }

        } else {
            PrimeFaces.current().executeScript("Swal.fire({"
                    + "  title: 'Problemas !',"
                    + "  text: 'No se puede realizar esta accion',"
                    + "  icon: 'error',"
                    + "  confirmButtonText: 'Ok'"
                    + "})");
        }

        PrimeFaces.current().executeScript("document.getElementById('resetform').click()");

    }

    public void generarArchivo(String tipoArchivo) throws JRException, IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext context = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) context.getResponse();
        File jasper = new File(context.getRealPath("/reportes/servicios.jasper"));
        try {
            JasperPrint jp = JasperFillManager.fillReport(jasper.getPath(), new HashMap(), dataSource.getConnection());
            switch (tipoArchivo) {
                case "pdf":
                    response.setContentType("application/pdf");
                    response.addHeader("Content-disposition", "attachment; filename=Lista servicios.pdf");
                    OutputStream os = response.getOutputStream();
                    JasperExportManager.exportReportToPdfStream(jp, os);
                    os.flush();
                    os.close();
                    break;

                case "xlsx":
                    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                    response.addHeader("Content-disposition", "attachment; filename=Lista servicios.xlsx");

                    JRXlsxExporter exporter = new JRXlsxExporter(); // initialize exporter 
                    exporter.setExporterInput(new SimpleExporterInput(jp)); // set compiled report as input
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

                    SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                    configuration.setOnePagePerSheet(true); // setup configuration
                    configuration.setDetectCellType(true);
                    configuration.setSheetNames(new String[]{"servicios"});
                    exporter.setConfiguration(configuration); // set configuration    
                    exporter.exportReport();
                    break;

                case "docx":
                    response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                    response.addHeader("Content-disposition", "attachment; filename=Lista servicios.docx");

                    JRDocxExporter exporterDoc = new JRDocxExporter(); // initialize exporter 
                    exporterDoc.setExporterInput(new SimpleExporterInput(jp)); // set compiled report as input
                    exporterDoc.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

                    SimpleDocxExporterConfiguration configurationDoc = new SimpleDocxExporterConfiguration();
                    configurationDoc.setMetadataAuthor("Urban barber shop."); // setup configuration
                    configurationDoc.setMetadataTitle("Reporte de servicios");
                    configurationDoc.setMetadataSubject("Listado de servicios");

                    exporterDoc.setConfiguration(configurationDoc); // set configuration    
                    exporterDoc.exportReport();
                    break;

                default:
                    System.err.println(" No se encontro este caso :: CategoriaView::generarArchivo");
                    break;

            }
            facesContext.responseComplete();

        } catch (SQLException ex) {
            //Logger.getLogger(CategoriaView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void guardarFotoTemporal(Servicio s){
        serFot = s;
    }
    
    public void cargarFotoServicio() {
        if (archivoFoto != null) {
            if (archivoFoto.getSize() > 70000) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "El archivo es muy grande", "El archivo es muy grande"));
            } else if (archivoFoto.getContentType().equalsIgnoreCase("image/png") || archivoFoto.getContentType().equalsIgnoreCase("image/jpeg")) {
                try (InputStream is = archivoFoto.getInputStream()) {
                    File carpeta = new File("C:\\ubs\\servicios\\fotoServicios");
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
                    serFot.setServicioFoto(nuevoNombre);
                    servicioFacadeLocal.edit(serFot);
                    //Resetear
                    PrimeFaces.current().executeScript("document.getElementById('resetform').click()");
                    serFot = new Servicio();
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/UrbanBarberShop/faces/recepcionista/consultarServicio.xhtml");
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
    
    public Servicio getSer() {
        return ser;
    }

    public void setSer(Servicio ser) {
        this.ser = ser;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public Servicio getSerTemporal() {
        return serTemporal;
    }

    public void setSerTemporal(Servicio serTemporal) {
        this.serTemporal = serTemporal;
    }

    public Part getArchivoCarga() {
        return archivoCarga;
    }

    public void setArchivoCarga(Part archivoCarga) {
        this.archivoCarga = archivoCarga;
    }

    public Part getArchivoFoto() {
        return archivoFoto;
    }

    public void setArchivoFoto(Part archivoFoto) {
        this.archivoFoto = archivoFoto;
    }

    public Servicio getSerFot() {
        return serFot;
    }

    public void setSerFot(Servicio serFot) {
        this.serFot = serFot;
    }
    
}

