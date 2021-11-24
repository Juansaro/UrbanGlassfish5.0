/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.controller;

import com.barber.EJB.BodegaFacadeLocal;
import com.barber.model.Bodega;
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
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
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

/**
 *
 * @author juan
 */
@Named(value = "bodegaSesion")
@SessionScoped
public class BodegaSesion implements Serializable{
    
    @EJB
    private BodegaFacadeLocal bodegaFacadeLocal;
    
    //recurso
    @Resource(lookup = "jdbc/defUrban")
    DataSource dataSource;
    
    
    private Bodega bodega;
    private List<Bodega> bodegas;
    
    private Bodega bod;
    private Bodega bodTemporal;
    
    private Part archivoCarga;
    
    @PostConstruct
    public void init(){
        bodegas = bodegaFacadeLocal.leerTodos();
        bodega = new Bodega();
        bod = new Bodega();
        bodTemporal = new Bodega();
        bodTemporal = new Bodega();
    }
    
    //Registrar bodega
    public void registrarBodega(){
        try {
            bod.setExistencias(0);
            bodegaFacadeLocal.create(bod);
            bod = new Bodega();
            bodegas = bodegaFacadeLocal.leerTodos();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bodega registrada", "Bodega registrada"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de registro", "Error de registro"));
        }
    }
    //Recupera datos del bodega al cual se va a editar
     public void guardarTemporal(Bodega b) {
        bodTemporal = b;
    }

    //Editar bodega (En el modal)
    public void editarBodega() {
        try {
            bodegaFacadeLocal.edit(bodTemporal);
            bodTemporal = new Bodega();
            bodegas = bodegaFacadeLocal.leerTodos();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bodega editada", "Bodega editada"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de edición", "Error de edición"));
        }
    }
    //Eliminar
    public void eliminarBodega(Bodega b){
        try{
            bodegaFacadeLocal.remove(b);
            bodegas = bodegaFacadeLocal.leerTodos();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bodega eliminada", "Bodega eliminada"));
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de eliminación", "Error de eliminación"));
        }
    }
    
    public void cargarInicialDatos() {
        if (archivoCarga != null) {
            if (archivoCarga.getSize()> 700000) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bodega eliminada", "Bodega eliminada"));
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
                        
                        Bodega bogObj = bodegaFacadeLocal.validarSiExiste(nextline[0]);
                        if (bogObj == null) {
                            bodegaFacadeLocal.crearBodega(nextline[0], Integer.parseInt(nextline[1]));
                        } else {        
                            bodegaFacadeLocal.edit(bogObj);
                        }

                    }
                    reader.close();
                    bodegas.clear();
                    bodegas = bodegaFacadeLocal.findAll();
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
        File jasper = new File(context.getRealPath("/reportes/bodega.jasper"));
        try {
            JasperPrint jp = JasperFillManager.fillReport(jasper.getPath(), new HashMap(), dataSource.getConnection());
            switch (tipoArchivo) {
                case "pdf":
                    response.setContentType("application/pdf");
                    response.addHeader("Content-disposition", "attachment; filename=Lista bodega.pdf");
                    OutputStream os = response.getOutputStream();
                    JasperExportManager.exportReportToPdfStream(jp, os);
                    os.flush();
                    os.close();
                    break;

                case "xlsx":
                    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                    response.addHeader("Content-disposition", "attachment; filename=Lista bodega.xlsx");

                    JRXlsxExporter exporter = new JRXlsxExporter(); // initialize exporter 
                    exporter.setExporterInput(new SimpleExporterInput(jp)); // set compiled report as input
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

                    SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                    configuration.setOnePagePerSheet(true); // setup configuration
                    configuration.setDetectCellType(true);
                    configuration.setSheetNames(new String[]{"bodega"});
                    exporter.setConfiguration(configuration); // set configuration    
                    exporter.exportReport();
                    break;

                case "docx":
                    response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                    response.addHeader("Content-disposition", "attachment; filename=Lista bodega.docx");

                    JRDocxExporter exporterDoc = new JRDocxExporter(); // initialize exporter 
                    exporterDoc.setExporterInput(new SimpleExporterInput(jp)); // set compiled report as input
                    exporterDoc.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

                    SimpleDocxExporterConfiguration configurationDoc = new SimpleDocxExporterConfiguration();
                    configurationDoc.setMetadataAuthor("Urban barber shop."); // setup configuration
                    configurationDoc.setMetadataTitle("Reporte de bodega");
                    configurationDoc.setMetadataSubject("Listado de bodega");

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
    
    //Getters y Setters

    public Bodega getBodega() {
        return bodega;
    }

    public void setBodega(Bodega bodega) {
        this.bodega = bodega;
    }

    public List<Bodega> getBodegas() {
        return bodegas;
    }

    public void setBodegas(List<Bodega> bodegas) {
        this.bodegas = bodegas;
    }

    public Bodega getBod() {
        return bod;
    }

    public void setBod(Bodega bod) {
        this.bod = bod;
    }

    public Bodega getBodTemporal() {
        return bodTemporal;
    }

    public void setBodTemporal(Bodega bodTemporal) {
        this.bodTemporal = bodTemporal;
    }

    public Part getArchivoCarga() {
        return archivoCarga;
    }

    public void setArchivoCarga(Part archivoCarga) {
        this.archivoCarga = archivoCarga;
    }
    
}
