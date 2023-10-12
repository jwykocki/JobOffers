package com.joboffers.domain.loginandregister;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryLoginRepositoryTestImpl implements LoginRepository{

    Map<String, User> repository = new ConcurrentHashMap<>();

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.values()
                .stream()
                .filter(user -> user.username().equals(username))
                .findFirst();
    }

    @Override
    public User save(User user) {
        repository.put(user.id(), user);
        return user;
    }
}
