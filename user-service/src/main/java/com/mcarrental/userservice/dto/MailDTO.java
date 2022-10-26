package com.mcarrental.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MailDTO {

    private String to;

    private String subject;

    private List<String> messageParts;

    private Map<String, String> links;

    private Locale locale;
}
