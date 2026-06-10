package service;

import enumeration.UserRole;
import exception.AuthException;
import exception.DatabaseException;
import model.User;
import repository.UserAuthRecord;
import repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class UserService {

    public User authenticate(String username, String plainPassword) throws DatabaseException, AuthException {
        Optional<UserAuthRecord> userAuthRecord = UserRepository.findUserByUsername(username);
        if(userAuthRecord.isEmpty()) {
            throw new AuthException("Invalid username or password");
        }
        if(!BCrypt.checkpw(plainPassword, userAuthRecord.get().passwordHash())) {
            throw new AuthException("Invalid username or password");
        }
        return new User(userAuthRecord.get().id(),userAuthRecord.get().username(),userAuthRecord.get().role());
    }

    public User register(String username, String plainPassword) throws AuthException {
        if(username == null) {
            throw new AuthException("Invalid username");
        }
        if(plainPassword.length()<5) {
            throw new AuthException("Invalid password");
        }
        if(UserRepository.existsByUsername(username)) {
            throw new AuthException("Username already exists");
        }
        String hash = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        Optional<UserAuthRecord> userAuthRecord = UserRepository.insertUser(username,hash,UserRole.USER);
        if(userAuthRecord.isEmpty()) {
            throw new AuthException("Something went wrong");
        }
        return new User(userAuthRecord.get().id(),userAuthRecord.get().username(),userAuthRecord.get().role());
    }


}
