package edu.rico.backend.usersapp.backendusersapp.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import edu.rico.backend.usersapp.backendusersapp.models.entities.Image;
import edu.rico.backend.usersapp.backendusersapp.models.entities.ImageLike;
import edu.rico.backend.usersapp.backendusersapp.models.entities.User;
import edu.rico.backend.usersapp.backendusersapp.repositories.ImageLikeRepository;
import edu.rico.backend.usersapp.backendusersapp.repositories.ImageRepository;
import edu.rico.backend.usersapp.backendusersapp.repositories.UserRepository;

import org.springframework.data.domain.Pageable;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageLikeRepository imageLikeRepository;

    private Cloudinary cloudinary;

    public ImageService(@Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret) {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
    }

    @Transactional
    public String storeFile(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return (String) uploadResult.get("url");
        } catch (IOException e) {
            throw new RuntimeException("No se pudo almacenar el archivo en Cloudinary.", e);
        }
    }

    @Transactional
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    @Transactional
    public Image createImage(Image image) {
        return imageRepository.save(image);
    }

    @Transactional
    public Optional<Image> getImageById(Long id) {
        return imageRepository.findById(id);
    }

    @Transactional
    public Image updateImage(Long id, Image updatedImage) {
        // Verificar si la imagen existe
        if (!imageRepository.existsById(id)) {
            throw new RuntimeException("No se encontró ninguna imagen con el ID proporcionado: " + id);
        }
        updatedImage.setId(id);
        return imageRepository.save(updatedImage);
    }

    @Transactional
    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }

    @Transactional
    public Page<Image> buscarImagenesPorTitulo(String titulo, int page, int size) {
        return imageRepository.findByTituloContainingIgnoreCase(titulo, PageRequest.of(page, size));
    }

    @Transactional
    public List<Image> getImagesByUserId(Long userId) {
        return imageRepository.findByUserId(userId);
    }

    @Transactional
    public void likeImage(Long imageId, String username) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Image not found with id: " + imageId));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        boolean alreadyLiked = imageLikeRepository.existsByUserAndImage(user, image);

        if (!alreadyLiked) {
            ImageLike like = new ImageLike(null, user, image);
            imageLikeRepository.save(like);

            image.setLikes(image.getLikes() + 1);
            imageRepository.save(image);
        } else {
            throw new IllegalArgumentException("User has already liked this image");
        }
    }

    public List<Image> getLatestImages() {
        return imageRepository.findLatestImages(PageRequest.of(0, 3));
    }

    public Page<Image> getAllImages(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return imageRepository.findAll(pageable);
    }

}