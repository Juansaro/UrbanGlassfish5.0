/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.EJB;

import com.barber.model.Ciudad;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author juan
 */
@Stateless
public class CiudadFacade extends AbstractFacade<Ciudad> implements CiudadFacadeLocal {

    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CiudadFacade() {
        super(Ciudad.class);
    }
    
    @Override
    public Ciudad validarSiExiste(String nombreIn){
        try {
            Query q = em.createQuery("SELECT c FROM Ciudad c WHERE c.nombreCiudad  LIKE :nombreIn");
            q.setParameter("nombreIn", nombreIn);
            return (Ciudad) q.getSingleResult();
        } catch (Exception e) {
            return  null;
        }
    }
    
    @Override
    public boolean crearCiudad(String ciu_nombre){
        try {
            Query c = em.createNativeQuery("INSERT INTO ciudad (nombre_ciudad) VALUES (?)");
            c.setParameter(1, ciu_nombre);
            c.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
}
