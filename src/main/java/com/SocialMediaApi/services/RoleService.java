package com.SocialMediaApi.services;

import com.SocialMediaApi.entities.Role;
import com.SocialMediaApi.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    public Role getUserRole() {
        //F: возвращается optional и хорошо бы обработать
        return roleRepository.findByName("ROLE_USER").get();
    }

    public Role getAdminRole() {
        //F: возвращается optional и хорошо бы обработать
        return roleRepository.findByName("ROLE_ADMIN").get();
    }
}
