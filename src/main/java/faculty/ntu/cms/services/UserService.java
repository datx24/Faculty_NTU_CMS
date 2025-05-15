package faculty.ntu.cms.services;

import faculty.ntu.cms.models.User;
import faculty.ntu.cms.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return user;
    }
    
    //phương thức đăng kí
    public void registerUser(User user) {
        userRepository.save(user);
    }
    // Thêm phương thức để lấy tất cả người dùng
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // Thêm phương thức findById
    public Optional<User> findById(Integer userId) {
        return userRepository.findById(userId);
    }
}
