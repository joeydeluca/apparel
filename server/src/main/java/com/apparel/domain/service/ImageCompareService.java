package com.apparel.domain.service;

import com.apparel.domain.model.Conflict;
import com.apparel.domain.model.EventGuestOutfit;
import com.apparel.domain.model.EventGuestOutfitItem;
import com.apparel.domain.model.Photo;
import com.apparel.domain.repository.ConflictRepository;
import com.apparel.domain.repository.EventGuestOutfitRepository;
import com.apparel.domain.service.models.ImageCompareResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

/**
 * Created by Joe Deluca on 10/23/2016.
 */
@Service
@Transactional
public class ImageCompareService {

    private EventGuestOutfitRepository eventGuestOutfitRepository;
    private ConflictRepository conflictRepository;

    @Autowired
    Environment env;

    @Autowired
    public ImageCompareService(EventGuestOutfitRepository eventGuestOutfitRepository, ConflictRepository conflictRepository) {
        this.eventGuestOutfitRepository = eventGuestOutfitRepository;
        this.conflictRepository = conflictRepository;
    }

    public void compareOutfits(EventGuestOutfit targetEventGuestOutfit) {
        Set<EventGuestOutfit> allEventGuestOutfits = eventGuestOutfitRepository.findByEventGuestEventUuid(targetEventGuestOutfit.getEventGuest().getEvent().getUuid());

        for(EventGuestOutfit eventGuestOutfit : allEventGuestOutfits) {
            // Do not compare with itself
            if(targetEventGuestOutfit.getUuid().equals(eventGuestOutfit.getUuid())) continue;

            // Only compare outfits on the same date
            if(targetEventGuestOutfit.getDate().getTime() == eventGuestOutfit.getDate().getTime()) continue;

            for(EventGuestOutfitItem eventGuestOutfitItem : eventGuestOutfit.getEventGuestOutfitItems()) {

                Photo photo1 = eventGuestOutfitItem.getItem().getPhoto();

                for(EventGuestOutfitItem targetEventGuestOutfitItem : targetEventGuestOutfit.getEventGuestOutfitItems()) {
                    Photo photo2 = targetEventGuestOutfitItem.getItem().getPhoto();

                    // Compare images
                    Float photoSimilarity = getPhotoSimilarity(photo1, photo2);
                    if(photoSimilarity != null) {
                        // Create conflict
                        Conflict conflict = new Conflict();
                        conflict.setEventGuestOutfitItem1(targetEventGuestOutfitItem);
                        conflict.setEventGuestOutfitItem2(eventGuestOutfitItem);
                        conflict.setRate(photoSimilarity);
                        conflictRepository.save(conflict);
                    }
                }

            }
        }
    }

    private Float getPhotoSimilarity(Photo photo1, Photo photo2) {
        RestTemplate restTemplate = new RestTemplate();

        if(env == null) return null;

        String imageCompareServiceUrl = env.getProperty("image-compare-url");
        imageCompareServiceUrl = imageCompareServiceUrl + "?url1=" + getPhotoUrl(photo1) + "&url2=" + getPhotoUrl(photo2);

        ImageCompareResponse response = restTemplate.getForObject(imageCompareServiceUrl, ImageCompareResponse.class);

        if(response.getSuccess() && response.getResult() != null) {
            return response.getResult();
        }

        return null;
    }

    private String getPhotoUrl(Photo photo) {
        return "https://apparelapp.herokuapp.com/photos/" + photo.getUuid();
    }
}
