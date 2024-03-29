package software.sigma.bu003.internship.coparts.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import software.sigma.bu003.internship.coparts.config.security.JwtConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@WebMvcTest({MainController.class})
@AutoConfigureMockMvc(addFilters = false)
class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtConfiguration jwtConfiguration;

    @Test
    void shouldMainRedirectToSwaggerIfSuccess() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(redirectedUrl("/swagger-ui/index.html"));
    }
}