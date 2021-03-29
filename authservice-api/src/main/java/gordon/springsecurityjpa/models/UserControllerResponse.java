package gordon.springsecurityjpa.models;

public class UserControllerResponse {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        User toSet = User.copyUser(user);
        toSet.setPassword("");
        this.user = toSet;
    }
}
