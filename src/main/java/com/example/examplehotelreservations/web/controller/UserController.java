package com.example.examplehotelreservations.web.controller;

import com.example.examplehotelreservations.mapper.UserMapper;
import com.example.examplehotelreservations.service.UserService;
import com.example.examplehotelreservations.web.model.Role;
import com.example.examplehotelreservations.web.model.RoleType;
import com.example.examplehotelreservations.web.model.User;
import com.example.examplehotelreservations.web.request.UserRequest;
import com.example.examplehotelreservations.web.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping()
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserRequest request, @RequestParam RoleType roleType){
        User newUser = userService.create(userMapper.requestToUser(request), Role.from(roleType));
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.userToResponse(newUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody UserRequest request){
        User updatedUser = userService.update(id,userMapper.requestToUser(request));
        return ResponseEntity.ok(userMapper.userToResponse(updatedUser));
    }

    @GetMapping
    public ResponseEntity<?> findAllOrFindByUsername(@RequestParam(required = false) String username) {
        if (username != null) {
            return ResponseEntity.ok(userMapper.userToResponse(userService.findByUsername(username)));
        } else {
            return ResponseEntity.ok( userMapper.userListToUserResponseList(userService.findAll()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(userMapper.userToResponse(userService.findById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
