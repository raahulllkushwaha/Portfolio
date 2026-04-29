package com.portfolio.config;

import com.portfolio.model.AdminUser;
import com.portfolio.repository.AdminUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${ADMIN_USERNAME}")
    private String adminUsername;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (adminUserRepository.count() == 0) {
            AdminUser admin = AdminUser.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .build();
            adminUserRepository.save(admin);
            log.info("====================================================");
            log.info("  Default admin user created from environment config.");
            log.info("====================================================");
        }
    }
}