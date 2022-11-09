package com.njha.inboxapp.home;

import com.njha.inboxapp.emaillist.EmailListDto;
import com.njha.inboxapp.emaillist.EmailListItemService;
import com.njha.inboxapp.folder.Folder;
import com.njha.inboxapp.folder.FolderService;
import com.njha.inboxapp.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class InboxController {

    @Autowired
    private FolderService folderService;

    @Autowired
    private EmailListItemService emailListItemService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String homePage(Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null || principal.getAttribute("login") == null) {
            return "index";
        }

        String userName = principal.getAttribute("name");
        model.addAttribute("userName", userName);

        String userId = principal.getAttribute("login");
        userService.takeCareOfNewUser(userId);

        List<Folder> userFolders = folderService.getAllFoldersForUser(userId);
        model.addAttribute("userFolders", userFolders);

        // get messages for default folder - Inbox
        List<EmailListDto> folderEmails = emailListItemService.findAllMessagesByUserAndFolder(userId, "Inbox");
        model.addAttribute("folderEmails", folderEmails);




        model.addAttribute("folderToUnreadCounts", 1);

        return "inbox-page";
    }
}
