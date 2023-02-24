package com.governance.embassy;

import com.governance.embassy.service.UserInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class UserResolveIdControllerTest {
    @Autowired MockMvc         mockMvc;
    @MockBean  UserInfoService userInfoService;

    @Test
    void should_resolve_user_id_by_name() throws Exception {
        //given
        when(userInfoService.resolveUserId(any())).thenReturn("U-12321312");

        //then
        mockMvc
                .perform(post("/user/resolve-id")
                        //language=json
                        .content("""
                                {"raw": "My name is Kirill Tolkachev"}
                                """)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value("U-12321312"));

    }

    @Test
    void should_return_error_when_input_is_invalid() throws Exception {
        mockMvc
                .perform(post("/user/resolve-id")
                        //language=json
                        .content("""
                                {"raw": "My nam failed"}
                                """)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Follow format: My " + "name is FIRSTNAME " + "SECONDNAME"));

    }
}
