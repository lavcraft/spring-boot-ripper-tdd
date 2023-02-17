package com.governance.embassy.port.input;

import com.governance.embassy.port.input.serde.UnsupportedUserInfoRawInputException;
import com.governance.embassy.port.input.serde.UserInfoRestErrorResponse;
import com.governance.embassy.service.UserInfoService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping(value = "/user")
public class UserResolveIdController {
    private UserInfoService userInfoService;

    public UserResolveIdController(final UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @PostMapping("/resolve-id")
    public UserInfoResponse resolveUserInf(@RequestBody UserInfo userFirstAndLastNames) {
        String userId = userInfoService.resolveUserId(userFirstAndLastNames);

        return UserInfoResponse
                .builder()
                .userId(userId)
                .build();
    }

    @ControllerAdvice
    public static class HandlerError {
        @ExceptionHandler(UnsupportedUserInfoRawInputException.class)
        public ResponseEntity<UserInfoRestErrorResponse> handle(RuntimeException ex,
                                                                WebRequest request) {
            return ResponseEntity
                    .badRequest()
                    .body(UserInfoRestErrorResponse
                            .builder()
                            .message("Follow format: My name is FIRSTNAME SECONDNAME")
                            .build());

        }
    }
}
