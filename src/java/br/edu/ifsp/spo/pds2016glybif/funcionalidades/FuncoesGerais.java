package br.edu.ifsp.spo.pds2016glybif.funcionalidades;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Classe que contém funções de uso geral.
 *
 * @author Lorhan Sohaky
 */
public class FuncoesGerais {

    private static final Logger LOGGER = Logger.getLogger(FuncoesGerais.class.getName());

    /**
     * Método que tranforma um Objeto em XML.
     *
     * @param obj Objeto a ser transformado.
     * @param classe Classe do objeto.
     * @return XML Objeto em XML.
     * @throws Exception Erro.
     */
    public static String objectToXML(Object obj, Class classe) throws Exception {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(classe);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();

            jaxbMarshaller.marshal(obj, sw);
            return sw.toString();
        } catch (JAXBException ex) {
            LOGGER.log(Level.INFO, "Erro ao transformar objeto para XML.", ex);
            throw new Exception("Erro ao transformar objeto para XML.", ex);
        }

    }

    /**
     * Método que transforma o XML em Objeto.
     *
     * @param <T> Tipo de objeto.
     * @param xml XML do objeto.
     * @param type tipo do objeto.
     * @return Objeto Objeto do tipo especificado.
     * @throws Exception Erro.
     */
    public static <T> T xmlToObject(String xml, Class<T> type) throws Exception {
        T obj = null;
        try {
            JAXBContext jc = JAXBContext.newInstance(type);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            StringReader reader = new StringReader(xml);

            obj = type.cast(unmarshaller.unmarshal(reader));
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        } catch (JAXBException ex) {
            LOGGER.log(Level.INFO, "Erro ao transformar XML para objeto.", ex);
            throw new Exception("Erro ao transformar XML para objeto.", ex);
        }
        return obj;
    }

    /**
     * Método que chama um RESTful com a passagem de parâmetros.
     *
     * @param host Endereço (URL).
     * @param urlParameters Parâmetros.
     * @param path Local do REST.
     * @param method Método de comunicação do protocolo HTTP.
     * @param contentType Tipo de dado aceito.
     * @param accept Tipo aceito.
     * @return String com a resposta do servidor.
     * @throws MalformedURLException Erro.
     * @throws IOException Erro.
     */
    public static String callSet(String host, String urlParameters, String path, String method, String contentType, String accept) throws Exception {

        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        URL url;
        try {
            url = new URL(host + "funcoes" + path);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setRequestProperty("Content-type", contentType);
            httpCon.setRequestProperty("Accept", accept);
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod(method);
            try (DataOutputStream wr = new DataOutputStream(httpCon.getOutputStream())) {
                wr.write(postData);
                wr.close();
            }
            return httpCon.getResponseMessage();
        } catch (IOException ex) {
            LOGGER.log(Level.INFO, "Erro ao chamar o site com o envio de argumentos.", ex);
            throw new Exception("Erro ao chamar o site com o envio de argumento.", ex);
        }
    }

    /**
     * Método que chama um RESTful sem a passagem de parâmetros.
     *
     * @param host Endreço (URL).
     * @param method Método.
     * @param accept Tipo aceito.
     * @return String com a resposta do servidor.
     * @throws MalformedURLException Erro.
     * @throws IOException Erro.
     */
    public static String callGet(String host, String method, String accept) throws Exception {
        String result = "";
        String tmp;
        URL url;
        BufferedReader br = null;
        try {
            url = new URL(host);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setRequestProperty("Accept", accept);
            httpCon.setRequestMethod(method);
            httpCon.setDoOutput(true);
            try (InputStreamReader in = new InputStreamReader(httpCon.getInputStream(), StandardCharsets.UTF_8)) {
                br = new BufferedReader(in);
                while ((tmp = br.readLine()) != null) {
                    result = result.concat(tmp);
                }
            }
        } catch (IOException ex) {
            LOGGER.log(Level.INFO, "Erro ao chamar o site sem envio de argumentos.", ex);
            throw new Exception("Erro ao chamar o site sem o envio de argumentos.", ex);
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return result;
    }

    /**
     * Método para envio de e-mails.
     *
     * @param nome Nome do usuário.
     * @param para E-mail da pessoa que irá receber a mensagem.
     * @param url Endereço do site.
     * @throws MessagingException Erro.
     */
    public static void enviarEmail(String nome, String para, String url) throws MessagingException {
        //TODO: Arrumar hard code, utilizar xml
        String email = "cidadeajudapds@gmail.com";
        String senha = "senhaTeste";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, senha);
            }
        });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(email));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(para));
        message.setSubject("Cidade Ajuda: Alteração de senha");

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText("Olá " + nome + ".<br>"
                + "Essa é uma mensagem padrão. Você tem 30 minutos para mudar a senha.<br>"
                + "<a href='" + url + "'>Clique aqui para mudar a senha</a>.<br><br><br>"
                + "Att. Cidade Ajuda", "UTF-8", "html");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        message.setContent(multipart);
        Transport.send(message);
    }
}
