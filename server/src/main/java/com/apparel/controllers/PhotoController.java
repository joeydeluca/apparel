package com.apparel.controllers;

import com.apparel.domain.model.Photo;
import com.apparel.domain.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by Joe Deluca on 8/28/2016.
 */
@RestController
public class PhotoController {

    PhotoRepository photoRepository;

    @Autowired
    public PhotoController(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @RequestMapping(value = "/photos/{uuid}", produces = "image/jpg", method = RequestMethod.GET)
    @ResponseBody
    public byte[] getPhoto(@PathVariable("uuid") String uuid, @RequestParam(required = false) String type) throws IOException, URISyntaxException {

        Photo photo = photoRepository.findOne(uuid);

        if(photo == null) {
            throw new IllegalArgumentException();
        }

        return photo.getThumbnail();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/photos/{uuid}")
    public ResponseEntity<Void> handleFileUpload(@PathVariable("uuid") String uuid, @RequestParam("file") MultipartFile file) throws IOException {

        Photo photo = photoRepository.findOne(uuid);
        photo.setThumbnail(file.getBytes());
        photoRepository.save(photo);

        return ResponseEntity.ok().build();
    }


}