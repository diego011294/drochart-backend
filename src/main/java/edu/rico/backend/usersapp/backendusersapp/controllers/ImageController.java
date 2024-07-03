package edu.rico.backend.usersapp.backendusersapp.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import edu.rico.backend.usersapp.backendusersapp.models.dto.UserDto;
import edu.rico.backend.usersapp.backendusersapp.models.entities.Image;
import edu.rico.backend.usersapp.backendusersapp.models.entities.User;
import edu.rico.backend.usersapp.backendusersapp.services.ImageService;
import edu.rico.backend.usersapp.backendusersapp.services.UserService;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(originPatterns = "*")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    private Cloudinary cloudinary;

    public ImageController() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dmjx8kaku",
                "api_key", "764538943993741",
                "api_secret", "_oAoL4zYp0kVxawfMFs9cP027GA"));
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description) {

        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username;
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }

            Long userId = getUserIdByUsername(username);

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "drochart",
                    "public_id", title));
            String fileDownloadUri = (String) uploadResult.get("url");

            Image image = new Image();
            image.setTitle(title);
            image.setDescription(description);
            image.setUrl(fileDownloadUri);
            image.setUser(new User(userId));
            imageService.createImage(image);

            return ResponseEntity.ok("Imagen subida con éxito: " + title);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al subir la imagen.");
        }
    }

    @GetMapping("/gallery")
    public ResponseEntity<Page<Image>> getAllImages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Page<Image> images = imageService.getAllImages(page, size);
        return ResponseEntity.ok(images);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Image> getImageById(@PathVariable Long id) {
        Optional<Image> imageOptional = imageService.getImageById(id);
        return imageOptional.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Image> updateImage(@PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile file) {

        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username;
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }

            Long userId = getUserIdByUsername(username);

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "drochart",
                    "public_id", title));
            String fileDownloadUri = (String) uploadResult.get("url");

            Image image = imageService.getImageById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Image not found"));
            image.setTitle(title);
            image.setDescription(description);
            image.setUrl(fileDownloadUri);
            image.setUser(new User(userId));
            Image updatedImage = imageService.updateImage(id, image);

            return ResponseEntity.ok(updatedImage);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable Long id) {
        imageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public Page<Image> buscarImagenesPorTitulo(@RequestParam String titulo, 
                                               @RequestParam int page, 
                                               @RequestParam int size) {
        return imageService.buscarImagenesPorTitulo(titulo, page, size);
    }

    @GetMapping("/show")
    public ResponseEntity<List<Image>> getImagesByUser() {
        // Obtener el usuario autenticado
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = getUserIdByUsername(username); // Obtener el ID del usuario

        // Obtener las imágenes asociadas al usuario por su ID
        List<Image> images = imageService.getImagesByUserId(userId);
        return ResponseEntity.ok(images);
    }

    @PutMapping("/{imageId}/like")
    public ResponseEntity<?> likeImage(@PathVariable Long imageId) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username;
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }

            imageService.likeImage(imageId, username);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/latest")
    public List<Image> getLatestImages() {
        return imageService.getLatestImages();
    }


    public Long getUserIdByUsername(String username) {
        Optional<UserDto> userDtoOptional = userService.getUserByUsername(username);

        // Verificar si el Optional tiene valor y obtener el ID
        if (userDtoOptional.isPresent()) {
            UserDto userDto = userDtoOptional.get();
            return userDto.getId(); // Supongamos que UserDto tiene un método getId()
        } else {
            // Manejar el caso donde no se encuentra el usuario
            throw new NoSuchElementException("Usuario no encontrado");
        }
    }
}
