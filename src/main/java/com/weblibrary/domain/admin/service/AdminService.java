package com.weblibrary.domain.admin.service;

import com.weblibrary.domain.admin.Repository.MemoryUserRoleRepository;
import com.weblibrary.domain.admin.Repository.UserRoleRepository;
import com.weblibrary.domain.admin.model.Role;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.repository.MemoryUserRepository;
import com.weblibrary.domain.user.repository.UserRepository;
import com.weblibrary.domain.user.service.UserService;
import lombok.AccessLevel;
import lombok.*;

import static com.weblibrary.domain.admin.model.RoleType.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminService {
    @Getter
    private static final AdminService instance = new AdminService();
    private static final UserRepository userRepository = MemoryUserRepository.getInstance();
    private final UserRoleRepository userRoleRepository = MemoryUserRoleRepository.getInstance();

    public boolean setUserAsAdmin(User user) {
        Role findAdminRole = userRoleRepository.findTypeByUserIdAndRoleType(user.getId(), Admin);
        if (findAdminRole == null) {
            Role newRole = new Role(MemoryUserRoleRepository.lastId++, user.getId(), Admin);
            userRoleRepository.save(newRole);
            return true;
        }
        return false;
    }

    public boolean setUserAsDefault(User user) {
        Role findAdminRole = userRoleRepository.findTypeByUserIdAndRoleType(user.getId(), Admin);
        Role findDefaltRole = userRoleRepository.findTypeByUserIdAndRoleType(user.getId(), Default);

        if (findAdminRole != null) {
            userRoleRepository.remove(findAdminRole.getId());
        }

        if (findDefaltRole == null) {
            Role newRole = new Role(MemoryUserRoleRepository.lastId++, user.getId(), Default);
            userRoleRepository.save(newRole);
            return true;
        }

        return false;
    }

    public User deleteUser(User user) {
        return userRepository.remove(user);
    }
}
