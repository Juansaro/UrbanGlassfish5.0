/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.utilidades;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public abstract class MailBajaCalificacion {

    public static void bajaCalificación(String nombreUsuario,  String correoPara) {
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
            mensage.setSubject("Hola!" + nombreUsuario +" has tenido un bajo rendimiento en Urban baber shop.");
            mensage.setContent("<center> "
                    + "</center>"
                    + "<br/>"
                    + "<h1> Estimado barbero "+ nombreUsuario +" </h1>"
                    + "<br/>"
                    + "Es de suma urgencia que subas tu promedio brindando una mejor calidad de servicio."
                    + "<br/>"
                    + "Recuerda responder de forma oportuna tus citas pendientes y agendadas"
                    + "así como prestar un servicio optimo al cliente"
                    + "Toma en cuenta las reseñas",
                    "text/html");
            Transport.send(mensage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
            
        }

    }

}