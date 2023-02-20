package com.governance.embassy.port.input.serde;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.governance.embassy.port.input.UserInfo;
import com.governance.embassy.service.UserInfoService;
import lombok.*;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.boot.jackson.JsonObjectSerializer;

import java.io.IOException;

@JsonComponent
public class UserInfoRestComponent {
    @Data
    private static class UserInfoDeserialize extends UserInfo {
        String raw;
    }

    public static class JsonUserInfoDeserializer extends JsonDeserializer<UserInfo> {


        @Override
        public UserInfo deserialize(JsonParser jsonParser,
                                    DeserializationContext deserializationContext) throws IOException,
                JacksonException {
            UserInfoDeserialize rawUserInfoRequest = jsonParser.readValueAs(UserInfoDeserialize.class);
            String   raw                = rawUserInfoRequest.raw;
            String   firstName          = rawUserInfoRequest.getFirstName();
            String   lastName           = rawUserInfoRequest.getLastName();

            if (raw == null && firstName != null && lastName != null) {
                return UserInfo
                        .builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .build();
            }

            String[] splittedByIS = raw.split("is");
            if (splittedByIS.length > 1) {
                String names = splittedByIS[1];
                String[] s = names
                        .trim()
                        .split("\s");
                if (s.length > 0) {
                    firstName = s[0];
                    lastName  = s[1];
                    return UserInfo
                            .builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .build();
                }
            }
            throw new UnsupportedUserInfoRawInputException(raw);
        }
    }


}
