package gordon.api.controllers;

import gordon.api.users.UserController;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
class UserControllerTest {

    @Mock


    @InjectMocks
    UserController userController;

    @Test
    void contextLoads(){
        Assert.assertNotNull(userController);
    }

    @Test
    void createUser() {

    }

    @Test
    void retrieveCollection() {
    }

    @Test
    void retrieveSingle() {
    }

    @Test
    void update() {
    }

    @Test
    void updatePartial() {
    }

    @Test
    void delete() {
    }
}