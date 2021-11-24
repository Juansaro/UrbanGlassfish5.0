/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.EJB;

import com.barber.model.Cita;
import com.barber.model.EstadoAsignacion;
import com.barber.model.Servicio;
import com.barber.model.TipoRol;
import com.barber.model.Usuario;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author juan
 */
@Stateless
public class CitaFacade extends AbstractFacade<Cita> implements CitaFacadeLocal {

    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CitaFacade() {
        super(Cita.class);
    }

    @Override
    public List<Cita> leerCitaCompletada(EstadoAsignacion estadoIn) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT c FROM Cita c WHERE c.estadoAsignacionIdEstadoAsignacion = :e");
            qt.setParameter("e", estadoIn);
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public List<Cita> leerCitasFidelizacion(Usuario clienteIn, EstadoAsignacion estadoIn) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT c FROM Cita c WHERE c.idCliente = :usu_cliente AND c.estadoAsignacionIdEstadoAsignacion = :e");
            qt.setParameter("usu_cliente", clienteIn);
            qt.setParameter("e", estadoIn);
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public List<Cita> leerCitasAgendado(Usuario clienteIn, EstadoAsignacion estadoAgenda) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT c FROM Cita c WHERE c.idCliente = :usu_cliente AND c.estadoAsignacionIdEstadoAsignacion = :agenda");
            qt.setParameter("usu_cliente", clienteIn);
            qt.setParameter("agenda", estadoAgenda);
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public List<Cita> leerCitasEspera(Usuario clienteIn, EstadoAsignacion estadoEspera) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT c FROM Cita c WHERE c.idCliente = :usu_cliente AND c.estadoAsignacionIdEstadoAsignacion = :espera");
            qt.setParameter("usu_cliente", clienteIn);
            qt.setParameter("espera", estadoEspera);
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean removerServicioCita(int fk_cita) {
        try {
            Query c = em.createNativeQuery("DELETE FROM servicio_has_cita  WHERE cita_id_cita = ?");
            c.setParameter(1, fk_cita);
            c.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean validarFechaCita(Date CitaIn) {

        StoredProcedureQuery q = em.createStoredProcedureQuery("VALIDAR_CITA")
                .registerStoredProcedureParameter(1, Date.class, ParameterMode.IN)
                .registerStoredProcedureParameter(2, boolean.class, ParameterMode.OUT)
                .setParameter(1, CitaIn);

        q.execute();
        //Out (2)
        boolean commentCount = (boolean) q.getOutputParameterValue(2);

        return commentCount;

    }

    @Override
    public boolean crearCita(Cita citaIn, int fk_servicio, int estado_asignacion_id_estado_asignacion, Usuario usuIn) {
        try {
            Query c = em.createNativeQuery(
                    "INSERT INTO cita "
                    + "(fecha_cita, servicio_id_servicio, estado_asignacion_id_estado_asignacion, usuario_id_usuario) "
                    + "VALUES (?, ?, ?, ?);"
            );
            c.setParameter(1, citaIn.getFechaCita());
            c.setParameter(2, citaIn.getEstadoAsignacionIdEstadoAsignacion());
            //c.setParameter(3, citaIn.getUsuarioIdUsuario());
            c.setParameter(4, citaIn.getIdCita());

            c.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /*
    @Override
    public boolean crearCitaHasServicio(Cita citaIn, int fk_servicio, int estado_asignacion_id_estado_asignacion, Usuario usuIn) {
        try {
            Query c = em.createNativeQuery(
                    "INSERT INTO cita "
                    + "(fecha_cita, servicio_id_servicio, estado_asignacion_id_estado_asignacion, usuario_id_usuario) "
                    + "VALUES (?, ?, ?, ?);"
            );
            c.setParameter(1, citaIn.getFechaCita());
            c.setParameter(2, citaIn.getEstadoAsignacionIdEstadoAsignacion());
            //c.setParameter(3, citaIn.getUsuarioIdUsuario());
            c.setParameter(4, citaIn.getIdCita());

            c.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
*/
    @Override
    public List<Cita> leerTodos(Usuario usu_cita) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT c FROM Cita c WHERE c.idCliente = :usu_cita");
            qt.setParameter("usu_cita", usu_cita);
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public List<Cita> leerClientes(Usuario usu_barbero, EstadoAsignacion estadoIn) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT c FROM Cita c WHERE c.idBarbero = :usu_barbero AND c.estadoAsignacionIdEstadoAsignacion = :estado");
            qt.setParameter("usu_barbero", usu_barbero);
            qt.setParameter("estado", estadoIn);
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public List<Cita> leerCitas(EstadoAsignacion estadoIn) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT c FROM Cita c WHERE c.estadoAsignacionIdEstadoAsignacion = :e");
            qt.setParameter("e", estadoIn);
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Usuario> leerBarberos(TipoRol r) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT u FROM Usuario u WHERE u.tipoRolNumeroRol = :r");
            qt.setParameter("r", r);
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void registrarCitaServicio(int fk_idCita, Servicio fk_idServicio) {
        try {
            Query q = em.createNativeQuery("INSERT INTO cita_has_servicio (cita_id_cita, servicio_id_servicio) VALUES (?, ?)");
            q.setParameter(1, fk_idCita);
            q.setParameter(2, fk_idServicio.getIdServicio());
            q.executeUpdate();
        } catch (Exception e) {
        }
    }
    
    @Override
    public boolean confirmarCita(int fk_idCita) {
        try {
            Query q = em.createNativeQuery("UPDATE cita SET estado_asignacion_id_estado_asignacion = 2 WHERE (id_cita = ?)");
            q.setParameter(1, fk_idCita);
            q.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean cancelarCita(int fk_idCita) {
        try {
            Query q = em.createNativeQuery("UPDATE cita SET estado_asignacion_id_estado_asignacion = 3 WHERE (id_cita = ?)");
            q.setParameter(1, fk_idCita);
            q.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean completarCita(int fk_idCita) {
        try {
            Query q = em.createNativeQuery("UPDATE cita SET estado_asignacion_id_estado_asignacion = 4 WHERE (id_cita = ?)");
            q.setParameter(1, fk_idCita);
            q.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public Cita encontrarCita(int idCita){
        Query q = em.createNamedQuery("Cita.findByIdCita", Cita.class).setParameter("idCita", idCita);
        
        List<Cita> listado = q.getResultList();
        
        if(!listado.isEmpty()){
            return listado.get(0);
        }
        return null;
    }
    
    @Override
    public boolean calificarCita(int fk_idCita) {
        try {
            Query q = em.createNativeQuery("UPDATE cita SET estado_asignacion_id_estado_asignacion = 5 WHERE (id_cita = ?)");
            q.setParameter(1, fk_idCita);
            q.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*
    @Override
    public List<Cita> leerServicios(TipoRol r) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT u FROM Usuario u WHERE u.tipoRolNumeroRol = :r");
            qt.setParameter("r", r);
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }*/
     
}
