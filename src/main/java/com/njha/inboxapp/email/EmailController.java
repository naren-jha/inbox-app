package com.njha.inboxapp.email;

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
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@Controller
public class EmailController {

    @Autowired
    private FolderService folderService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/emails/{id}")
    public String viewEmail(@PathVariable UUID id, Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null || principal.getAttribute("login") == null) {
            return "index";
        }

        String userName = principal.getAttribute("name");
        model.addAttribute("userName", userName);

        String userId = principal.getAttribute("login");
        List<Folder> userFolders = folderService.getAllFoldersForUser(userId);
        model.addAttribute("userFolders", userFolders);

        Email email = emailService.findEmailById(id);
        model.addAttribute("email", email);
        String toIds = String.join(", ", email.getTo());
        model.addAttribute("toIds", toIds);



        model.addAttribute("folderToUnreadCounts", 1);

        return "email-page";
    }
}
