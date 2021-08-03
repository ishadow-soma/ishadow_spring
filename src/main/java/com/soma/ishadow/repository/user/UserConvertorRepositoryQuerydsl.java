package com.soma.ishadow.repository.user;

import com.soma.ishadow.domains.user.UserConvertor;

import java.util.Optional;

public interface UserConvertorRepositoryQuerydsl {
    Optional<UserConvertor> findById(Long userId);
}
