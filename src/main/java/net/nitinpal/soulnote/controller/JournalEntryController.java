package net.nitinpal.soulnote.controller;

import net.nitinpal.soulnote.entity.JournalEntry;
import net.nitinpal.soulnote.entity.User;
import net.nitinpal.soulnote.service.JournalEntryService;
import net.nitinpal.soulnote.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAll() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User userInDb = userService.findByUsername(username);
        List<JournalEntry> all = userInDb.getJournalEntries();

        if (all != null && !all.isEmpty()) {
            return ResponseEntity.ok(all);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry myEntry) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            journalEntryService.saveEntry(myEntry, username);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error saving journal entry: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("id/{journalId}")
    public ResponseEntity<?> findJournalById(@PathVariable ObjectId journalId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        Optional<JournalEntry> journalEntry = journalEntryService.findById(journalId);

        if (!journalEntry.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Journal not found"));
        }
        boolean isOwnedByUser = user.getJournalEntries()
                .stream()
                .anyMatch(entry -> entry.getId().equals(journalId));

        if (!isOwnedByUser) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Collections.singletonMap("error",
                            "User tried to access unauthorized journal"));
        }
        return ResponseEntity.ok(journalEntry.get());

    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteJournalById(@PathVariable ObjectId myId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean removed = journalEntryService.deleteById(myId, username);
        if (removed) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("message", "Journal entry not found"));
    }

    @PutMapping("id/{myId}")
    public ResponseEntity<?> updateJournalById(
            @PathVariable ObjectId myId,
            @RequestBody JournalEntry newEntry
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        Optional<JournalEntry> optionalOldEntry = journalEntryService.findById(myId);
        if (!optionalOldEntry.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Journal entry not found.");
        }
        boolean isOwnedByUser = user.getJournalEntries()
                .stream()
                .anyMatch(entry -> entry.getId().equals(myId));
        if (!isOwnedByUser) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You are not authorized to update this journal entry.");
        }
        JournalEntry oldEntry = optionalOldEntry.get();
        if (newEntry.getTitle() != null && !newEntry.getTitle().trim().isEmpty()) {
            oldEntry.setTitle(newEntry.getTitle().trim());
        }
        if (newEntry.getContent() != null && !newEntry.getContent().trim().isEmpty()) {
            oldEntry.setContent(newEntry.getContent().trim());
        }
        journalEntryService.updateEntry(oldEntry);
        return ResponseEntity.ok(oldEntry);
    }

}
