package by.mrf1n.lectio.service;

import by.mrf1n.lectio.model.User;
import by.mrf1n.lectio.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("lectioUserDetailsService")
public class LectioUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public LectioUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByLogin(login);
        LectioUserDetails userDetails;
        if (user.isPresent()) {
            userDetails = new LectioUserDetails();
            userDetails.setUser(user.get());
        } else {
            throw new UsernameNotFoundException("User not exist with name : " + login);
        }
        return userDetails;
    }
}
