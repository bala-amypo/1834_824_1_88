// // package com.example.demo.security;

// // import com.example.demo.model.User;
// // import com.example.demo.repository.UserRepository;

// // import org.springframework.security.core.userdetails.UserDetailsService;
// // import org.springframework.security.core.userdetails.UserDetails;
// // import org.springframework.security.core.userdetails.UsernameNotFoundException;

// // import org.springframework.security.core.authority.SimpleGrantedAuthority;
// // import org.springframework.security.core.userdetails.User.UserBuilder;

// // import java.util.Collections;

// // public class CustomUserDetailsService implements UserDetailsService {

// //     private final UserRepository userRepository;

// //     public CustomUserDetailsService(UserRepository userRepository) {
// //         this.userRepository = userRepository;
// //     }

// //     @Override
// //     public UserDetails loadUserByUsername(String email)
// //             throws UsernameNotFoundException {

// //         User user = userRepository.findByEmail(email)
// //                 .orElseThrow(() -> new UsernameNotFoundException("User not found"));

// //         UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(email);
// //         builder.password(user.getPassword());
// //         builder.authorities(Collections.singleton(new SimpleGrantedAuthority(user.getRole())));

// //         return builder.build();
// //     }
// // }

// package com.example.demo.security;

// import com.example.demo.model.User;
// import com.example.demo.repository.UserRepository;
// import org.springframework.security.core.userdetails.*;
// import org.springframework.stereotype.Service;

// @Service
// public class CustomUserDetailsService implements UserDetailsService {

//     private final UserRepository userRepository;

//     public CustomUserDetailsService(UserRepository userRepository) {
//         this.userRepository = userRepository;
//     }

//     @Override
//     public UserDetails loadUserByUsername(String email)
//             throws UsernameNotFoundException {

//         User user = userRepository.findByEmail(email)
//                 .orElseThrow(() ->
//                         new UsernameNotFoundException("User not found with email: " + email));

//         return org.springframework.security.core.userdetails.User
//                 .withUsername(user.getEmail())
//                 .password(user.getPassword())
//                 .roles(user.getRole())
//                 .build();
//     }
// }

package com.example.demo.security;

import com.example.demo.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private Long id;
    private String email;
    private String password;
    private String role;

    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
    }

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority(role)
        );
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
