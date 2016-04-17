package com.jomik.apparel.domain.repository;

import com.jomik.apparel.domain.model.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Mick on 4/17/2016.
 */
public interface ItemRepository extends JpaRepository<Item, String> {
}