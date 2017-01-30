package com.sibendu.spring.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sibendu.spring.boot.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long>{

	public Image findByName(String name);
}
