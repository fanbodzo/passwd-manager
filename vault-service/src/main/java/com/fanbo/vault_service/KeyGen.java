package com.fanbo.vault_service;

public class KeyGen {
    public static void main(String[] args) {
        byte[] key = new byte[32];
        new java.security.SecureRandom().nextBytes(key);
        System.out.println(java.util.Base64.getEncoder().encodeToString(key));
    }
}
