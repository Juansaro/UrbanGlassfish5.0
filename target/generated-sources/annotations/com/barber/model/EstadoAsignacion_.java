package com.barber.model;

import com.barber.model.Cita;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-12-04T23:00:01")
@StaticMetamodel(EstadoAsignacion.class)
public class EstadoAsignacion_ { 

    public static volatile SingularAttribute<EstadoAsignacion, String> descripcion;
    public static volatile CollectionAttribute<EstadoAsignacion, Cita> citaCollection;
    public static volatile SingularAttribute<EstadoAsignacion, Integer> idEstadoAsignacion;

}