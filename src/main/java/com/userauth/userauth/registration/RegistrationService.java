package com.userauth.userauth.registration;

import com.userauth.userauth.appuser.AppUser;
import com.userauth.userauth.appuser.AppUserRole;
import com.userauth.userauth.appuser.AppUserService;
import com.userauth.userauth.registration.token.ConfirmationToken;
import com.userauth.userauth.registration.token.ConfirmationTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class RegistrationService {

    private final EmailValidator emailValidator;
    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;

    public RegistrationService(EmailValidator emailValidator, AppUserService appUserService, ConfirmationTokenService confirmationTokenService) {
        this.emailValidator = emailValidator;
        this.appUserService = appUserService;
        this.confirmationTokenService = confirmationTokenService;
    }

    public String register(RegistrationRequest request) {
        boolean isValid = emailValidator.test(request.email());

        if (!isValid) {
            throw new IllegalStateException("Email Not Valid");
        }
        return appUserService.signUpUser(new AppUser(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.password(),
                AppUserRole.USER

        ));
    }
    @Transactional
    public String confirmToken(String token) {

        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail());
        return "confirmed";
    }
}
