package edu.rico.backend.usersapp.backendusersapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import edu.rico.backend.usersapp.backendusersapp.models.entities.Comment;
import edu.rico.backend.usersapp.backendusersapp.models.entities.Image;
import edu.rico.backend.usersapp.backendusersapp.models.entities.User;
import edu.rico.backend.usersapp.backendusersapp.repositories.CommentRepository;
import edu.rico.backend.usersapp.backendusersapp.repositories.ImageRepository;
import edu.rico.backend.usersapp.backendusersapp.repositories.UserRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

        public Comment createComment(Comment comment, Long imageId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Obtener el nombre de usuario del token
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        comment.setUser(user);

        Optional<Image> imageOptional = imageRepository.findById(imageId);
        if (imageOptional.isPresent()) {
            Image image = imageOptional.get();
            comment.setImage(image);
            return commentRepository.save(comment);
        } else {
            throw new RuntimeException("Image not found with id " + imageId);
        }
    }


    public Page<Comment> getCommentsByImageId(Long imageId, PageRequest pageRequest) {
        return commentRepository.findByImageId(imageId, pageRequest);
    }


    public void deleteComment(Long commentId){
        commentRepository.deleteById(commentId);
    }

}
