/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.utilidades;

import com.barber.model.Servicio;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author juan
 */
public class MailFactura {

    public static void correoCita(
            String nombreCliente,
            String apellidoCliente,
            String correoPara,
            Date fechaCita,
            String nombreBarbero,
            String apellidoBarbero,
            Collection<Servicio> servicios,
            float costo
    ) {
        final String usuario = "urbanbarbershopOficial@gmail.com";
        final String clave = "sennaland 432";

        Properties props = new Properties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com"); // envia 
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.starttls.required", "false");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, clave);
            }
        });

        try {
            int it = 0;
            int servicioIndice = servicios.size();
            List<Servicio> listaServicios = new ArrayList(servicios);

            String[] nombresServicios = new String[servicioIndice];

            MimeMessage mensage = new MimeMessage(session);
            mensage.setFrom(new InternetAddress(usuario));
            mensage.addRecipient(Message.RecipientType.TO, new InternetAddress(correoPara));
            mensage.setSubject("Hola " + nombreCliente + " Tu cita ha sido facturada.");
            String content = "<html>\n<body>\n";

            content += "<h1> Hola, ";
            content += nombreCliente;
            content += " ";
            content += apellidoCliente;
            content += " </h1>";
            content += "Fecha de la cita: ";
            content += fechaCita;
            content += "<br/> Fuiste atendido por el barbero <br/>";
            content += nombreBarbero;
            content += " ";
            content += apellidoBarbero;
            content += "<br/> Servicios agendados: ";

            for (Servicio servicio : servicios) {
                nombresServicios[it] = listaServicios.get(it).getNombre();
                content += (nombresServicios[it] + " ");
                it++;
            }

            content += "<br/> Costo total:  ";
            content += costo;
            content += "<br/>";
            content += "</body>\n</html>";
            mensage.setContent(content, "text/html");
            Transport.send(mensage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);

        }

    }

}
