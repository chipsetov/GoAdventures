package io.softserve.goadventures.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;

@Service
public class GeneratePasswordService {
    public GeneratePasswordService() {
    }

    public String generatePassword(String email, MailContentBuilder mailContentBuilder) {
        String newPassword = RandomStringUtils.random(10, true, true);

        LoggerFactory.getLogger(GeneratePasswordService.class).info("\n\n\tNew generate password: " + newPassword + "\n");

        try {
            EmailSenderService senderService = new EmailSenderService(mailContentBuilder);
            senderService.sendNewPassword(email, newPassword);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return newPassword;
    }
}
