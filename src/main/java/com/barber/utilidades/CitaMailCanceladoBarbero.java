/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.utilidades;

import java.util.Date;
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
public class CitaMailCanceladoBarbero {
    
    public static void correoCita(String nombreBarber, String apellidoBarber, String nombreCliente, String apellidoCliente, String correoPara, Date fechaCita) {
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
            MimeMessage mensage = new MimeMessage(session);
            mensage.setFrom(new InternetAddress(usuario));
            mensage.addRecipient(Message.RecipientType.TO, new InternetAddress(correoPara));
            mensage.setSubject("Hola " +nombreBarber + " una cido ha sido cancelada.");
            mensage.setContent("<center> "
                    + "<img src='https://thumbs.dreamstime.com/b/protecci%C3%B3n-de-la-clave-de-la-seguridad-de-la-contrase%C3%B1a-de-los-datos-de-usuario-79323179.jpg' width='200px' height='200px' >"
                    + "</center>"
                    + "<br/>"
                    + "<h1> Hola, "+ nombreBarber + " " + apellidoBarber + " </h1>"
                    + "La cita asignada el día " + fechaCita + " ha sido cancelada por el cliente " + nombreCliente + " " + apellidoCliente
                    + "<br/>"
                    + ""
                    + "<br/>"
                    + "Recuerda estar pendiente de las citas en espera y agendadas."
                    + "<br/>"
                    + "Te esperamos!"
                    + "<br/>",
                    "text/html");
            Transport.send(mensage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
            
        }

    }
    
}