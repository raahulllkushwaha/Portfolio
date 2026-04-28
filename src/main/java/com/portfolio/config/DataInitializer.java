package com.portfolio.config;

import com.portfolio.model.AdminUser;
import com.portfolio.repository.AdminUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Seeds a default admin user on first startup if none exists.
 * IMPORTANT: Change the default credentials immediately after first login!
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (adminUserRepository.count() == 0) {
            AdminUser admin = AdminUser.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .build();
            adminUserRepository.save(admin);
            log.warn("====================================================");
            log.warn("  Default admin created: admin / admin123");
            log.warn("  PLEASE CHANGE THIS PASSWORD IMMEDIATELY!");
            log.warn("====================================================");
        }
    }
}
