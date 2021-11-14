package gordon.api.web;

import gordon.api.validation.UserIdentityValidationGroup;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * UserDTO is a data transmission object intended to be used as a type
 * for incoming requests.
 */
public class UserDto {
  @NotNull(groups = {UserIdentityValidationGroup.class})
  @Size(min = 1)
  private String username;

  @NotNull(groups = {UserIdentityValidationGroup.class})
  @Size(min = 1)
  private String password;

  public String getUsername() {
    return username;
  }

  public UserDto setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public UserDto setPassword(String password) {
    this.password = password;
    return this;
  }
}
