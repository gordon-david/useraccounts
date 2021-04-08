package gordon.api.users;

public class UserControllerResponse {
    private User user;
    private String message;

    public User getUser() {
        return user;
    }

    public UserControllerResponse() {
    }

    public UserControllerResponse(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public UserControllerResponse setUser(User user) {
        User toSet = User.copyUser(user);
        toSet.setPassword("");
        this.user = toSet;
        return this;
    }

    public UserControllerResponse setMessage(String message) {
        this.message = message;
        return this;
    }
}
