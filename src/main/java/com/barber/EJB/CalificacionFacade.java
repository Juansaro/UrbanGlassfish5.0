/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.EJB;

import com.barber.model.Calificacion;
import com.barber.model.Usuario;
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
public class CalificacionFacade extends AbstractFacade<Calificacion> implements CalificacionFacadeLocal {

    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CalificacionFacade() {
        super(Calificacion.class);
    }
    @Override
    public List<Calificacion> leerTdos() {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT c FROM Calificacion c");
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public List<Calificacion> leerCalificacionesCliente(Usuario clienteIn) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT c FROM Calificacion c WHERE c.citaTerminada.idCliente = :usu_cliente");
            qt.setParameter("usu_cliente", clienteIn);
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public List<Calificacion> leerCalificacionesBarbero(Usuario barberoIn) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT c FROM Calificacion c WHERE c.citaTerminada.idBarbero = :usu_barbero");
            qt.setParameter("usu_barbero", barberoIn);
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
}
