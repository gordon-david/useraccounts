package gordon.api.web;

import gordon.api.validation.UserIdentityValidationGroup;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
