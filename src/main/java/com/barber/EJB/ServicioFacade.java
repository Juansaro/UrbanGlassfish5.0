/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.EJB;

import com.barber.model.Servicio;
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
public class ServicioFacade extends AbstractFacade<Servicio> implements ServicioFacadeLocal {

    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ServicioFacade() {
        super(Servicio.class);
    }
    
    @Override
    public Servicio validarSiExiste(String nombreIn){
        try {
            Query q = em.createQuery("SELECT s FROM Servicio s WHERE s.nombre  LIKE :nombreIn");
            q.setParameter("nombreIn", nombreIn);
            return (Servicio) q.getSingleResult();
        } catch (Exception e) {
            return  null;
        }
    }
    
    @Override
    public boolean crearServicio(String ser_nombre, String ser_descripcion , float ser_costo) {
        try {
            Query c = em.createNativeQuery("INSERT INTO servicio (nombre, descripcion, costo) VALUES (?, ?, ?)");
            c.setParameter(1, ser_nombre);
            c.setParameter(2, ser_descripcion);
            c.setParameter(3, ser_costo);
            c.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }    
    }
    
    @Override
    public boolean actualizarServicios(int ser, float costo) {
        try {
            Query qr = em.createNativeQuery("UPDATE servicio SET costo = ? WHERE (id_servicio = ?)");
            qr.setParameter(1, costo);
            qr.setParameter(2, ser);
            qr.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }

    }
    
    @Override
    public Servicio encontrarServicio(int idServicio){
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT s FROM Servicio s WHERE s.idServicio = :s");
            qt.setParameter("s", idServicio);
            return (Servicio) qt.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public List<Servicio> leerTodos() {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT s FROM Servicio s");
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
