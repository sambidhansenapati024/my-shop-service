package com.myShop.my_shop_service.repo;
import com.myShop.my_shop_service.entity.User;
import com.myShop.my_shop_service.enums.Role;
import com.myShop.my_shop_service.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByMobileNumber(String mobileNumber);

    Optional<User> findByEmail(String email);

    boolean existsByMobileNumber(String mobileNumber);

    boolean existsByEmail(String email);

    List<User> findByRole(Role role);
}
