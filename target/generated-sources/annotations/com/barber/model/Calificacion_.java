package com.barber.model;

import com.barber.model.Cita;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-12-06T11:49:05")
@StaticMetamodel(Calificacion.class)
public class Calificacion_ { 

    public static volatile SingularAttribute<Calificacion, Cita> citaTerminada;
    public static volatile SingularAttribute<Calificacion, String> puntaje;
    public static volatile SingularAttribute<Calificacion, Integer> idCalificacion;
    public static volatile SingularAttribute<Calificacion, String> comentario;

}