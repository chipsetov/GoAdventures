package io.softserve.goadventures.auth.service;

import io.softserve.goadventures.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class EmailSenderService {

    private final JWTService jwtService;

    @Autowired
    public EmailSenderService(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    public void sendEmail(String confirmationToken, User user) throws MessagingException, UnsupportedEncodingException{
        String username = "goadventuressup@gmail.com";
        String password = "Adventures12_";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "false");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.port", "587");

        Session session = Session.getInstance(props, null);

        Transport transport = session.getTransport("smtp");
        Message msg = new MimeMessage(session);

        InternetAddress from = new InternetAddress("goadventuressup@gmail.com ", "GoAdventures support");
        msg.setFrom(from);
        InternetAddress toAddress = new InternetAddress(user.getEmail());
        msg.setRecipient(Message.RecipientType.TO, toAddress);
        msg.setSubject("Please confirm your account");

        String htmlText =
                "<head>" +
                        "<style type=\"text/css\">" +
                        ".red{color:#FF0000;} " +
                        ".pink{color:#8B008B;}" +
                        ".button {" +
                        "  display: inline-block;" +
                        "  padding: 15px 25px;" +
                        "  font-size: 24px;" +
                        "  cursor: pointer;" +
                        "  text-align: center;" +
                        "  text-decoration: none;" +
                        "  outline: none;" +
                        "  color: #fff;" +
                        "  background-color: #4CAF50;" +
                        "  border: none;" +
                        "  border-radius: 15px;" +
                        "  box-shadow: 0 9px #999;" +
                        "}" +
                        ".button:hover {background-color: #3e8e41}" +
                        ".button:active {" +
                        "  background-color: #3e8e41;" +
                        "  box-shadow: 0 5px #666;" +
                        "  transform: translateY(4px);" +
                        "}" +
                        "</style>" +
                        "</head>" +
                        "<hr align=\"center\" width=\"90%\" size=\"1\"/>" +
                        "<h1 class=red align=\"center\">" + "Welcome to GoAdventures" + "</h1>" +
                        "<h4 class=pink align=\"center\">" + "We need to make sure you're you! " + "</h4>" +
                        "<br></br>" +
                        "<hr align=\"center\" width=\"90%\" size=\"1\"/>" +
                        "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>" +
                        "<h3 class=pink align=\"center\">" + "Hi " + user.getFullname() + "," + "</h3>" +
                        "<h3 class=pink align=\"center\">" + "Thanks for signing up for GoAdventures!" + "</h3>" +
                        "<h3 class=pink align=\"center\">" + "A Quick Click to Confirm" + "</h3>" +
                        "<h3 class=pink align=\"center\">" + "Your Account" + "</h3>" +
                        "<p align=\"center\">" +
                        "<a href=http://localhost:3001/confirm-account?token=" + confirmationToken + ">" + "<button class=button>Confirm Account</button></a></p>" +
                        "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>" + "<br></br>" +
                        "<hr align=\"center\" width=\"90%\" size=\"1\"/>" +
                        "<br></br>" + "<br></br>" +
                        "<h4 class=pink align = \"center\">" + "All Rights Reserved" + "</h4>";
        msg.setContent(htmlText, "text/html");

        transport.connect(props.get("mail.smtp.host").toString(),
                Integer.parseInt(props.get("mail.port").toString()), username, password);
        transport.sendMessage(msg,
                msg.getRecipients(Message.RecipientType.TO));
        transport.close();
    }
}