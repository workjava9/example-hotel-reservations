package com.example.examplehotelreservations;

import com.example.examplehotelreservations.repository.jpa.UserRepository;
import com.example.examplehotelreservations.service.UserService;
import com.example.examplehotelreservations.web.model.RoleType;
import com.example.examplehotelreservations.web.model.hotel.Hotel;
import com.example.examplehotelreservations.web.model.hotel.Role;
import com.example.examplehotelreservations.web.model.hotel.User;
import com.example.examplehotelreservations.web.response.HotelResponse;
import com.example.examplehotelreservations.web.response.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;

@Testcontainers
@ActiveProfiles("test")
public abstract class BaseControllerIT {

    @Autowired protected ObjectMapper objectMapper;
    @Autowired protected PasswordEncoder passwordEncoder;
    @Autowired protected UserService userService;
    @Autowired protected UserRepository userRepository;

    @Container
    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.3"))
                    .withDatabaseName("hotel_reservations"); // без пробела

    @DynamicPropertySource
    static void dbProps(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        r.add("spring.datasource.username", POSTGRES::getUsername);
        r.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @BeforeEach
    void seed() {
        userService.create(
                User.builder().username("admin").password(passwordEncoder.encode("admin"))
                        .email("admin@example.com").build(),
                Role.from(RoleType.ROLE_ADMIN));

        userService.create(
                User.builder().username("user").password(passwordEncoder.encode("user"))
                        .email("user@example.com").build(),
                Role.from(RoleType.ROLE_USER));
    }

    @AfterEach
    void cleanup() {
        userRepository.deleteAll();
    }

    protected User createUser(Long id, RoleType roleType){
        return new User(
                id, "user " + id, passwordEncoder.encode("user"), "email " + id,
                Role.from(roleType), new ArrayList<>(), new ArrayList<>());
    }

    protected Hotel createHotel(Long id) {
        return new Hotel(id, "hotel " + id, "hotel", "city", "address",
                0.0, 0.0, 0, new ArrayList<>(), new ArrayList<>());
    }

    protected HotelResponse createHotelResponse(Long id) {
        return new HotelResponse(id, "hotel " + id, "hotel", "city", "address", 0.0, 0.0, 0);
    }

    protected UserResponse createUserResponse(Long id, RoleType roleType){
        return new UserResponse(id, "user " + id, passwordEncoder.encode("user"),
                "email " + id, Role.from(roleType).getAuthority().toString());
    }
}
