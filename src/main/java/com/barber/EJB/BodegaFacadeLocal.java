/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.EJB;

import com.barber.model.Bodega;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author juan
 */
@Local
public interface BodegaFacadeLocal {

    void create(Bodega bodega);

    void edit(Bodega bodega);

    void remove(Bodega bodega);

    Bodega find(Object id);

    List<Bodega> findAll();

    List<Bodega> findRange(int[] range);

    int count();

    public Bodega validarSiExiste(String nombreIn);

    public boolean crearBodega(String bod_nombre, int bod_existencias);

    public List<Bodega> leerTodos();
    
}
