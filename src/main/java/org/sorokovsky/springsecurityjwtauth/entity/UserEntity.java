package org.sorokovsky.springsecurityjwtauth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserEntity extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column
    private String firstName = "";
    @Column
    private String lastName = "";
    @Column
    String middleName = "";
}
