package gordon.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import gordon.api.Application;
import gordon.api.users.*;
import org.junit.Assert;
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
        Assert.assertNotNull(userController);
        Assert.assertNotNull(mockMvc);
    }

    @Test
    void CreatNewUser() throws Exception {
        User newUser = new User();
        newUser.setPassword(USER_PASSWORD);
        newUser.setUsername(USER_USERNAME);

        UserControllerResponse responseContent;

        Mockito.when(mockUserDataService.registerNewUser(Mockito.any(User.class))).thenReturn(newUser);

        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.post(USER_CONTROLLER_URI)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newUser)));

        MvcResult mvcResult = resultActions.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        responseContent = objectMapper.readValue(response.getContentAsString(), UserControllerResponse.class);

        Assert.assertEquals(response.getStatus(), HttpStatus.ACCEPTED.value());
        Assert.assertEquals(response.getContentType(), MediaType.APPLICATION_JSON_VALUE);
        Assert.assertEquals(newUser.getUsername(), responseContent.getUser().getUsername());
    }
}