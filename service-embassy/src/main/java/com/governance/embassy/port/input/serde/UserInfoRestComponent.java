package com.governance.embassy.port.input.serde;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.governance.embassy.port.input.UserInfo;
import com.governance.embassy.service.UserInfoService;
import lombok.*;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class UserInfoRestComponent {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RawUserInfoRequest {
        String raw;
    }

    public static class UserInfoRestDeserializer extends JsonDeserializer<UserInfo> {
        @Override
        public UserInfo deserialize(JsonParser jsonParser,
                                    DeserializationContext deserializationContext) throws IOException,
                JacksonException {
            RawUserInfoRequest rawUserInfoRequest = jsonParser.readValueAs(RawUserInfoRequest.class);
            String             raw                = rawUserInfoRequest.raw;

            String[] splittedByIS = raw.split("is");
            if (splittedByIS.length > 1) {
                String   names = splittedByIS[1];
                String[] s     = names.trim().split("\s");
                if (s.length > 0) {
                    String firstName = s[0];
                    String lastName  = s[1];
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
