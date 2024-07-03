package edu.rico.backend.usersapp.backendusersapp.models.entities;

import jakarta.persistence.*;

@Entity
public class ImageLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    public ImageLike() {
    }

    public ImageLike(Long id, User user, Image image) {
        this.id = id;
        this.user = user;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ImageLike [id=" + id + ", user=" + user + ", image=" + image + "]";
    }

    
}
