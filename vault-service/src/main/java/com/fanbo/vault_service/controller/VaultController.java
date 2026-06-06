package com.fanbo.vault_service.controller;

import com.fanbo.vault_service.dto.VaultEntryRequest;
import com.fanbo.vault_service.enitity.VaultEntry;
import com.fanbo.vault_service.service.VaultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/vault")
@RequiredArgsConstructor
public class VaultController {
    private final VaultService vaultService;

    @GetMapping("/all")
    public ResponseEntity<List<VaultEntry>> getAllEntries(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userId = authentication.getName();
        return ResponseEntity.ok(vaultService.getAllEntriesByUser(userId));
    }

    @GetMapping("/{id}/password")
    public ResponseEntity<String> getVaultPassword(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(vaultService.getUnencryptedEntryById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addNewVaultEntry(@Valid @RequestBody VaultEntryRequest request,
            Authentication authentication) {
        try {
            vaultService.addNewVaultEntry(authentication.getName(), request);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteVaultEntry(@PathVariable("id") Long id) {
        try{
            vaultService.deleteEntry(id);
            return ResponseEntity.ok("success");
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No entries found");
        }
    }

}
