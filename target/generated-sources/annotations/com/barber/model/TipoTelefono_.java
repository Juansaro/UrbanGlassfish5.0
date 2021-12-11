package com.barber.model;

import com.barber.model.Usuario;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-12-10T22:56:22")
@StaticMetamodel(TipoTelefono.class)
public class TipoTelefono_ { 

    public static volatile SingularAttribute<TipoTelefono, String> descripcion;
    public static volatile CollectionAttribute<TipoTelefono, Usuario> usuarioCollection;
    public static volatile SingularAttribute<TipoTelefono, Integer> numeroTipoTelefono;

}