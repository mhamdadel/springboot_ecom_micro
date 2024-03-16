package ecommercemicroservices.authentication.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ecommercemicroservices.authentication.enums.PermissionType.ADMIN_READ;
import static ecommercemicroservices.authentication.enums.PermissionType.ADMIN_UPDATE;
import static ecommercemicroservices.authentication.enums.PermissionType.ADMIN_DELETE;
import static ecommercemicroservices.authentication.enums.PermissionType.ADMIN_CREATE;
import static ecommercemicroservices.authentication.enums.PermissionType.MANAGER_READ;
import static ecommercemicroservices.authentication.enums.PermissionType.MANAGER_UPDATE;
import static ecommercemicroservices.authentication.enums.PermissionType.MANAGER_DELETE;
import static ecommercemicroservices.authentication.enums.PermissionType.MANAGER_CREATE;

@RequiredArgsConstructor
public enum RoleType {

    USER(Collections.emptySet()),
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_DELETE,
                    MANAGER_CREATE
            )
    ),
    MANAGER(
            Set.of(
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_DELETE,
                    MANAGER_CREATE
            )
    )

    ;

    @Getter
    private final Set<PermissionType> permissionTypes;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissionTypes()
                .stream()
                .map(permissionType -> new SimpleGrantedAuthority(permissionType.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
