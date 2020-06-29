package com.simen.tradesystem.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.simen.tradesystem.core.BaseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;

@Entity
public class User extends BaseEntity {
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    private String firstname;
    private String lastname;
    private String username;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String[] roles;

    protected User () {
        super();
    }

    public User(String username, String firstname, String lastname, String password, String[] roles){
        this();
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        setPassword(password);
        this.roles = roles;
    }

    public static PasswordEncoder getPasswordEncoder() {
        return PASSWORD_ENCODER;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }
}
