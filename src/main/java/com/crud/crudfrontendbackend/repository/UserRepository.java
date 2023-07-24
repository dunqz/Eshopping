package com.crud.crudfrontendbackend.repository;

import com.crud.crudfrontendbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional
    @Query(value = "SELECT *FROM USER WHERE email_address = :emailAddress",nativeQuery = true)
    User findByEmailAddress(String emailAddress);

}