package org.duckdns.ahamike.rollbook.config.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.duckdns.ahamike.rollbook.table.EntityGroup;
import org.duckdns.ahamike.rollbook.table.EntityRole;
import org.duckdns.ahamike.rollbook.table.EntityUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UserDetailsCustom implements UserDetails {
    private Long id;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String name;
    private String email;
    private String phone;
    private String tag;
    private Set<String> roles = new HashSet<>();
    private Set<String> groups = new HashSet<>();
    private Long activate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        if (activate == 100) {
            return true;
        }
        else {
            return false;
        }
    }

    public UserDetailsCustom(EntityUser user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        if (user.getTag() != null) {
            this.tag = user.getTag().getName();
        }
        else {
            this.tag = null;
        }
        for (EntityRole role : user.getRoles()) {
            if (this.roles.contains(role.getName()) == false) {
                this.roles.add(role.getName());
            }
        }
        for (EntityGroup group : user.getGroups()) {
            if (this.groups.contains(group.getName()) == false) {
                this.groups.add(group.getName());
            }
            for (EntityRole role : group.getRoles()) {
                if (this.roles.contains(role.getName()) == false) {
                    this.roles.add(role.getName());
                }
            }
        }
        this.activate = user.getActive();
    }
}
