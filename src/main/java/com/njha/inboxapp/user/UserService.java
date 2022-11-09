package com.njha.inboxapp.user;

import com.njha.inboxapp.emaillist.EmailListItemService;
import com.njha.inboxapp.folder.Folder;
import com.njha.inboxapp.folder.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private FolderService folderService;

    @Autowired
    private EmailListItemService emailListItemService;

    public void takeCareOfNewUser(String userId) {
        List<Folder> userFolders = folderService.findAllByUserId(userId);
        boolean isNewUser = false;
        if (userFolders == null || userFolders.size() == 0) {
            isNewUser = true;
        }

        if (isNewUser) {
            folderService.createStandardFoldersForNewUser(userId);
            emailListItemService.sendWelcomeEmails(userId, "Inbox");
        }
    }
}
