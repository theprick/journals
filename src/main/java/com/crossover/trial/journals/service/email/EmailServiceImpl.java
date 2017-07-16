package com.crossover.trial.journals.service.email;

import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.service.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

//FIXME: move to template folder
//FIXME: move to template folder
@Service
public class EmailServiceImpl implements EmailService {

    @Value("${mail.smtp.host}")
    private String host;

    @Value("${mail.smtp.port}")
    private String port;

    @Value("${mail.smtp.auth}")
    private String auth;

    @Value("${mail.debug}")
    private String debug;

    @Value("${mail.smtp.starttls.enable}")
    private String starttlsEnable;

    @Value("${mail.smtp.socketFactory.class}")
    private String socketFactoryClass;

    @Value("${mail.smtp.user}")
    private String user;

    @Value("${mail.smtp.passwd}")
    private String passwd;

    @Value("${mail.smtp.protocol}")
    private String protocol;

    @Value("${mail.smtp.from}")
    private String from;

    public void sendNewJurnalPublishedEmail(User user, Journal journal) {
        try {
            Session session = createSession();

            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getLoginName()));
            //FIXME this should not be hardcoded
            msg.setSubject("New journal has been published in " + journal.getCategory());

            String content = EmailComposer.createNewJournaPublishedNotificationMail(user, journal);
            msg.setText(content, "utf-8", "html");

            Transport transport = session.getTransport(protocol);
            transport.connect();
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
        } catch(Exception ex) {
            throw new ServiceException("Failed to send mail");
        }
    }

    public void sendDailyDigestEmail(User user, List<Journal> journals) {
        try {
            Session session = createSession();

            MimeMessage  msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getLoginName()));
            msg.setSubject("Daily digest");

            String content = EmailComposer.createDailyDigestMail(user, journals);
            msg.setText(content, "utf-8", "html");

            Transport transport = session.getTransport(protocol);
            transport.connect();
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
        }catch(Exception ex) {
            throw new ServiceException("Failed to send mail");
        }
    }

    private Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", starttlsEnable);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", auth);
        props.put("mail.debug", debug);
        props.put("mail.smtp.socketFactory.class", socketFactoryClass);

        return Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, passwd);
                    }});
    }
}
