package com.apparel.domain.repository;

import com.apparel.domain.model.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

/**
 * Created by Mick on 4/17/2016.
 */
public interface ItemRepository extends JpaRepository<Item, String> {

    Set<Item> findByUserUuid(String uuid);

}
