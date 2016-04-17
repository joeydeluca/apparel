package com.jomik.apparel.domain.repository;

import com.jomik.apparel.domain.model.outfit.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Mick on 4/17/2016.
 */
public interface OutfitRepository extends JpaRepository<Outfit, String> {
}
