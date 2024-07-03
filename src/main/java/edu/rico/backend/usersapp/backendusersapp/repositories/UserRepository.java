package edu.rico.backend.usersapp.backendusersapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.rico.backend.usersapp.backendusersapp.models.entities.User;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {
                //Haciendo uso de JPA con método predefinido al usar palabras clave
                Optional<User> findByUsername(String username);
                //haciendo uso de JPA definiendo una consulta personalizada JPQL
                //@Query("SELECT u FROM User u WHERE u.username=?1")
                //Optional<User> getUserByUsername(String username);

                @Query("SELECT u FROM User u JOIN FETCH u.images i WHERE SIZE(u.images) = (SELECT MAX(SIZE(ui.images)) FROM User ui)")
                Optional<User> findUserWithMostPosts();


}
