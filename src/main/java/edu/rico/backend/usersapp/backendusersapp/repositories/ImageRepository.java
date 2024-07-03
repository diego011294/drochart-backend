package edu.rico.backend.usersapp.backendusersapp.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.rico.backend.usersapp.backendusersapp.models.entities.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    
    @Query("SELECT p FROM Image p WHERE LOWER(p.title) LIKE %:title%")
    Page<Image> findByTituloContainingIgnoreCase(@Param("title") String title, Pageable pageable);

    List<Image> findByUserId(Long userId);

    @Query("SELECT i FROM Image i ORDER BY i.id DESC")
    List<Image> findLatestImages(PageRequest pageable);

    @Query("SELECT i FROM Image i ORDER BY i.id DESC")
    Page<Image> findAll(Pageable pageable);

}
