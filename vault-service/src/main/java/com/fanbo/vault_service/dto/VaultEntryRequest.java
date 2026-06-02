package com.fanbo.vault_service.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VaultEntryRequest {

    @NotBlank(message = "domain name can't be empty")
    private String domainName;

    @NotBlank(message = "login can't be empty")
    private String login;

    @NotBlank(message = "password can't be empty")
    private String password;
}
