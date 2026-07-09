package com.uca.pncparcialfinalhotel.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
@Entity @Table(name="users") @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class User implements UserDetails {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="role_id", nullable=false) private Role role;
    @Column(name="first_name", nullable=false) private String firstName;
    @Column(name="last_name", nullable=false) private String lastName;
    @Column(name="email", nullable=false, unique=true) private String email;
    @Column(name="phone") private String phone;
    @Column(name="password", nullable=false) private String password;
    @Column(name="is_active", nullable=false) @Builder.Default private Boolean isActive = true;
    // Opcion A (regla de negocio no trivial): se incrementa cada vez que el
    // usuario cambia su contrasena. JwtUtil graba esta version dentro de
    // cada token emitido; si no coincide con la version actual, el token
    // se considera invalido aunque no haya expirado todavia.
    @Column(name="token_version", nullable=false) @Builder.Default private Integer tokenVersion = 0;
    @Column(name="created_at") private LocalDateTime createdAt;
    @Column(name="updated_at") private LocalDateTime updatedAt;
    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getRoleName())); }
    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return Boolean.TRUE.equals(isActive); }
    @PrePersist public void prePersist() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }
    @PreUpdate public void preUpdate() { updatedAt = LocalDateTime.now(); }
}