package com.userauth.userauth.registration;


public record RegistrationRequest(String firstName, String lastName, String password, String email) {
}
