package org.example.fileshibernate.model;

import org.example.fileshibernate.dto.PrefsDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class User implements UserDetails {

    public static final String DIRECTION_ASCENDING = "ASC";
    public static final String DIRECTION_DESCENDING = "DESC";

    public static final String ORDER_BY_DATE = "DATE";
    public static final String ORDER_BY_NAME = "NAME";
    public static final String ORDER_BY_SIZE = "SIZE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 20)
    private String username;

    @Column(length = 100)
    private String password;

    @Column(length = 30)
    private String secret;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column
    private String direction = DIRECTION_ASCENDING;

    @Column
    private String sortingOrder = ORDER_BY_NAME;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getSortingOrder() {
        return sortingOrder;
    }

    public void setSortingOrder(String sortingOrder) {
        this.sortingOrder = sortingOrder;
    }

    public PrefsDto getPrefs() {
        return new PrefsDto(direction, sortingOrder);
    }

    public void setPrefs(PrefsDto prefsDto) {
        this.direction = prefsDto.getDirection();
        this.sortingOrder = prefsDto.getSortingOrder();
    }
}
