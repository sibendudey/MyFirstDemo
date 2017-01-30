package com.sibendu.spring.boot.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.sibendu.spring.boot.model.Image;
import com.sibendu.spring.boot.repository.ImageRepository;

@Service
public class ImageService {
	private static String UPLOAD_ROOT = "C:/Users/deysi/Documents/Spring_projects/Images_Upload";

	private final ImageRepository imageRepository;
	private final ResourceLoader resourceLoader;

	@Autowired
	public ImageService(ImageRepository imageRepository, ResourceLoader resourceLoader) {
		this.imageRepository = imageRepository;
		this.resourceLoader = resourceLoader;
	}

	public Resource findOneImage(String fileName) {
		return resourceLoader.getResource("file:" + UPLOAD_ROOT + "/" + fileName);
	}

	public void createImage(MultipartFile file) throws IOException {
		if (!file.isEmpty()) {
			Files.copy(file.getInputStream(), Paths.get(UPLOAD_ROOT, file.getOriginalFilename()));
			imageRepository.save(new Image(file.getOriginalFilename()));
		}
	}
	
	public void deleteImage(String fileName) throws IOException	{
		final Image byName = imageRepository.findByName(fileName);
		imageRepository.delete(byName);
		Files.deleteIfExists(Paths.get(UPLOAD_ROOT, fileName));
	}
	
	@Bean
	CommandLineRunner setUp(ImageRepository imageRepository) throws IOException	{
		
		return (args) -> {
		FileSystemUtils.deleteRecursively(new File(UPLOAD_ROOT));
		
		Files.createDirectories(Paths.get(UPLOAD_ROOT));
		
		FileCopyUtils.copy("Test File", new FileWriter(UPLOAD_ROOT + "/test"));
		imageRepository.save(new Image("test"));
		
		FileCopyUtils.copy("Test File 2", new FileWriter(UPLOAD_ROOT + "/test2"));
		imageRepository.save(new Image("test2"));
		
		FileCopyUtils.copy("Test File 3", new FileWriter(UPLOAD_ROOT + "/test3"));
		imageRepository.save(new Image("test3"));
		};
	}
}
