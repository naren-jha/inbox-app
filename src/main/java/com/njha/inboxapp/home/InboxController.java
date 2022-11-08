package com.njha.inboxapp.home;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.njha.inboxapp.emaillist.EmailListDto;
import com.njha.inboxapp.emaillist.EmailListItem;
import com.njha.inboxapp.emaillist.EmailListItemService;
import com.njha.inboxapp.folder.Folder;
import com.njha.inboxapp.folder.FolderService;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class InboxController {

    @Autowired
    private FolderService folderService;

    @Autowired
    private EmailListItemService emailListItemService;

    @GetMapping("/")
    public String homePage(Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null || principal.getAttribute("login") == null) {
            return "index";
        }

        String userName = principal.getAttribute("name");
        model.addAttribute("userName", userName);

        String userId = principal.getAttribute("login");
        List<Folder> userFolders = folderService.findAllByUserId(userId);
        boolean isNewUser = false;
        if (userFolders == null || userFolders.size() == 0) {
            isNewUser = true;
        }

        if (isNewUser) {
            folderService.createStandardFoldersForNewUser(userId);
            emailListItemService.sendWelcomeEmails(userId, "Inbox");
        }

        userFolders = folderService.getAllFoldersForUser(userId);
        model.addAttribute("userFolders", userFolders);

        // get messages for default folder - Inbox
        List<EmailListItem> inboxMessages = emailListItemService.findAllMessagesByUserAndFolder(userId, "Inbox");

        PrettyTime prettyTime = new PrettyTime();
        List<EmailListDto> folderEmails = inboxMessages.stream().map(emailItem -> {
            UUID timeUUID = emailItem.getKey().getTimeUUID();
            Date emailDateTime = new Date(Uuids.unixTimestamp(timeUUID));
            return EmailListDto.builder().email(emailItem).agoTimeString(prettyTime.format(emailDateTime)).build();
        }).collect(Collectors.toList());

        model.addAttribute("folderEmails", folderEmails);




        model.addAttribute("folderToUnreadCounts", 1);

        return "inbox-page";
    }
}
