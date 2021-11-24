/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.EJB;

import com.barber.model.Despacho;
import java.util.Date;
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
public class DespachoFacade extends AbstractFacade<Despacho> implements DespachoFacadeLocal {

    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DespachoFacade() {
        super(Despacho.class);
    }
    
    @Override
    public boolean registrarDespacho(Date fecha) {
        try {
            Query qr = em.createNativeQuery("INSERT INTO despacho (fecha_solicitud) VALUES (?)");
            qr.setParameter(1, fecha);
            qr.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean salidaProducto(int cantidadIn, int idProductoIn) {
        try {
            Query qr = em.createNativeQuery("UPDATE producto SET cantidad = ? WHERE (id_producto = ?)");
            qr.setParameter(1, cantidadIn);
            qr.setParameter(2, idProductoIn);
            qr.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean salidaBodega(int existencias, int idBodegaIn) {
        try {
            Query qr = em.createNativeQuery("UPDATE bodega SET existencias = ? WHERE (id_bodega = ?)");
            qr.setParameter(1, existencias);
            qr.setParameter(2, idBodegaIn);
            qr.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public List<Despacho> leerTodos() {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT d FROM Despacho d");
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
}
