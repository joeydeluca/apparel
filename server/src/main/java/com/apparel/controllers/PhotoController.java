package com.apparel.controllers;

import com.apparel.domain.model.photo.Photo;
import com.apparel.domain.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by Joe Deluca on 8/28/2016.
 */
@Controller
public class PhotoController {

    PhotoRepository photoRepository;

    @Autowired
    public PhotoController(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @RequestMapping(value = "/photo", produces = "image/jpg", method = RequestMethod.GET)
    @ResponseBody
    public byte[] getPhoto(@RequestParam String uuid, @RequestParam(required = false) String type) throws IOException, URISyntaxException {

        Photo photo = photoRepository.findOne(uuid);

        return photo.getThumbnail();
    }


}