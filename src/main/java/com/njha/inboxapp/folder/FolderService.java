package com.njha.inboxapp.folder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class FolderService {

    @Autowired
    private FolderRepository folderRepository;

    public void createStandardFoldersForNewUser(String userId) {
        folderRepository.save(Folder.builder().userId(userId).label("Inbox").color("Blue").build());
        folderRepository.save(Folder.builder().userId(userId).label("Sent").color("Green").build());
        folderRepository.save(Folder.builder().userId(userId).label("Important").color("Red").build());
        folderRepository.save(Folder.builder().userId(userId).label("Starred").color("Yellow").build());

        List<Folder> userFolders = findAllByUserId(userId);

        if (userFolders == null || userFolders.size() == 0) {

        }
    }

    public List<Folder> findAllByUserId(String userId) {
        return folderRepository.findAllByUserId(userId);
    }

    public List<Folder> getAllFoldersForUser(String userId) {
        List<Folder> userFolders = folderRepository.findAllByUserId(userId);

        // Show default folder to users if for whatever reasons user folders were not created on signup
        if (userFolders == null || userFolders.size() == 0) {
            userFolders = Arrays.asList(
                    Folder.builder().userId(userId).label("Inbox").color("Blue").build(),
                    Folder.builder().userId(userId).label("Sent").color("Green").build(),
                    Folder.builder().userId(userId).label("Important").color("Red").build(),
                    Folder.builder().userId(userId).label("Starred").color("Yellow").build()
            );
        }

        return userFolders;
    }
}
