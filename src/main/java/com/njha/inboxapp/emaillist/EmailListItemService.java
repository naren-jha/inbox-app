package com.njha.inboxapp.emaillist;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public List<EmailListDto> findAllMessagesByUserAndFolder(String userId, String label) {
        List<EmailListItem> inboxMessages =  emailListItemRepository.findAllByKey_UserIdAndKey_Label(userId, label);

        PrettyTime prettyTime = new PrettyTime();
        List<EmailListDto> folderEmails = inboxMessages.stream().map(emailItem -> {
            UUID timeUUID = emailItem.getKey().getTimeUUID();
            Date emailDateTime = new Date(Uuids.unixTimestamp(timeUUID));
            String agoTimeString = prettyTime.format(emailDateTime);
            return EmailListDto.builder().email(emailItem).agoTimeString(agoTimeString).build();
        }).collect(Collectors.toList());

        return folderEmails;
    }
}
