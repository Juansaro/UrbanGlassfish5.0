/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.EJB;

import com.barber.model.Despacho;
import com.barber.model.DetalleDespacho;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author juan
 */
@Stateless
public class DetalleDespachoFacade extends AbstractFacade<DetalleDespacho> implements DetalleDespachoFacadeLocal{
    
    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DetalleDespachoFacade() {
        super(DetalleDespacho.class);
    }
    
    @Override
    public boolean registrarDetalle(int cantidadIn, float costoIn, int productoIn, int despachoIn){
        try {
            Query qr = em.createNativeQuery("INSERT INTO detalle_Despacho (cantidad_solicitada, costo_total, producto_id_producto, despacho_id_despacho) VALUES (?, ?, ?, ?)");
            qr.setParameter(1, cantidadIn);
            qr.setParameter(2, costoIn);
            qr.setParameter(3, productoIn);
            qr.setParameter(4, despachoIn);
            qr.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public List<DetalleDespacho> leerTodos(Despacho despachoIn) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT d FROM DetalleDespacho d WHERE d.despachoIdDespacho = :d");
            qt.setParameter("d", despachoIn);
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
}
