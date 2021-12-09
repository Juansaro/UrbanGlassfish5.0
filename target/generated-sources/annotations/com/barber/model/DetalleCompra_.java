package com.barber.model;

import com.barber.model.Compra;
import com.barber.model.Producto;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-12-06T11:49:05")
@StaticMetamodel(DetalleCompra.class)
public class DetalleCompra_ { 

    public static volatile SingularAttribute<DetalleCompra, Date> fechaRecibido;
    public static volatile SingularAttribute<DetalleCompra, Compra> numeroCompra;
    public static volatile SingularAttribute<DetalleCompra, Integer> cantidadSolicitada;
    public static volatile SingularAttribute<DetalleCompra, Float> costoTotal;
    public static volatile SingularAttribute<DetalleCompra, Producto> productoIdProducto;
    public static volatile SingularAttribute<DetalleCompra, Integer> numeroDetalle;

}