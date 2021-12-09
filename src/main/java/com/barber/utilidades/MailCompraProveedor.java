/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.utilidades;

import com.barber.model.DetalleCompra;
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
public abstract class MailCompraProveedor {

    public static void correoCompra(
            String nombreProveedor,
            String apellidoProveedor,
            String correoPara,
            Date fechaCompra,
            float cobro,
            List<DetalleCompra> detalles) {
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
            int detallesIndice = detalles.size();

            String[] nombresProductos = new String[detallesIndice];
            int[] cantidades = new int[detallesIndice];
            float[] costoTotales = new float[detallesIndice];

            MimeMessage mensage = new MimeMessage(session);
            mensage.setFrom(new InternetAddress(usuario));
            mensage.addRecipient(Message.RecipientType.TO, new InternetAddress(correoPara));
            mensage.setSubject("Hola! " + nombreProveedor + " La compra ha sido registrada en Urban barber shop.");
            String content = "<html>\n<body>\n";

            content += "<h1> Hola, ";
            content += nombreProveedor;
            content += " ";
            content += apellidoProveedor;
            content += " </h1>";
            content += "Fecha de la compra: ";
            content += fechaCompra;
            content += " <br/> ";
            content += "<br/> Detalles de compra: ";

            for (DetalleCompra det : detalles) {
                nombresProductos[it] = detalles.get(it).getProductoIdProducto().getNombreProducto();
                cantidades[it] = detalles.get(it).getCantidadSolicitada();
                costoTotales[it] = detalles.get(it).getCostoTotal();
                content += ("Producto: " + nombresProductos[it] + " " + " cantidad total: " + cantidades[it] + " costo total: " + costoTotales[it] + " \n");
                it++;
            }

            content += "<br/> Valor a pagar al proveedor:  ";
            content += cobro;
            content += "<br/> Recuerda visitar Urban barber shop para solocitar más comprar de productos!";
            content += "<br/>";
            content += "</body>\n</html>";
            mensage.setContent(content, "text/html");
            Transport.send(mensage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);

        }

    }

}
