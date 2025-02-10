package com.weblibrary.domain.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@AllArgsConstructor
@Entity
@NoArgsConstructor(force = true)
public class Role implements Comparable<Role>{
    @Setter
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long roleId;
    private final Long userId;
    private final RoleType roleType;

    @Override
    public int compareTo(Role other) {
        return roleType.compareTo(other.roleType);
    }
}
