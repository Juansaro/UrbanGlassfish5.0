/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.EJB;

import com.barber.model.EstadoAsignacion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author juan
 */
@Local
public interface EstadoAsignacionFacadeLocal {

    void create(EstadoAsignacion estadoAsignacion);

    void edit(EstadoAsignacion estadoAsignacion);

    void remove(EstadoAsignacion estadoAsignacion);

    EstadoAsignacion find(Object id);

    List<EstadoAsignacion> findAll();

    List<EstadoAsignacion> findRange(int[] range);

    int count();

    public EstadoAsignacion asignacionCitaInicial();
    
}
