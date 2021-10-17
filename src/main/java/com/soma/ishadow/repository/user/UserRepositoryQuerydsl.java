package com.soma.ishadow.repository.user;


import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.domains.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryQuerydsl {

    User save(User user);

    List<User> findByAll();

    Optional<User> findById(Long userId);

    Optional<User> findByEmail(String email);

    Optional<User> delete(Long userId) throws BaseException;

    Optional<User> findByEmailAndName(String email, String name);
}
