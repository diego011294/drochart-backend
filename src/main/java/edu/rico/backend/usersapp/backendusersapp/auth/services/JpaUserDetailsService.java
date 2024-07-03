package edu.rico.backend.usersapp.backendusersapp.auth.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.rico.backend.usersapp.backendusersapp.repositories.UserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<edu.rico.backend.usersapp.backendusersapp.models.entities.User> o = userRepository.findByUsername(username);
        //si el username NO esta presenta en la bbdd
        if (!o.isPresent()) {
            throw new UsernameNotFoundException(String.format("El usuario %s no existe en el sistema", username));
        }
        //Si esta presente se recogen todos sus datos
        edu.rico.backend.usersapp.backendusersapp.models.entities.User user = o.orElseThrow();

        List<GrantedAuthority> authorities = user.getRoles()
                                                .stream()
                                                .map(r -> new SimpleGrantedAuthority(r.getName()))
                                                .collect(Collectors.toList());

        return new User(
            user.getUsername(),
            user.getPassword(),
            true, 
            true, 
            true, 
            true, 
            authorities //como en el rol authorities sólamente está el role "ROLE_USER" será este el rol definido para este objeto usuario.
            );
    }
    
}
