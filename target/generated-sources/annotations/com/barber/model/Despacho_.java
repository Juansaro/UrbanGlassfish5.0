package com.barber.model;

import com.barber.model.DetalleDespacho;
import com.barber.model.Usuario;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-11-26T21:55:06")
@StaticMetamodel(Despacho.class)
public class Despacho_ { 

    public static volatile SingularAttribute<Despacho, Usuario> recepcionista;
    public static volatile SingularAttribute<Despacho, Date> fechaSolicitud;
    public static volatile SingularAttribute<Despacho, Integer> idDespacho;
    public static volatile CollectionAttribute<Despacho, DetalleDespacho> detalleDespachoCollection;

}