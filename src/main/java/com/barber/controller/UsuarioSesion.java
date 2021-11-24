/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.controller;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.barber.EJB.CiudadFacadeLocal;
import com.barber.EJB.TipoIdentificacionFacadeLocal;
import com.barber.EJB.TipoRolFacadeLocal;
import com.barber.EJB.TipoTelefonoFacadeLocal;
import com.barber.EJB.UsuarioFacadeLocal;
import com.barber.model.Ciudad;
import com.barber.model.TipoIdentificacion;
import com.barber.model.TipoRol;
import com.barber.model.TipoTelefono;
import com.barber.model.Usuario;
import com.barber.utilidades.Mail;
import com.barber.utilidades.envioMasivoServicios;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.primefaces.PrimeFaces;
import org.primefaces.shaded.commons.io.FilenameUtils;

/**
 *
 * @author juan
 */
@Named(value = "usuarioSesion")
@SessionScoped
public class UsuarioSesion implements Serializable {

    //Conexión con FacadeLocal's
    //Es un punto de conexión a la base de datos
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    //LLaves FK en FacadeLocal
    @EJB
    private CiudadFacadeLocal ciudadFacadeLocal;
    @EJB
    private TipoRolFacadeLocal rolFacadeLocal;
    @EJB
    private TipoTelefonoFacadeLocal tipoTelefonoFacadeLocal;
    @EJB
    private TipoIdentificacionFacadeLocal tipoIdentificacionFacadeLocal;

    private Usuario usuario;
    //Usar esta estructura para las FK
    @Inject
    private Ciudad ciudad;
    @Inject
    private TipoRol tipoRol;
    @Inject
    private TipoTelefono tipoTelefono;
    @Inject
    private TipoIdentificacion tipoIdentificacion;
    @Inject
    private RolRequest r;

    //Lista local
    private List<Usuario> usuarios;
    //Usar esta estructura para las FK (Listar)
    private List<TipoIdentificacion> tipoIdentificaciones;
    private List<TipoTelefono> tipoTelefonos;
    private List<TipoRol> roles;
    private List<Ciudad> ciudades;
    private List<String> correos;

    //Atributos de clase
    private String correoUsuario;
    private String contrasena;
    private String correoIn;
    private String claveIn;
    //Archivos (carga)
    private Part archivoFoto;
    private Part archivoCarga;
    //Format
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    //------>Instacías de sesión<------
    private Usuario usuReg;
    private Usuario usuLog;
    private Usuario usuTemporal;
    //------>Instacías de sesión<------

    //Este código me permite mostrar los datos en un select de un formulario (Me lista los datos en la vista)
    @PostConstruct
    public void init() {
        //Usar esto para la estructura local
        usuarios = usuarioFacadeLocal.leerTodos();
        //Usar esta estructura para las FK
        ciudades = ciudadFacadeLocal.findAll();
        roles = rolFacadeLocal.findAll();
        tipoTelefonos = tipoTelefonoFacadeLocal.findAll();
        tipoIdentificaciones = tipoIdentificacionFacadeLocal.findAll();
        usuReg = new Usuario();
        usuLog = new Usuario();
        usuTemporal = new Usuario();
        //Limpiar un formulario
        usuario = new Usuario();
    }

    //Login
    public void validarUsuario() throws IOException {
        usuLog = usuarioFacadeLocal.encontrarUsuarioCorreo(correoUsuario);
        //rol = ejbFacade.encontrarRol(numeroRol);
        if (usuLog != null) {
            if (usuLog.getCorreo().equals(correoUsuario)) {
                if (usuLog.getContrasena().equals(contrasena)) {
                    switch (usuLog.getTipoRolNumeroRol().toString()) {
                        case "Recepcionista":
                            FacesContext fc = FacesContext.getCurrentInstance();
                            fc.getExternalContext().redirect("/UrbanBarberShop/faces/recepcionista/index.xhtml");
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Bienvenido!", "Bienvenido!"));
                            ;
                        case "Cliente":
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("correo", usuLog);
                            FacesContext.getCurrentInstance().getExternalContext().redirect("/UrbanBarberShop/faces/cliente/index.xhtml");
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Bienvenido!", "Bienvenido!"));
                            break;
                        case "Barbero":
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("correo", usuLog);
                            FacesContext.getCurrentInstance().getExternalContext().redirect("/UrbanBarberShop/faces/barbero/index.xhtml");
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Bienvenido!", "Bienvenido!"));
                            break;
                        default:
                            break;
                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "No dispones de un rol en el sistema", "No dispones de un rol en el sistema"));
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Clave incorrecta", "Clave incorrecta"));
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "El usuario no existe", "El usuario no existe"));
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "El usuario no existe", "El usuario no existe"));
    }

    public void validarUsuarioSesion() throws IOException {
        if (usuLog == null || usuLog.getCorreo() == null) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().invalidateSession();
            fc.getExternalContext().redirect("../index.xhtml");
        }
    }

    //Registrar usuario
    public void registrarUsuario() {
        try {
            usuTemporal = usuarioFacadeLocal.encontrarUsuarioCorreo(usuReg.getCorreo());
            if (usuTemporal == null) {
                //Usar esta estructura para las FK
                this.usuReg.setCiudadNumeroCiudad(ciudad);
                //Asignación del CLIENTE por inyeccion de despendencia
                this.usuReg.setTipoRolNumeroRol(r.asignacionRolCliente());
                this.usuReg.setTipoIdentificacionIdTipoIdentificacion(tipoIdentificacion);
                this.usuReg.setTipoTelefonoNumeroTipoTelefono(tipoTelefono);
                //Principal
                usuarioFacadeLocal.create(usuReg);
                //Limpiar formulario de registro
                usuReg = new Usuario();
                usuTemporal = new Usuario();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario registrado", "Usuario registrado"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Lo sentimos ya está registrado ese correo", "Lo sentimos pero está registrado ese correo"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al registrar usuario", "Error al registrar usuario"));
        }
    }

    //Cerrar sesion
    public void cerrarSesion() throws IOException {
        //Se destruye la información almacenada en el FacesContext (Dentro del método validarUsuario())
        usuLog = null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Has cerrado sesión", "Has cerrado sesión"));
        FacesContext.getCurrentInstance().getExternalContext().redirect("../index.xhtml");
    }

    //Carga inicial de datos de usuario
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
                    File carpeta = new File("C:\\cdi\\administrador\\archivos");
                    if (!carpeta.exists()) {
                        carpeta.mkdirs();
                    }
                    Files.copy(is, (new File(carpeta, archivoCarga.getSubmittedFileName())).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    CSVParser conPuntoyComa = new CSVParserBuilder().withSeparator(';').build();
                    CSVReader reader = new CSVReaderBuilder(new FileReader("C:\\cdi\\administrador\\archivos\\" + archivoCarga.getSubmittedFileName())).withCSVParser(conPuntoyComa).build();
                    String[] nextline;
                    while ((nextline = reader.readNext()) != null) {

                        Usuario usuObj = usuarioFacadeLocal.validarSiExiste(nextline[3]);
                        if (usuObj == null) {
                            usuarioFacadeLocal.crearUsuario(nextline[0], nextline[1], nextline[2], nextline[3], Integer.parseInt(nextline[4]), Integer.parseInt(nextline[5]), Integer.parseInt(nextline[6]), nextline[7], Integer.parseInt(nextline[8]), nextline[9]);
                        } else {
                            usuarioFacadeLocal.edit(usuObj);
                        }
                    }
                    reader.close();
                    usuarios.clear();
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
        usuarios = usuarioFacadeLocal.findAll();
        PrimeFaces.current().executeScript("document.getElementById('resetform').click()");

    }

    //Recupera datos del usuario al cual se va a editar
    public void guardarTemporal(Usuario u) {
        usuTemporal = u;
    }

    //Editar usuario (En el modal)
    public void editarUsuario() {
        try {
            //usuTemporal sirve para el ciclo de vida de SOLO la edición
            //Estructura FK'S
            this.usuTemporal.setCiudadNumeroCiudad(ciudad);
            this.usuTemporal.setTipoRolNumeroRol(tipoRol);
            this.usuTemporal.setTipoIdentificacionIdTipoIdentificacion(tipoIdentificacion);
            this.usuTemporal.setTipoTelefonoNumeroTipoTelefono(tipoTelefono);
            //El parámetro que usea para editar es usuTemporal
            usuarioFacadeLocal.edit(usuTemporal);
            //Limpieza local
            usuTemporal = new Usuario();
            //Limpieza de las FK'S
            tipoRol = new TipoRol();
            usuarios = usuarioFacadeLocal.findAll();
            //Mensaje
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario modificado", "Usuario modificado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error de edición", "Error de edición"));
        }
    }

    public void editarCliente() {
        try {
            usuLog.setCiudadNumeroCiudad(ciudad);
            usuLog.setTipoIdentificacionIdTipoIdentificacion(tipoIdentificacion);
            usuLog.setTipoTelefonoNumeroTipoTelefono(tipoTelefono);
            //El parámetro que usea para editar es usuTemporal
            usuarioFacadeLocal.edit(usuLog);
            ciudad = new Ciudad();
            //Mensaje
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario modificado", "Usuario modificado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error de edición", "Error de edición"));
        }
    }

    //Eliminar
    public void eliminarUsuario(Usuario u) {
        try {
            this.usuarioFacadeLocal.remove(u);
            //Colocar prepararEliminar()
            usuarios = usuarioFacadeLocal.findAll();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario eliminado", "Usuario eliminado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error al eliminar", "Error al eliminar"));
        }
    }

    public void recuperarClave() {
        try {
            usuReg = usuarioFacadeLocal.recuperarClave(correoIn);
            if (usuReg != null) {
                Mail.recuperarClaves(usuReg.getNombre(), usuReg.getCorreo(), usuReg.getContrasena());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correo enviado", "Correo enviado"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "No se encontró el correo", "No se encontró el correo"));
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exepción de correo", "Exepción de correo"));
        }

    }

    public void avisoServicios() {
        try {
            correos = usuarioFacadeLocal.leerCorreosClientes();

            if (correos != null) {
                envioMasivoServicios.recuperarCliente(correos);
            } else {
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Fallo al enviar", "Fallo al enviar"));
        }
    }

    //Carga de foto de perfil
    public void cargarFotoPerfil() {
        if (archivoFoto != null) {
            if (archivoFoto.getSize() > 70000) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "El archivo es muy grande", "El archivo es muy grande"));
            } else if (archivoFoto.getContentType().equalsIgnoreCase("image/png") || archivoFoto.getContentType().equalsIgnoreCase("image/jpeg")) {
                try (InputStream is = archivoFoto.getInputStream()) {
                    File carpeta = new File("C:\\ubs\\usuarios\\fotoperfil");
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
                    usuLog.setUsuFoto(nuevoNombre);
                    usuarioFacadeLocal.edit(usuLog);
                    //Resetear
                    PrimeFaces.current().executeScript("document.getElementById('resetform').click()");
                    redireccionIndex(usuLog);
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error de carga", "Error de carga"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "El formato no esta permitido", "El formato no esta permitido"));
            }
        } else {
            //Mensaje
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error de carga", "Error de carga"));
        }
    }

    private void redireccionIndex(Usuario usuIn) {
        try {
            switch (usuLog.getTipoRolNumeroRol().toString()) {
                case "Recepcionista":
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/UrbanBarberShop/faces/recepcionista/index.xhtml");
                    break;
                case "Cliente":
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/UrbanBarberShop/faces/cliente/index.xhtml");
                    break;
                case "Barbero":
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/UrbanBarberShop/faces/barbero/index.xhtml");
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error de redirección", "Error de carga"));
        }
    }

    //Getters y Setters
    public Usuario getUsuReg() {
        return usuReg;
    }

    public void setUsuReg(Usuario usuReg) {
        this.usuReg = usuReg;
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

    public TipoRol getTipoRol() {
        return tipoRol;
    }

    public void setTipoRol(TipoRol tipoRol) {
        this.tipoRol = tipoRol;
    }

    public TipoTelefono getTipoTelefono() {
        return tipoTelefono;
    }

    public void setTipoTelefono(TipoTelefono tipoTelefono) {
        this.tipoTelefono = tipoTelefono;
    }

    public TipoIdentificacion getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(TipoIdentificacion tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public List<TipoIdentificacion> getTipoIdentificaciones() {
        return tipoIdentificaciones;
    }

    public void setTipoIdentificaciones(List<TipoIdentificacion> tipoIdentificaciones) {
        this.tipoIdentificaciones = tipoIdentificaciones;
    }

    public List<TipoTelefono> getTipoTelefonos() {
        return tipoTelefonos;
    }

    public void setTipoTelefonos(List<TipoTelefono> tipoTelefonos) {
        this.tipoTelefonos = tipoTelefonos;
    }

    public List<TipoRol> getRoles() {
        return roles;
    }

    public void setRoles(List<TipoRol> roles) {
        this.roles = roles;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuLog() {
        return usuLog;
    }

    public void setUsuLog(Usuario usuLog) {
        this.usuLog = usuLog;
    }

    public Usuario getUsuTemporal() {
        return usuTemporal;
    }

    public void setUsuTemporal(Usuario usuTemporal) {
        this.usuTemporal = usuTemporal;
    }

    public String getCorreoIn() {
        return correoIn;
    }

    public void setCorreoIn(String correoIn) {
        this.correoIn = correoIn;
    }

    public String getClaveIn() {
        return claveIn;
    }

    public void setClaveIn(String claveIn) {
        this.claveIn = claveIn;
    }

    public Part getArchivoFoto() {
        return archivoFoto;
    }

    public void setArchivoFoto(Part archivoFoto) {
        this.archivoFoto = archivoFoto;
    }

    public Part getArchivoCarga() {
        return archivoCarga;
    }

    public void setArchivoCarga(Part archivoCarga) {
        this.archivoCarga = archivoCarga;
    }

}
