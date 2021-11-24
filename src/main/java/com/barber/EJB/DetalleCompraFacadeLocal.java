/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.EJB;

import com.barber.model.Compra;
import com.barber.model.DetalleCompra;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author juan
 */
@Local
public interface DetalleCompraFacadeLocal {

    void create(DetalleCompra detalleCompra);

    void edit(DetalleCompra detalleCompra);

    void remove(DetalleCompra detalleCompra);

    DetalleCompra find(Object id);

    List<DetalleCompra> findAll();

    List<DetalleCompra> findRange(int[] range);

    int count();

    public void registrarDetalleCompra(int fkCompra, int cantidadSolicitada, Date fechaRecibido, int fkProducto, float costoTotal);

    public List<DetalleCompra> leerTodos();

    public List<DetalleCompra> leerCompras(Compra detCom);
    
}
