package com.apparel.domain.repository;

import com.apparel.domain.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Mick on 4/17/2016.
 */
@Repository
public interface PhotoRepository extends JpaRepository<Photo, String> {
}
