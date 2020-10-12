package org.example.fileshibernate.dto;


import org.example.fileshibernate.validation.PasswordsMatch;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@PasswordsMatch
public class PasswordDto {


    @NotNull
    @Size(min=8, max=100, message = "Hasło musi mieć co najmniej 8 znaków. ")
    private String password;

    @NotNull
    private String repeatedPassword;



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
    }
}
