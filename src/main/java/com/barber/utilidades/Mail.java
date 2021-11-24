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

public abstract class Mail {

    public static void recuperarClaves(String nombreUsuario,  String correoPara, String claveNueva) {
        final String usuario = "senaland066@gmail.com";
        final String clave = "sennaland 432";

        Properties props = new Properties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com"); // envia 
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.port", "25");
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
            mensage.setSubject("Recordatorio de claves");
            mensage.setContent("<center> "
                    + "<img src='https://thumbs.dreamstime.com/b/protecci%C3%B3n-de-la-clave-de-la-seguridad-de-la-contrase%C3%B1a-de-los-datos-de-usuario-79323179.jpg' width='200px' height='200px' >"
                    + "</center>"
                    + "<br/>"
                    + "<h1> Hola, "+ nombreUsuario +" </h1>"
                    + "<br/>"
                    + "Su clave de ingreso al sistema es : " + claveNueva,
                    "text/html");
            Transport.send(mensage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
            
        }

    }

}

