package gordon.api.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gordon.api.Application;
import gordon.api.persistence.User;
import gordon.api.security.AuthUserDetails;
import gordon.api.service.UserDataService;
import gordon.api.web.UserController;
import gordon.api.web.GenericResponse;
import gordon.api.web.UserDto;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(classes = {Application.class})
class UserControllerTest {

    private final String USER_PASSWORD = "password";
    private final String USER_USERNAME = "username";
    private final String USER_PASSWORD_UPDATED = "password_updated";
    private final String USER_USERNAME_UPDATED = "username_updated";

    private final String USER_CONTROLLER_URI = "/users";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    UserDataService mockUserDataService;

    @InjectMocks
    UserController userController;

    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MockitoAnnotations.initMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void contextLoads(){
        Assertions.assertNotNull(userController);
        Assertions.assertNotNull(mockMvc);
    }

    @Test
    void CanCreateNewUser() throws Exception {
        UserDto newUser = new UserDto();
        newUser.setPassword(USER_PASSWORD);
        newUser.setUsername(USER_USERNAME);

        GenericResponse responseContent;

        // mock userdataservice.registerNewUser, called by POST /user endpoint
        Mockito.when(mockUserDataService.registerNewUser(Mockito.any(UserDto.class))).thenReturn(new User());

        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.post(USER_CONTROLLER_URI)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newUser)));

        MvcResult mvcResult = resultActions.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.ACCEPTED.value());
    }

    @Test
    void Given_AuthenticatedUser_Then_CanUpdateUser() throws Exception {
        User userInDB = new User();
        userInDB.setPassword(USER_PASSWORD);
        userInDB.setRoles("ROLE_USER");
        userInDB.setUsername(USER_USERNAME);
        userInDB.setActive(true);
        userInDB.setId(1);

        AuthUserDetails authUserDetails = new AuthUserDetails(userInDB);

        UserDto updatedUser = new UserDto();
        updatedUser.setUsername(USER_USERNAME_UPDATED);

        // mocking spring security context ***/
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
        SecurityContextHolder.setContext(securityContext);

        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URI)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(updatedUser)));

        MvcResult result = resultActions.andReturn();
        MockHttpServletResponse response = result.getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.OK.value());
    }
}