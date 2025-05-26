package kr.ac.hansung.cse.productcrud1.repo;

import kr.ac.hansung.cse.productcrud1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
