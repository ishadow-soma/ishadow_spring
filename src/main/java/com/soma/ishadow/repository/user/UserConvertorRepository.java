package com.soma.ishadow.repository.user;

import com.soma.ishadow.domains.user.UserConvertor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserConvertorRepository extends JpaRepository<UserConvertor, Long>, UserConvertorRepositoryQuerydsl{

    Optional<UserConvertor> findById(Long userId);
}
