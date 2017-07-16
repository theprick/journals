package com.crossover.trial.journals.service.email;

import org.junit.Assert;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.function.Supplier;

public class WiserAssertions {

    private final List<WiserMessage> messages;

    private WiserAssertions(List<WiserMessage> messages) {
        this.messages = messages;
    }

    public static WiserAssertions assertReceivedMessage(Wiser wiser) {
        return new WiserAssertions(wiser.getMessages());
    }

    public void assertMessage(String from, String to, String subject, String content, String contentType) {
        WiserMessage message = messages.stream().filter(m -> m.getEnvelopeSender().equals(from))
                .findFirst().orElseThrow(assertionError("No mail message from {0} found", from));

        Assert.assertEquals(to, message.getEnvelopeReceiver());
        try {
            MimeMessage mimeMessage = message.getMimeMessage();
            Assert.assertEquals(subject, mimeMessage.getSubject());
            Assert.assertEquals(contentType, mimeMessage.getContentType());
            Assert.assertEquals(removeNewLinesAndSpaces(content), removeNewLinesAndSpaces((String)mimeMessage.getContent()));
        } catch(MessagingException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static Supplier<AssertionError> assertionError(String errorMessage, String... args) {
        return () -> new AssertionError(MessageFormat.format(errorMessage, args));
    }

    private String removeNewLinesAndSpaces(String origText) {
        String text = origText.replaceAll("\\n", "");
        text = text.replaceAll("\\r", "");
        text = text.replaceAll("\\s", "");
        return text;
    }
}
