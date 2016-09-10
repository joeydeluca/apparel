package com.apparel.domain.specifications;

import com.apparel.domain.model.Event;
import org.springframework.data.jpa.domain.Specification;

/**
 * Created by Joe Deluca on 9/10/2016.
 */
public class EventSpecification {

    public static Specification<Event> isNotDeleted() {
        return (root, query, cb) -> {
            return cb.equal(root.get("markedForDelete"), false);
        };
    }

    public static Specification<Event> containsTitle(String keyword) {
        return (root, query, cb) -> {
            return cb.like(cb.trim(cb.lower(root.get("title"))), keyword.toLowerCase().trim());
        };
    }
}
