package gordon.api.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

 public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    
    Optional<User> findById(String id);

   User getById(int id);
 }
