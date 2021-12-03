package com.barber.model;

import com.barber.model.Cita;
import com.barber.model.Ciudad;
import com.barber.model.Compra;
import com.barber.model.Despacho;
import com.barber.model.TipoIdentificacion;
import com.barber.model.TipoRol;
import com.barber.model.TipoTelefono;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-12-02T22:48:28")
@StaticMetamodel(Usuario.class)
public class Usuario_ { 

    public static volatile SingularAttribute<Usuario, TipoRol> tipoRolNumeroRol;
    public static volatile CollectionAttribute<Usuario, Cita> citaCollection;
    public static volatile CollectionAttribute<Usuario, Compra> compraCollection;
    public static volatile SingularAttribute<Usuario, Integer> idUsuario;
    public static volatile SingularAttribute<Usuario, TipoIdentificacion> tipoIdentificacionIdTipoIdentificacion;
    public static volatile SingularAttribute<Usuario, Ciudad> ciudadNumeroCiudad;
    public static volatile CollectionAttribute<Usuario, Cita> citaCollection1;
    public static volatile SingularAttribute<Usuario, String> nombre;
    public static volatile CollectionAttribute<Usuario, Despacho> despachoCollection;
    public static volatile SingularAttribute<Usuario, String> usuFoto;
    public static volatile SingularAttribute<Usuario, String> apellido;
    public static volatile SingularAttribute<Usuario, String> correo;
    public static volatile SingularAttribute<Usuario, String> contrasena;
    public static volatile SingularAttribute<Usuario, TipoTelefono> tipoTelefonoNumeroTipoTelefono;
    public static volatile SingularAttribute<Usuario, String> numeroDocumento;
    public static volatile SingularAttribute<Usuario, String> numeroTelefono;

}