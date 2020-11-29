package com.parnikel.ataccama.dao;

import com.parnikel.ataccama.model.database.Database;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostgresDatabaseRepository extends JpaRepository<Database, Long> {
}
