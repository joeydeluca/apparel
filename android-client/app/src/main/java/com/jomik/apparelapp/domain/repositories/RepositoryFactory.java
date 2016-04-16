package com.jomik.apparelapp.domain.repositories;

import com.jomik.apparelapp.domain.repositories.item.ItemsCloudRepository;
import com.jomik.apparelapp.domain.repositories.item.ItemsInMemoryRepository;
import com.jomik.apparelapp.domain.repositories.item.ItemsRepository;

/**
 * Created by Joe Deluca on 4/7/2016.
 */
public class RepositoryFactory {
    public static ItemsRepository getItemsRepository(Type type) {
        return type == Type.CLOUD ? ItemsCloudRepository.getInstance() : ItemsInMemoryRepository.getInstance();
    }

    public enum Type {
        CLOUD,
        IN_MEMORY
    }
}
