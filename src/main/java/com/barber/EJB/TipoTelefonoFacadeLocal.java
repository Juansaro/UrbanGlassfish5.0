/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.EJB;

import com.barber.model.TipoTelefono;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author juan
 */
@Local
public interface TipoTelefonoFacadeLocal {

    void create(TipoTelefono tipoTelefono);

    void edit(TipoTelefono tipoTelefono);

    void remove(TipoTelefono tipoTelefono);

    TipoTelefono find(Object id);

    List<TipoTelefono> findAll();

    List<TipoTelefono> findRange(int[] range);

    int count();
    
}
