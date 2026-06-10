package repository;

import enumeration.UserRole;

public record UserAuthRecord(int id, String username, String passwordHash, UserRole role) {
}
