package com.fanbo.vault_service.dto;

import jakarta.persistence.Column;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VaultEntryResponse {

    private String login;
    private String password;
}
