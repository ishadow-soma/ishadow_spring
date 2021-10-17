package com.soma.ishadow.repository.user;


import com.soma.ishadow.domains.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryQuerydsl {

    User save(User user);

    List<User> findByAll();

    Optional<User> findById(Long userId);

    Optional<User> findByEmail(String email);

    Optional<User> delete(Long userId);

    Optional<User> findByEmailAndName(String email, String name);
}
