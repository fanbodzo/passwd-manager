package com.fanbo.vault_service.enitity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Table(name = "vault")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VaultEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String domainName;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;
}
