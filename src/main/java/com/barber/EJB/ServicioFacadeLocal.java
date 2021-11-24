/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.EJB;

import com.barber.model.Servicio;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author juan
 */
@Local
public interface ServicioFacadeLocal {

    void create(Servicio servicio);

    void edit(Servicio servicio);

    void remove(Servicio servicio);

    Servicio find(Object id);

    List<Servicio> findAll();

    List<Servicio> findRange(int[] range);

    int count();

    public Servicio validarSiExiste(String nombreIn);

    public boolean crearServicio(String ser_nombre, String ser_descripcion, float ser_costo);

    public Servicio encontrarServicio(int idServicio);

    public boolean actualizarServicios(int ser, float costo);

    public List<Servicio> leerTodos();
    
}
