package com.njha.inboxapp.email;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.njha.inboxapp.emaillist.EmailListItem;
import com.njha.inboxapp.emaillist.EmailListItemKey;
import com.njha.inboxapp.emaillist.EmailListItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
public class EmailService {

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private EmailListItemRepository emailListItemRepository;

    public void sendEmail(String toUserId, String fromUserId, String label, String subject, String body) {
        UUID createdAt = Uuids.timeBased();
        EmailListItemKey key = EmailListItemKey.builder().userId(toUserId).label(label).timeUUID(createdAt).build();
        EmailListItem accountCreatedNotifEmailItemList = EmailListItem.builder()
                .key(key)
                .subject(subject)
                .from(fromUserId)
                .unread(true)
                .build();
        emailListItemRepository.save(accountCreatedNotifEmailItemList);

        Email email = Email.builder()
                .id(createdAt)
                .from(fromUserId)
                .to(Arrays.asList(toUserId))
                .subject(subject)
                .body(body)
                .build();
        emailRepository.save(email);
    }

    public void sendWelcomeEmails(String userId) {
        String sub = "New VolcanoMail Account Created!";
        String body = "Hello! Your new VolcanoMail account is created and is ready for use.";
        sendEmail(userId, "admin@volcanomail", "Inbox", sub, body);

        sub = "Welcome To VolcanoMail!";
        body = "Hello! Welcome to VolcanoMail! Be hot as a Volcano and let the explosion of emails begin.";
        sendEmail(userId, "admin@volcanomail", "Inbox", sub, body);
    }

    public Email findEmailById(UUID id) {
        return emailRepository.findById(id).get();
    }
}
