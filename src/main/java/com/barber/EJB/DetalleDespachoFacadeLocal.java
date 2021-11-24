/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.EJB;

import com.barber.model.Despacho;
import com.barber.model.DetalleDespacho;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author juan
 */
@Local
public interface DetalleDespachoFacadeLocal {
    
    void create (DetalleDespacho detalleDespacho);
    
    void edit (DetalleDespacho detalleDespacho);
    
    void remove (DetalleDespacho detalleDespacho);
    
    DetalleDespacho find(Object id);
    
    List<DetalleDespacho> findAll();
    
    List<DetalleDespacho> findRange(int[] range);
    
    int count();

    public List<DetalleDespacho> leerTodos(Despacho despachoIn);

    public boolean registrarDetalle(int cantidadIn, float costoIn, int productoIn, int despachoIn);
    
}
