package kr.ac.hansung.cse.productcrud1.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {

    public enum Role {
        ROLE_USER, ROLE_ADMIN
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // ✅ 기본 생성자
    public User() {}

    // ✅ getter/setter 직접 명시
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
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
}
