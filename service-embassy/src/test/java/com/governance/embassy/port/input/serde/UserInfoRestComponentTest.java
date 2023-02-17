package com.governance.embassy.port.input.serde;

import com.governance.embassy.port.input.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.ObjectContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class UserInfoRestComponentTest {
    @Autowired JacksonTester<UserInfo> userInfoJacksonTester;

    @Test
    void should_split_user_name_into_first_and_last() throws IOException {
        //language=json
        UserInfo read = userInfoJacksonTester.parseObject("""
                {
                "raw": "My name is Kirill Tolkachev"
                }""");

        assertAll(
                () -> assertThat(read.getFirstName()).isEqualTo("Kirill"),
                () -> assertThat(read.getLastName()).isEqualTo("Tolkachev")
        );
    }

    @Test
    void should_return_fail_when_no_valid_format() throws IOException {
        assertThrows(
                UnsupportedUserInfoRawInputException.class,
                () -> {
                    //language=json
                    UserInfo read = userInfoJacksonTester.parseObject("""
                            {
                            "raw": "My failed input"
                            }""");
                }
        );

    }

}