package com.fanbo.vault_service.repository;

import com.fanbo.vault_service.enitity.VaultEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VaultEntryRepository extends JpaRepository<VaultEntry, Long> {
    Optional<VaultEntry> findByDomainName(String domainName);
    Optional<VaultEntry> findByLogin(String login);
    List<VaultEntry> findByUserId(String userId);
}
