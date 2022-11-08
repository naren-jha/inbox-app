package com.njha.inboxapp.emaillist;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class EmailListItemService {

    @Autowired
    private EmailListItemRepository emailListItemRepository;

    public void sendWelcomeEmails(String userId, String label) {
        EmailListItemKey key = EmailListItemKey.builder().userId(userId).label(label).timeUUID(Uuids.timeBased()).build();
        EmailListItem accountCreatedNotifEmailItemList = EmailListItem.builder()
                .key(key)
                .subject("New VolcanoMail Account Created!")
                .to(Arrays.asList(userId))
                .unread(true)
                .build();
        emailListItemRepository.save(accountCreatedNotifEmailItemList);

        key = EmailListItemKey.builder().userId(userId).label(label).timeUUID(Uuids.timeBased()).build();
        EmailListItem welcomeEmailItemList = EmailListItem.builder()
                .key(key)
                .subject("Welcome To VolcanoMail!")
                .to(Arrays.asList(userId))
                .unread(true)
                .build();
        emailListItemRepository.save(welcomeEmailItemList);
    }

    public List<EmailListItem> findAllMessagesByUserAndFolder(String userId, String label) {
        return emailListItemRepository.findAllByKey_UserIdAndKey_Label(userId, label);
    }
}
