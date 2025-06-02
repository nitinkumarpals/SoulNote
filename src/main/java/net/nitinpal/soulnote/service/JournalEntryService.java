package net.nitinpal.soulnote.service;

import net.nitinpal.soulnote.entity.JournalEntry;
import net.nitinpal.soulnote.entity.User;
import net.nitinpal.soulnote.repository.JournalEntryRepo;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    private static final Logger log = LoggerFactory.getLogger(JournalEntryService.class);
    @Autowired
    private JournalEntryRepo journalEntryRepo;
    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String username) {
        User user = userService.findByUsername(username);
        journalEntry.setDate(new Date());
        JournalEntry saved = journalEntryRepo.save(journalEntry);
        user.getJournalEntries().add(saved);
        userService.updateJournalUser(user);
    }

    public void updateEntry(JournalEntry journalEntry) {
        journalEntryRepo.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepo.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepo.findById(id);
    }

    public boolean existsById(ObjectId id) {
        return journalEntryRepo.existsById(id);
    }

    @Transactional
    public void deleteById(ObjectId id, String username) {
        try {
            User user = userService.findByUsername(username);
            boolean removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if (removed) {
                userService.updateJournalUser(user);
                journalEntryRepo.deleteById(id);
            } else {
                throw new IllegalArgumentException("Journal ID not found for user: " + id);
            }
        } catch (Exception e) {
            System.err.println("Error deleting journal: " + e.getMessage());
            throw e;
        }
    }

//    public List<JournalEntry> findByUsername(String username){
//
//    }
}
