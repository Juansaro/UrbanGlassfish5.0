package com.barber.model;

import com.barber.model.Despacho;
import com.barber.model.Producto;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-11-26T21:55:06")
@StaticMetamodel(DetalleDespacho.class)
public class DetalleDespacho_ { 

    public static volatile SingularAttribute<DetalleDespacho, Integer> cantidadSolicitada;
    public static volatile SingularAttribute<DetalleDespacho, Float> costoTotal;
    public static volatile SingularAttribute<DetalleDespacho, Producto> productoIdProducto;
    public static volatile SingularAttribute<DetalleDespacho, Integer> idDetalleDespacho;
    public static volatile SingularAttribute<DetalleDespacho, Despacho> despachoIdDespacho;

}