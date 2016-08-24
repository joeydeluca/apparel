package com.apparel.domain.repository;

import com.apparel.domain.model.photo.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Mick on 4/17/2016.
 */
public interface PhotoRepository extends JpaRepository<Photo, String> {
}
