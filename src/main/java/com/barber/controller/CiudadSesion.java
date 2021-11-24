package com.barber.controller;

import com.barber.EJB.CiudadFacadeLocal;
import com.barber.model.Ciudad;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.primefaces.PrimeFaces;

@Named(value = "ciudadSesion")
@RequestScoped
public class CiudadSesion implements Serializable{
    
    @EJB
    private CiudadFacadeLocal ciudadFacadeLocal;
    
    @Inject
    private Ciudad ciudad;
    
    private List<Ciudad> ciudades;
    
    private Part archivoCarga;
    
    @PostConstruct
    public void init(){
        ciudades = ciudadFacadeLocal.findAll();
        ciudad = new Ciudad();
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
                        
                        Ciudad ciuObj = ciudadFacadeLocal.validarSiExiste(nextline[0]);
                        if (ciuObj == null) {
                            ciudadFacadeLocal.crearCiudad(nextline[0]);
                        } else {        
                            ciudadFacadeLocal.edit(ciuObj);
                        }

                    }
                    reader.close();
                    ciudades.clear();
                    ciudades = ciudadFacadeLocal.findAll();
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

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public List<Ciudad> getCiudades() {
        return ciudades;
    }

    public void setCiudades(List<Ciudad> ciudades) {
        this.ciudades = ciudades;
    }

    public Part getArchivoCarga() {
        return archivoCarga;
    }

    public void setArchivoCarga(Part archivoCarga) {
        this.archivoCarga = archivoCarga;
    }

}
