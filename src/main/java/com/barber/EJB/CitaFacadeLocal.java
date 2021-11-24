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
import javax.ejb.Local;

/**
 *
 * @author juan
 */
@Local
public interface CitaFacadeLocal {

    void create(Cita cita);

    void edit(Cita cita);

    void remove(Cita cita);

    Cita find(Object id);

    List<Cita> findAll();

    List<Cita> findRange(int[] range);

    int count();

    public boolean crearCita(Cita citaIn, int fk_servicio, int estado_asignacion_id_estado_asignacion, Usuario usuario_id_usuario);

    public List<Cita> leerTodos(Usuario usu_cita);

    public List<Usuario> leerBarberos(TipoRol Rol);

    public boolean validarFechaCita(Date CitaIn);

    public boolean removerServicioCita(int fk_cita);
    
    public void registrarCitaServicio(int fk_idCita, Servicio fk_idServicio);

    public boolean confirmarCita(int fk_idCita);

    public List<Cita> leerClientes(Usuario usu_barbero, EstadoAsignacion estadoIn);

    public Cita encontrarCita(int idCita);

    public boolean cancelarCita(int fk_idCita);

    public List<Cita> leerCitas(EstadoAsignacion estadoIn);

    public boolean completarCita(int fk_idCita);

    public List<Cita> leerCitaCompletada(EstadoAsignacion estadoIn);

    public List<Cita> leerCitasFidelizacion(Usuario clienteIn, EstadoAsignacion estadoIn);

    public boolean calificarCita(int fk_idCita);

    public List<Cita> leerCitasAgendado(Usuario clienteIn, EstadoAsignacion estadoAgenda);

    public List<Cita> leerCitasEspera(Usuario clienteIn, EstadoAsignacion estadoEspera);

}
