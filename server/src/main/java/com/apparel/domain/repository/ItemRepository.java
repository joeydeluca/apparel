package com.apparel.domain.repository;

import com.apparel.domain.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by Mick on 4/17/2016.
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, String> {

    Set<Item> findByUserUuid(String uuid);

}
