package org.example.teledon.repository;

import org.example.teledon.domain.User;

public abstract class UserRepository implements Repository<User, Long> {
    public abstract User findOne(String email, String password);
    public abstract User findOne(String email);
}
