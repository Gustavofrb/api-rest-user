package com.User.Api_Rest_User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>,JpaRepository<User, Long>{

	User findByLogin(String login);

}
