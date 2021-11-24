/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.EJB;

import com.barber.model.TipoRol;
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
public class TipoRolFacade extends AbstractFacade<TipoRol> implements TipoRolFacadeLocal {

    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoRolFacade() {
        super(TipoRol.class);
    }
    
    @Override
    public TipoRol encontrarUsuarioRol(){
        Query q = em.createNamedQuery("TipoRol.findByNumeroRol", TipoRol.class).setParameter("numeroRol", 2);
        
        List<TipoRol> listado = q.getResultList();
        
        if(!listado.isEmpty()){
            return listado.get(0);
        }
        return null;
    }
    
    private TipoRol asignacionRolCliente(){
        try {
            int cliente = 2;
            Query c = em.createQuery("SELECT r FROM TipoRol r WHERE r.numeroRol = :cliente");
            c.setParameter(cliente, cliente);
            return (TipoRol) c.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
}
