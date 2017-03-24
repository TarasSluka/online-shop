package com.sluka.taras.web.controller.rest;

import com.sluka.taras.service.PhotoService;
import com.sluka.taras.service.serviceImpl.PhotoServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/product/photo/{id}")
public class PhotoProductRestController {
    private final Logger logger = LogManager.getLogger(PhotoProductRestController.class);
    @Autowired
    ServletContext servletContext;
    private PhotoService photoService;

    @Autowired
    PhotoProductRestController(PhotoServiceImpl photoService) {
        this.photoService = photoService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<String>> getAllPhoto(@PathVariable("id") Long id) throws IOException {
        List<String> list = photoService.getAllURLToPhotoProduct(id);
        if (list.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createPhoto(@PathVariable("id") Long id, MultipartHttpServletRequest request) throws IOException {
        List<MultipartFile> multipartFileList = request.getFiles("image");
        if (multipartFileList.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        photoService.savePhotoProduct(id, multipartFileList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //------------------- Delete a photo--------------------------------------------------------
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/{name:.+}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deletePhoto(@PathVariable("id") Long id, @PathVariable("name") String name) {
        logger.info("id = " + id + " name " + name);
        boolean f = photoService.deletePhotoProduct(id, name);
        if (f)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteDirectory(@PathVariable("id") Long id) throws IOException {
        logger.info("id = " + id);
        photoService.deleteDirectoryWithFileProduct(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/ava", method = RequestMethod.GET)
    public ResponseEntity<Map> getAva(@PathVariable("id") Long id) throws IOException {
        String avaUrl = null;
        avaUrl = photoService.getURLToAvaProduct(id);
        Map map = new HashMap();
        map.put("avaUrl", avaUrl);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}