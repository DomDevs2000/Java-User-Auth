package com.userauth.userauth.registration;

import com.userauth.userauth.appuser.AppUser;
import com.userauth.userauth.appuser.AppUserRole;
import com.userauth.userauth.appuser.AppUserService;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final EmailValidator emailValidator;
    private final AppUserService appUserService;

    public RegistrationService(EmailValidator emailValidator, AppUserService appUserService) {
        this.emailValidator = emailValidator;
        this.appUserService = appUserService;
    }

    public String register(RegistrationRequest request) {
        boolean isValid = emailValidator.test(request.getEmail());

        if (!isValid) {
            throw new IllegalStateException("Email Not Valid");
        }
        return appUserService.signUpUser(new AppUser(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                AppUserRole.USER

        ));
    }
}
