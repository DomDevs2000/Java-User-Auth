package com.userauth.userauth.appuser;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AppUserRepositiory {

    Optional<AppUser> findByEmail(String email);
}

