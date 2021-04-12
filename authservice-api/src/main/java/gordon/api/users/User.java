package gordon.api.users;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false, unique = true)
    @NotNull(groups = UserIdentityValidationGroup.class)
    private String username;

    @NotNull(groups = UserIdentityValidationGroup.class)
    private String password;

    @NotNull
    private boolean active;

    @NotNull
    private String roles;

    public static User copyUser(User user){
        User toReturn = new User();
        toReturn.id = user.getId();
        toReturn.username = user.getUsername();
        toReturn.password = user.getPassword();
        toReturn.active = user.getActive();
        toReturn.roles = user.getRoles();
        return toReturn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
