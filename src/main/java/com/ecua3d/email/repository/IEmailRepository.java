package com.ecua3d.email.repository;

import com.ecua3d.email.model.EmailLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IEmailRepository extends JpaRepository<EmailLogEntity, Integer> {
    //When use JQL you should take at data class of ENTITY and its name param equal to described
    @Query("SELECT e FROM EmailLogEntity e WHERE e.statusEmail NOT IN :statusEmail")
    List<EmailLogEntity> findByNotStatusEmail(@Param("statusEmail") String statusEmail);
}
