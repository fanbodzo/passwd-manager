package com.fanbo.vault_service.service;

import com.fanbo.vault_service.dto.VaultEntryRequest;
import com.fanbo.vault_service.dto.VaultEntryResponse;
import com.fanbo.vault_service.enitity.VaultEntry;
import com.fanbo.vault_service.repository.VaultEntryRepository;
import com.fanbo.vault_service.utils.AesEncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VaultService {

    private final VaultEntryRepository vaultEntryRepository;
    private final AesEncryptionUtil aesEncryptionUtil;

    //to zwraca wszystko fajnie bylo by dodac convert to dto mapping
    //ale naraize budujes zksielet apki to ejst jako to do do zrobienia
    public List<VaultEntry> getAllEntriesByUser(String userId) {
        return vaultEntryRepository.findByUserId(userId);
    }

    public Optional<VaultEntry> getEntryByDomainName(String domainName){
        return vaultEntryRepository.findByDomainName(domainName);
    }

    public void addNewVaultEntry(String userId,VaultEntryRequest request) throws Exception {
        VaultEntry vaultEntry = VaultEntry.builder().userId(userId)
                .domainName(request.getDomainName())
                .login(request.getLogin())
                .password(aesEncryptionUtil.encrypt(request.getPassword()))
                .build();

        vaultEntryRepository.save(vaultEntry);
    }

    public void deleteEntry(Long id){
        vaultEntryRepository.deleteById(id);
    }

    public String getUnencryptedEntryById(Long id) throws Exception{
        if(vaultEntryRepository.findById(id).isPresent()){
            VaultEntry vaultEntry = vaultEntryRepository.findById(id).get();
            return aesEncryptionUtil.decrypt(vaultEntry.getPassword());
        }else{
            throw new NoSuchElementException("Entry not found");
        }
    }
}
