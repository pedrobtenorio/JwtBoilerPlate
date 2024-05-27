package com.pedrotenorio.jwtboilerplate.domain.DTO;

import com.pedrotenorio.jwtboilerplate.domain.Role;

public record RegisterRequest(String username, Role role, String password, String email) {
}
