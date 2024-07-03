package edu.rico.backend.usersapp.backendusersapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.rico.backend.usersapp.backendusersapp.models.entities.Image;
import edu.rico.backend.usersapp.backendusersapp.models.entities.ImageLike;
import edu.rico.backend.usersapp.backendusersapp.models.entities.User;

public interface ImageLikeRepository extends JpaRepository<ImageLike, Long> {
        boolean existsByUserAndImage(User user, Image image);

}
