/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.EJB;

import com.barber.model.EstadoAsignacion;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author juan
 */
@Stateless
public class EstadoAsignacionFacade extends AbstractFacade<EstadoAsignacion> implements EstadoAsignacionFacadeLocal {

    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstadoAsignacionFacade() {
        super(EstadoAsignacion.class);
    }
    
    @Override
    public EstadoAsignacion asignacionCitaInicial(){
        try {
            int estado = 1;
            Query c = em.createQuery("SELECT e FROM EstadoAsignacion e WHERE e.idEstadoAsignacion = :estado");
            c.setParameter("estado", estado);
            return (EstadoAsignacion) c.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
}
