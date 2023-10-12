package com.joboffers.domain.loginandregister;

import java.nio.channels.FileChannel;
import java.util.Optional;

public interface LoginRepository {
    Optional<User> findByUsername(String username);

    User save(User user);
}
