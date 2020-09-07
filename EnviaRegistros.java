package com.jay.enviaregistros;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

/**
 * @description Programa para envio automatico de correos
 *
 * @param ver archivo parametros.xml
 * @author jdelatorre
 */

/**
 * 2013-02-12 By Jorge De La Torre Dávalos
 * Se actualiza para enviar por parametro asunto del mensaje, y contenido del mensaje
 */
public class EnviaRegistros {

    static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

    public static void main(String[] args) throws IOException {

        Calendar cal = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);

        String[] parametros = new String[6];

        leerParametros(parametros);

        String servidor = parametros[0];
        String remitente = parametros[1];
        String destinatario = parametros[2];
        String adjunto = parametros[3];
        String asunto = parametros[4];
        String cuerpo = parametros[5];

        Properties properties = System.getProperties();

        properties.setProperty("mail.smtp.host", servidor);

        Session sesion = Session.getDefaultInstance(properties);

        try {
            MimeMessage mensaje = new MimeMessage(sesion);

            mensaje.setFrom(new InternetAddress(remitente));

            mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));

            mensaje.setSubject(asunto + sdf.format(cal.getTime()));

            BodyPart cuerpoMensaje = new MimeBodyPart();

            cuerpoMensaje.setText(cuerpo);

            Multipart multipart = new MimeMultipart();

            multipart.addBodyPart(cuerpoMensaje);

            cuerpoMensaje = new MimeBodyPart();
            DataSource source = new FileDataSource(adjunto);
            cuerpoMensaje.setDataHandler(new DataHandler(source));
            cuerpoMensaje.setFileName(adjunto);
            multipart.addBodyPart(cuerpoMensaje);

            mensaje.setContent(multipart);

            Transport.send(mensaje);
            System.out.println("Mensaje Enviado Correctamente.");
        } catch (javax.mail.MessagingException mex) {
            System.out.println("Error: " + mex.toString());
        }

    }

    static void leerParametros(String[] parametros) throws IOException {
        String directorioTrabajo = new java.io.File(".").getCanonicalPath();
        try {
            File archivoParametros = new File(directorioTrabajo + "\\parametros.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(archivoParametros);
            doc.getDocumentElement().normalize();
            NodeList nodeLst = doc.getElementsByTagName("parametro");

            for (int s = 0; s < nodeLst.getLength(); s++) {

                Node fstNode = nodeLst.item(s);

                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element parametroServidor = (Element) fstNode;

                    NodeList servidorLista = parametroServidor.getElementsByTagName("servidor");
                    Element servidorNodo = (Element) servidorLista.item(0);
                    NodeList servidorNodoLista = servidorNodo.getChildNodes();
                    parametros[0] = ((Node) servidorNodoLista.item(0)).getTextContent();
                    parametros[0] = parametros[0].replace("\n", "");
                    parametros[0] = parametros[0].replace(" ", "");
                    System.out.println("Servidor : " + parametros[0]);



                    NodeList remitenteLista = parametroServidor.getElementsByTagName("remitente");
                    Element remitenteNodo = (Element) remitenteLista.item(0);
                    NodeList remitenteNodoLista = remitenteNodo.getChildNodes();
                    parametros[1] = ((Node) remitenteNodoLista.item(0)).getTextContent();
                    parametros[1] = parametros[1].replace("\n", "");
                    parametros[1] = parametros[1].replace(" ", "");
                    System.out.println("Remitente : " + parametros[1]);



                    NodeList destinatarioLista = parametroServidor.getElementsByTagName("destinatario");
                    Element destinatarioNodo = (Element) destinatarioLista.item(0);
                    NodeList destinatarioNodoLista = destinatarioNodo.getChildNodes();
                    parametros[2] = ((Node) destinatarioNodoLista.item(0)).getTextContent();
                    parametros[2] = parametros[2].replace("\n", "");
                    parametros[2] = parametros[2].replace(" ", "");
                    System.out.println("Destinatario : " + parametros[2]);



                    NodeList adjuntoLista = parametroServidor.getElementsByTagName("adjunto");
                    Element adjuntoNodo = (Element) adjuntoLista.item(0);
                    NodeList adjuntoNodoLista = adjuntoNodo.getChildNodes();
                    parametros[3] = ((Node) adjuntoNodoLista.item(0)).getTextContent();
                    parametros[3] = parametros[3].replace(" ", "");
                    parametros[3] = parametros[3].replace("\n", "");
                    System.out.println("Adjunto : " + parametros[3]);


                    NodeList asuntoLista = parametroServidor.getElementsByTagName("asunto");
                    Element asuntoNodo = (Element) asuntoLista.item(0);
                    NodeList asuntoNodoLista = asuntoNodo.getChildNodes();
                    parametros[4] = ((Node) asuntoNodoLista.item(0)).getTextContent();
                    parametros[4] = parametros[4].replace("\n", "");
                    System.out.println("Asunto : " + parametros[4]);

                    NodeList cuerpoLista = parametroServidor.getElementsByTagName("cuerpo");
                    Element cuerpoNodo = (Element) cuerpoLista.item(0);
                    NodeList cuerpoNodoLista = cuerpoNodo.getChildNodes();
                    parametros[5] = ((Node) cuerpoNodoLista.item(0)).getTextContent();
                    System.out.println("Mensaje : " + parametros[5]);

                }

            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }
}
