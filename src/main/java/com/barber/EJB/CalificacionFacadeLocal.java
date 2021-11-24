/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.EJB;

import com.barber.model.Calificacion;
import com.barber.model.Usuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author juan
 */
@Local
public interface CalificacionFacadeLocal {

    void create(Calificacion calificacion);

    void edit(Calificacion calificacion);

    void remove(Calificacion calificacion);

    Calificacion find(Object id);

    List<Calificacion> findAll();

    List<Calificacion> findRange(int[] range);

    int count();

    public List<Calificacion> leerTdos();

    public List<Calificacion> leerCalificacionesCliente(Usuario clienteIn);

    public List<Calificacion> leerCalificacionesBarbero(Usuario barberoIn);
    
}
