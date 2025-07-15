package com.System.BankBack.repository;

import com.System.BankBack.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /** ¿Existe un usuario con este username?  (único) */
    boolean existsByUsername(String username);

    /** Traer el usuario (y su subtipo) por username, si lo necesitas en otras capas */
    java.util.Optional<User> findByUsername(String username);
}
