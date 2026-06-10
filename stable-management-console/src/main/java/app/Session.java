package app;

import model.User;

public class Session {
    private User currentUser;

    public void login(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
