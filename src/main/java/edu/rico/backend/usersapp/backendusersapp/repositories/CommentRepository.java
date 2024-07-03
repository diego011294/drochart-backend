package edu.rico.backend.usersapp.backendusersapp.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.rico.backend.usersapp.backendusersapp.models.entities.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

        @Query("SELECT c FROM Comment c WHERE c.image.id = :imageId ORDER BY c.id DESC")
        Page<Comment> findByImageId(Long imageId, Pageable pageable);


}
