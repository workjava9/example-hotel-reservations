package com.example.examplehotelreservations.mapper;

import com.example.examplehotelreservations.web.model.hotel.User;
import com.example.examplehotelreservations.web.request.UserRequest;
import com.example.examplehotelreservations.web.response.UserResponse;
import com.example.examplehotelreservations.web.response.UserResponseList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(source = "role.authority", target = "role")
    UserResponse userToResponse(User user);

    User requestToUser(UserRequest request);

    default UserResponseList userListToUserResponseList(List<User> users) {
        List<UserResponse> userResponses =
                users.stream().map(this::userToResponse)
                        .collect(Collectors.toList());

        return new UserResponseList(userResponses);
    }

}
