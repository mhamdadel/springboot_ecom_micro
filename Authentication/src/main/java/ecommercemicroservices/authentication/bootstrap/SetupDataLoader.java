package ecommercemicroservices.authentication.bootstrap;

import ecommercemicroservices.authentication.model.Authority;
import ecommercemicroservices.authentication.model.Role;
import ecommercemicroservices.authentication.repository.AuthorityRepository;
import ecommercemicroservices.authentication.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthorityRepository authorityRepository;


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;
        Authority readAuthority
                = createPrivilegeIfNotFound("READ_AUTHORITY");
        Authority writeAuthority
                = createPrivilegeIfNotFound("WRITE_AUTHORITY");

        Set<Authority> adminAuthorities = Set.of(readAuthority, writeAuthority);
        createRoleIfNotFound("ROLE_ADMIN", adminAuthorities);
            createRoleIfNotFound("ROLE_USER", Set.of(readAuthority));
        alreadySetup = true;
    }

    @Transactional
    public Authority createPrivilegeIfNotFound(String name) {

        Authority authority = authorityRepository.findByName(name);
        if (authority == null) {
            authority = new Authority();
            authority.setName(name);

            authorityRepository.save(authority);
        }
        return authority;
    }

    @Transactional
    public Role createRoleIfNotFound(
            String name, Set<Authority> authorities) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setAuthorities(authorities);
            roleRepository.save(role);
        }
        return role;
    }
}
