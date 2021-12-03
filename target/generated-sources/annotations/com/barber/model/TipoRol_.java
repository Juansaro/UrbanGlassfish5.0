package com.barber.model;

import com.barber.model.Usuario;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-12-02T22:48:28")
@StaticMetamodel(TipoRol.class)
public class TipoRol_ { 

    public static volatile SingularAttribute<TipoRol, String> descripcion;
    public static volatile CollectionAttribute<TipoRol, Usuario> usuarioCollection;
    public static volatile SingularAttribute<TipoRol, Integer> numeroRol;

}