package com.njha.inboxapp.email;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.njha.inboxapp.emaillist.EmailListItem;
import com.njha.inboxapp.emaillist.EmailListItemKey;
import com.njha.inboxapp.emaillist.EmailListItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class EmailService {

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private EmailListItemRepository emailListItemRepository;

    public void sendEmailUtil(String toUserId, String fromUserId, String label, String subject, String body) {
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
        sendEmailUtil(userId, "admin@volcanomail", "Inbox", sub, body);

        sub = "Welcome To VolcanoMail!";
        body = "Hello! Welcome to VolcanoMail! Be hot as a Volcano and let the explosion of emails begin.";
        sendEmailUtil(userId, "admin@volcanomail", "Inbox", sub, body);
    }

    public Email findEmailById(UUID id) {
        return emailRepository.findById(id).get();
    }

    public void sendEmail(List<String> toIds, String fromId, String subject, String body) {
        UUID createdAt = Uuids.timeBased();
        Email email = Email.builder()
                .id(createdAt)
                .from(fromId)
                .to(toIds)
                .subject(subject)
                .body(body)
                .build();
        emailRepository.save(email);

        toIds.forEach(toId -> {
            EmailListItemKey key = EmailListItemKey.builder().userId(toId).label("Inbox").timeUUID(createdAt).build();
            EmailListItem accountCreatedNotifEmailItemList = EmailListItem.builder()
                    .key(key)
                    .subject(subject)
                    .from(fromId)
                    .unread(true)
                    .build();
            emailListItemRepository.save(accountCreatedNotifEmailItemList);
        });

        // sender user will have this in their sent item
        EmailListItemKey key = EmailListItemKey.builder().userId(fromId).label("Sent").timeUUID(createdAt).build();
        EmailListItem accountCreatedNotifEmailItemList = EmailListItem.builder()
                .key(key)
                .subject(subject)
                .from(fromId)
                .unread(true)
                .build();
        emailListItemRepository.save(accountCreatedNotifEmailItemList);
    }
}
