package com.example.examplehotelreservations.web.controller;

import com.example.examplehotelreservations.mapper.UserMapper;
import com.example.examplehotelreservations.service.UserService;
import com.example.examplehotelreservations.web.model.RoleType;
import com.example.examplehotelreservations.web.model.hotel.Role;
import com.example.examplehotelreservations.web.model.hotel.User;
import com.example.examplehotelreservations.web.request.UserRequest;
import com.example.examplehotelreservations.web.response.UserResponse;
import com.example.examplehotelreservations.web.response.UserResponseList;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerWebTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockitoBean UserService userService;
    @MockitoBean UserMapper userMapper;

    @Test
    @DisplayName("GET /api/v1/user returns list or by username")
    void findAllOrByUsername() throws Exception {
        UserResponse ur = new UserResponse();
        ur.setId(1L);
        ur.setUsername("john");
        ur.setEmail("john@ex.com");
        ur.setRole("ROLE_USER");

        when(userService.findAll()).thenReturn(java.util.List.of(new User()));
        when(userMapper.userListToUserResponseList(anyList()))
                .thenReturn(new UserResponseList(java.util.List.of(ur)));

        mvc.perform(get("/api/v1/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users", hasSize(1)))
                .andExpect(jsonPath("$.users[0].id", is(1)))
                .andExpect(jsonPath("$.users[0].username", is("john")));
    }

    @Test
    @DisplayName("GET /api/v1/user/{id} returns user")
    void findById() throws Exception {
        UserResponse ur = new UserResponse();
        ur.setId(42L);
        ur.setUsername("alice");
        ur.setEmail("a@ex.com");
        ur.setRole("ROLE_ADMIN");

        when(userService.findById(42L)).thenReturn(new User());
        when(userMapper.userToResponse(any(User.class))).thenReturn(ur);

        mvc.perform(get("/api/v1/user/{id}", 42))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(42)))
                .andExpect(jsonPath("$.role", is("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("POST /api/v1/user creates user -> 201")
    void create() throws Exception {
        UserRequest req = new UserRequest("neo", "p@ss", "n@ex.com");
        UserResponse ur = new UserResponse();
        ur.setId(100L);
        ur.setUsername("neo");
        ur.setEmail("n@ex.com");
        ur.setRole("ROLE_USER");

        when(userMapper.requestToUser(any(UserRequest.class))).thenReturn(new User());
        when(userService.create(any(User.class), any(Role.class))).thenReturn(new User());
        when(userMapper.userToResponse(any(User.class))).thenReturn(ur);

        mvc.perform(post("/api/v1/user")
                        .param("roleType", RoleType.ROLE_USER.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.username", is("neo")));
    }

    @Test
    @DisplayName("PUT /api/v1/user/{id} updates user -> 200")
    void update() throws Exception {
        UserRequest req = new UserRequest("trinity", "qwe", "t@ex.com");
        UserResponse ur = new UserResponse();
        ur.setId(7L);
        ur.setUsername("trinity");
        ur.setEmail("t@ex.com");
        ur.setRole("ROLE_USER");

        when(userMapper.requestToUser(any(UserRequest.class))).thenReturn(new User());
        when(userService.update(eq(7L), any(User.class))).thenReturn(new User());
        when(userMapper.userToResponse(any(User.class))).thenReturn(ur);

        mvc.perform(put("/api/v1/user/{id}", 7)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(7)))
                .andExpect(jsonPath("$.username", is("trinity")));
    }

    @Test
    @DisplayName("DELETE /api/v1/user/{id} -> 204")
    void deleteById() throws Exception {
        doNothing().when(userService).delete(5L);

        mvc.perform(delete("/api/v1/user/{id}", 5))
                .andExpect(status().isNoContent());
    }
}
