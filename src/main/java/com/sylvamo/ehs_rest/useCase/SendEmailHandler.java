package com.sylvamo.ehs_rest.useCase;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

@Service
public class SendEmailHandler {

    private final JavaMailSender mailSender;
    private final String remetente;

    public SendEmailHandler(JavaMailSender mailSender,
                        @Value("${app.mail.from}") String remetente) {
        this.mailSender = mailSender;
        this.remetente = remetente;
    }

    /**
     * Envia um e-mail com todos os arquivos em 'arquivos' como anexos.
     */
    public void enviarComAnexos(String para,
                                String assunto,
                                String corpo,
                                List<File> arquivos) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        // true = multipart
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(remetente);
        helper.setTo(para);
        helper.setSubject(assunto);
        helper.setText(corpo, true); // true = HTML permitido

        for (File file : arquivos) {
            FileSystemResource resource = new FileSystemResource(file);
            helper.addAttachment(file.getName(), resource);
        }

        mailSender.send(message);
    }
}
