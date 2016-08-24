package com.apparel.domain.repository;

import com.apparel.domain.model.outfit.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Mick on 4/17/2016.
 */
public interface OutfitRepository extends JpaRepository<Outfit, String> {
}
