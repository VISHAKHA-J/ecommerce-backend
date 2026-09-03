package com.ecommerce.dto;

import com.ecommerce.enums.Role;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class UserDTO {

    private Long id;

    private String username;

    private String email;

    private Role role;
}