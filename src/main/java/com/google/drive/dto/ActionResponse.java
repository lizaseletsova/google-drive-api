package com.google.drive.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class ActionResponse implements Serializable {
    private String access_token;
    private String expires_in;
    private String scope;
    private String token_type;
    private String id_token;
}
