/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.utilidades;

import com.barber.model.Servicio;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class SmsCitaFacturada {

    public static final String ACCOUNT_SID = "AC9418a16ecb7f93d9550c33a195d7f173";
    public static final String AUTH_TOKEN = "3a4f2a4b6867b2c8e9740f0e1a130888";

    public static void envioSMSCitaFacturada(
            String numeroPara,
            String nombreCliente,
            String apellidoCliente,
            Date fechaCita,
            String nombreBarbero,
            String apellidoBarbero,
            Collection<Servicio> servicios,
            float costo
    ) {

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        //Construcción de arrays de String's
        int it = 0;
        int servicioIndice = servicios.size();
        List<Servicio> listaServicios = new ArrayList(servicios);
        String[] nombresServicios = new String[servicioIndice];

        String contenido = " ";

        for (Servicio servicio : servicios) {
            nombresServicios[it] = listaServicios.get(it).getNombre();
            contenido += (nombresServicios[it] + " ");
            it++;
        }

        Message message = Message.creator(
                //Número para
                new PhoneNumber("+57" + numeroPara),
                //Número envía
                new PhoneNumber("+12053748958"),
                //Mensaje
                "Hola! " + nombreBarbero + " " + apellidoBarbero + " " 
                + "  Tu cita ha sido cancelada por el cliente "
                + nombreCliente + " " + apellidoCliente + " porgramada el día " + fechaCita 
                + " servicios solicitados " + contenido + " , Recuerda estar pendiente de tus notificaciones en Urban barber shop"
        ).create();

    }

}
