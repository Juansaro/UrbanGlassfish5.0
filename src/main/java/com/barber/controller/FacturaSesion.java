/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.controller;

import com.barber.EJB.CitaFacadeLocal;
import com.barber.EJB.FacturaFacadeLocal;
import com.barber.model.Cita;
import static com.barber.model.Cita_.estadoAsignacionIdEstadoAsignacion;
import com.barber.model.EstadoAsignacion;
import com.barber.model.Factura;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
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

/**
 *
 * @author juan
 */
@Named(value = "facturaSesion")
@SessionScoped
public class FacturaSesion implements Serializable{
    
    @EJB
    private FacturaFacadeLocal facturaFacadeLocal;
    @EJB
    private CitaFacadeLocal citaFacadeLocal;
    
    @Resource(lookup = "jdbc/defUrban")
    DataSource dataSource;
    
    private Factura factura;
    @Inject
    private Cita cita;
    @Inject
    private EstadoAsignacion estadoFacturado;
    @Inject
    private UsuarioSesion usu;
    
    private List<Factura> facturas;
    private List<Cita> citas;
    
    private Factura fac = new Factura();
    private Factura facTemporal = new Factura();
    
    @PostConstruct
    public void init(){
        estadoFacturado = new EstadoAsignacion();
        estadoFacturado.setIdEstadoAsignacion(6);
        citas = citaFacadeLocal.findAll();
        facturas = facturaFacadeLocal.findAll();
        factura = new Factura();
    }
    
    //Registrar Factura
    public void registrarFactura(Cita c){
        try {
            c.setEstadoAsignacionIdEstadoAsignacion(estadoFacturado);
            fac.setCitaIdCita(c);
            fac.setCosto(c.getCosto());
            facturaFacadeLocal.create(fac);
            citaFacadeLocal.edit(c);
            
            facturas = facturaFacadeLocal.findAll();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Factura registrada", "Factura registrada"));
            FacesContext.getCurrentInstance().getExternalContext().redirect("/UrbanBarberShop/faces/recepcionista/consultarFactura.xhtml");
        } catch (IOException e) {
        }
    }
    
    //Guardar temporal
    public void guardarTemporal(Factura f){
        facTemporal = f;
    }
    
    //Editar factura
    public String editarFactura(){
        try {
            this.facTemporal.setCitaIdCita(cita);
            facturaFacadeLocal.edit(facTemporal);
            facTemporal = new Factura();
            cita = new Cita();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Factura editada", "Factura editada"));
            return "/RecepConsultarFactura.xhtml";
        } catch (Exception e) {
        }
        return null;
    }
    
    //Preparar página para eliminar
    public String prepararEliminar(){
        facturas = facturaFacadeLocal.findAll();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Factura borrada", "Factura borrada"));
        return "/RecepConsultarUsuarios.xhtml";
    }
    //Eliminar
    public void eliminarFactura(Factura f){
        try{
            this.facturaFacadeLocal.remove(f);
            this.factura = new Factura();
            //Colocar prepararEliminar()
            prepararEliminar();
        }catch(Exception e){
        }
    }
    
    public void generarArchivoFactura(String tipoArchivo) throws JRException, IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext context = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) context.getResponse();
        String nombreReporte = "";
        String nombreReporteDescarga = "";

        try {

            Map parametros = new HashMap();
            Integer idTemporal = facTemporal.getIdFactura();
            parametros.put("facturaIn", idTemporal);
            nombreReporte = "factura";
            
            File jasper = new File(context.getRealPath("/reportes/" + nombreReporte + ".jasper"));

            JasperPrint jp = JasperFillManager.fillReport(jasper.getPath(), parametros, dataSource.getConnection());
            switch (tipoArchivo) {
                case "pdf":
                    response.setContentType("application/pdf");
                    response.addHeader("Content-disposition", "attachment; filename=" + nombreReporte + ".pdf");
                    OutputStream os = response.getOutputStream();
                    JasperExportManager.exportReportToPdfStream(jp, os);
                    os.flush();
                    os.close();
                    break;

                case "xlsx":
                    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                    response.addHeader("Content-disposition", "attachment; filename=factura.xlsx");

                    JRXlsxExporter exporter = new JRXlsxExporter(); // initialize exporter 
                    exporter.setExporterInput(new SimpleExporterInput(jp)); // set compiled report as input
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

                    SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                    configuration.setOnePagePerSheet(true); // setup configuration
                    configuration.setDetectCellType(true);
                    configuration.setSheetNames(new String[]{"factura"});
                    exporter.setConfiguration(configuration); // set configuration    
                    exporter.exportReport();
                    break;

                case "docx":
                    response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                    response.addHeader("Content-disposition", "attachment; filename=factura.docx");

                    JRDocxExporter exporterDoc = new JRDocxExporter(); // initialize exporter 
                    exporterDoc.setExporterInput(new SimpleExporterInput(jp)); // set compiled report as input
                    exporterDoc.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

                    SimpleDocxExporterConfiguration configurationDoc = new SimpleDocxExporterConfiguration();
                    configurationDoc.setMetadataAuthor("Urban barber shop."); // setup configuration
                    configurationDoc.setMetadataTitle("Reporte de factura");
                    configurationDoc.setMetadataSubject("Factura");

                    exporterDoc.setConfiguration(configurationDoc); // set configuration    
                    exporterDoc.exportReport();
                    break;

                default:
                    System.err.println(" No se encontro este caso :: FacturaSesion::generarArchivo");
                    break;

            }
            idTemporal = 0;
            facesContext.responseComplete();
            facTemporal = new Factura();
        } catch (SQLException ex) {
            Logger.getLogger(FacturaSesion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<Factura> leerFacturasCliente(){
        return facturaFacadeLocal.leerFacturasCliente(usu.getUsuLog());
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }

    public Factura getFac() {
        return fac;
    }

    public void setFac(Factura fac) {
        this.fac = fac;
    }

    public Factura getFacTemporal() {
        return facTemporal;
    }

    public void setFacTemporal(Factura facTemporal) {
        this.facTemporal = facTemporal;
    }
    
}

