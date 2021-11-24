/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.EJB;

import com.barber.model.Bodega;
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
public class BodegaFacade extends AbstractFacade<Bodega> implements BodegaFacadeLocal {

    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BodegaFacade() {
        super(Bodega.class);
    }
    
    /*
    @Override
    public boolean crearBodega(Bodega bodegaIn) {
        try {
            Query q = em.createNativeQuery("INSERT INTO Bodega ( nombre, existencias) VALUES (?, ?)");
            q.setParameter(1, bodegaIn.getNombre());
            q.setParameter(2, bodegaIn.getExistencias());
            q.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }*/
    
    @Override
    public Bodega validarSiExiste(String nombreIn){
        try {
            Query q = em.createQuery("SELECT b FROM Bodega b WHERE b.nombre  LIKE :nombreIn");
            q.setParameter("nombreIn", nombreIn);
            return (Bodega) q.getSingleResult();
        } catch (Exception e) {
            return  null;
        }
    }
    
    @Override
    public boolean crearBodega(String bod_nombre, int bod_existencias) {
        try {
            Query c = em.createNativeQuery("INSERT INTO bodega (nombre, existencias) VALUES (?, ?)");
            c.setParameter(1, bod_nombre);
            c.setParameter(2, bod_existencias );
            c.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }    
    } 
    
    @Override
    public List<Bodega> leerTodos() {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT b FROM Bodega b");
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
}
