/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.EJB;

import com.barber.model.Compra;
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
public class CompraFacade extends AbstractFacade<Compra> implements CompraFacadeLocal {

    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CompraFacade() {
        super(Compra.class);
    }
    
    @Override
    public boolean registrarCompra(Date fechaIn, int proveedorIn){
        try {
            Query qr = em.createNativeQuery("INSERT INTO compra (fecha_solicitud, numero_proveedor) VALUES (?, ?)");
            qr.setParameter(1, fechaIn);
            qr.setParameter(2, proveedorIn);
            qr.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public List<Compra> leerTodos() {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT c FROM Compra c");
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
}
