package com.barber.model;

import com.barber.model.Usuario;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-11-23T19:05:46")
@StaticMetamodel(TipoIdentificacion.class)
public class TipoIdentificacion_ { 

    public static volatile SingularAttribute<TipoIdentificacion, String> descripcion;
    public static volatile SingularAttribute<TipoIdentificacion, Integer> idTipoIdentificacion;
    public static volatile CollectionAttribute<TipoIdentificacion, Usuario> usuarioCollection;

}