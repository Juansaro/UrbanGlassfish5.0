/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.EJB;

import com.barber.model.Ciudad;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author juan
 */
@Local
public interface CiudadFacadeLocal {

    void create(Ciudad ciudad);

    void edit(Ciudad ciudad);

    void remove(Ciudad ciudad);

    Ciudad find(Object id);

    List<Ciudad> findAll();

    List<Ciudad> findRange(int[] range);

    int count();

    public Ciudad validarSiExiste(String nombreIn);

    public boolean crearCiudad (String ciu_nombre);
    
}
