package com.barber.model;

import com.barber.model.Usuario;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-12-03T11:13:59")
@StaticMetamodel(Ciudad.class)
public class Ciudad_ { 

    public static volatile SingularAttribute<Ciudad, String> nombreCiudad;
    public static volatile SingularAttribute<Ciudad, Integer> numeroCiudad;
    public static volatile CollectionAttribute<Ciudad, Usuario> usuarioCollection;

}