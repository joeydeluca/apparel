package com.apparel.domain.repository;

import com.apparel.domain.model.Conflict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Joe Deluca on 8/28/2016.
 */
@Repository
public interface ConflictRepository extends JpaRepository<Conflict, String> {

}
