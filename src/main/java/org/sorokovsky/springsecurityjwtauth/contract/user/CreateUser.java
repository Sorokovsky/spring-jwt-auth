package org.sorokovsky.springsecurityjwtauth.contract.user;

public record CreateUser(String email, String password, String firstName, String lastName, String middleName) {

}
