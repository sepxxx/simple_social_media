package com.simple_social_media.services;

import com.simple_social_media.entities.Role;
import com.simple_social_media.repositories.RoleRepository;
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
}
