/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.EJB;

import com.barber.model.Despacho;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author juan
 */
@Local
public interface DespachoFacadeLocal {

    void create(Despacho despacho);

    void edit(Despacho despacho);

    void remove(Despacho despacho);

    Despacho find(Object id);

    List<Despacho> findAll();

    List<Despacho> findRange(int[] range);

    int count();

    public boolean salidaProducto(int cantidadIn, int idProductoIn);

    public boolean salidaBodega(int existencias, int idBodegaIn);

    public boolean registrarDespacho(Date fecha);

    public List<Despacho> leerTodos();
    
}
