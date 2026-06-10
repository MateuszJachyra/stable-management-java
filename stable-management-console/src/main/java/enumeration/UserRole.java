package enumeration;

public enum UserRole {
    ADMIN(1),USER(2);

    private int role;

    UserRole(int role) {
        this.role = role;
    }
}
