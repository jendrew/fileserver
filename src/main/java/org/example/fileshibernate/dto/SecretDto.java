package org.example.fileshibernate.dto;

import javax.validation.constraints.Size;

public class SecretDto {

    @Size(min=20, max=20, message = "Coś się nie zgadza - kod powinien mieć 20 znaków")
    private String secret;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
