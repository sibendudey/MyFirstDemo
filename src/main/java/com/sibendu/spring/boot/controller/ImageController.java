package com.sibendu.spring.boot.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sibendu.spring.boot.service.ImageService;

@Controller
public class ImageController {

	private static final String BASE_PATH = "/images";
	private static final String FILENAME = "{fileName:.+}";
	
	private final ImageService imageService;
	
	
	@Autowired
	public ImageController(ImageService imageService)	{
		this.imageService = imageService;
	}
	
	@RequestMapping( method = RequestMethod.GET , value = BASE_PATH + "/" + FILENAME + "/raw")
	@ResponseBody
	public ResponseEntity<?> oneRawImage(@PathVariable String fileName)	{
		Resource file = imageService.findOneImage(fileName);
		try {
			return ResponseEntity.ok().contentLength(file.contentLength()).contentType(MediaType.IMAGE_JPEG).body(new InputStreamResource(file.getInputStream()));
		} catch (IOException e) {
			return ResponseEntity.badRequest().body("Couldn't found filename: " + fileName + " => " + e.getMessage());
		}
		
	}
	
	
	@RequestMapping( method = RequestMethod.POST , value = BASE_PATH)
	public ResponseEntity<?> createImage(@RequestParam("file") MultipartFile file , HttpServletRequest request)	{
		try {
			imageService.createImage(file);
			final URI locationURI = new URI(request.getRequestURL().toString() + "/").resolve(file.getOriginalFilename() + "/raw");
			return ResponseEntity.created(locationURI).body("Successfully upload: " + file.getOriginalFilename() );
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload" + file.getOriginalFilename() + " => " + e.getMessage());
		} catch (URISyntaxException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload" + file.getOriginalFilename() + " => " + e.getMessage());
		}
	}
	
	@RequestMapping( method = RequestMethod.DELETE , value = BASE_PATH + "/" + FILENAME + "/raw")
	public ResponseEntity<?> deleteImage( @PathVariable String fileName)	{
		try {
			imageService.deleteImage(fileName);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Successfully deleted: " + fileName);
		} catch (IOException e) {
			return ResponseEntity.badRequest().body("Couldn't found filename: " + fileName + " => " + e.getMessage());
		}		
	}
}
