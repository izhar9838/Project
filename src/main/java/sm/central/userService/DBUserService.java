package sm.central.userService;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import sm.central.repository.UserRepository;
import sm.central.security.model.UserEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.IOException;
import java.util.Arrays;

@Component
public class DBUserService {
    @Autowired
    private UserRepository userRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeDefaultUsers() throws IOException {
        // Read the image from the classpath (src/main/resources/public/profile.jpeg)
        ClassPathResource resource = new ClassPathResource("public/profile.jpeg");
        byte[] byteArray;

        try {
            byteArray = StreamUtils.copyToByteArray(resource.getInputStream());
        } catch (IOException e) {
            throw new IOException("Could not read the profile image from classpath: " + e.getMessage(), e);
        }

        // Using existsByUsername
        if (!userRepository.existsByUsername("admin")) {
            UserEntity entity = new UserEntity();
            entity.setRole("admin");
            entity.setUsername("admin");
            entity.setPassword("admin");
            entity.setEmail("i4izharali9838@gmail.com");
            entity.setPhoneNumber(9838909249L);
            entity.setProfileImage(byteArray);


            userRepository.save(entity);
            System.out.println("Created admin user");
        }
    }
}