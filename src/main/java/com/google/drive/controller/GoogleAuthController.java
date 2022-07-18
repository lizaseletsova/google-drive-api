package com.google.drive.controller;

import com.google.api.client.util.Value;
import com.google.drive.exception.GoogleAccessDeniedException;
import com.google.drive.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ResolvableType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static  com.google.drive.enums.AccessKey.DRIVE;
import static com.google.drive.enums.AccessKey.GOOGLE_OAUTH_TOKEN;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.CODE;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.SCOPE;

@RequiredArgsConstructor
public class GoogleAuthController {
    private static final String URLS = "urls";
    private static final String LOGGIN = "loggin";
    private final TokenService tokenService;
    private final ClientRegistrationRepository clientRegistrationRepository;
    @Value("authorization.request.base.uri")
    private final String authorizationRequestBaseUri;
    Map<String, String> oauth2AuthenticationUrls
            = new HashMap<>();

    @RequestMapping("/oauth2/callback/google")
    public String callbackUrl(
            HttpServletRequest request,
            HttpSession httpSession) {
        httpSession
                .setAttribute(GOOGLE_OAUTH_TOKEN.toString(),
                        tokenService.fetchToken(getCode(request), getScopeWithPermission(request))
                );
        return "redirect:/files";
    }

    @RequestMapping("/")
    public String login(Model model) {
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
                .as(Iterable.class);
        if (type != ResolvableType.NONE &&
                ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }
        clientRegistrations.forEach(registration ->
                oauth2AuthenticationUrls.put(registration.getClientName(),
                        authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
        model.addAttribute(URLS, oauth2AuthenticationUrls);

        return LOGGIN;
    }

    private String getCode(HttpServletRequest request) {
        Optional<String> code = Optional.of(request.getParameter(CODE));
        return code.orElseThrow(() -> new GoogleAccessDeniedException("Authorization from google failed"));
    }

    private String getScopeWithPermission(HttpServletRequest request) {
        String[] scopes = request.getParameter(SCOPE).split(" ");
        return Stream.of(scopes)
                        .filter(s -> s.contains(DRIVE.label))
                        .findFirst()
                        .orElseThrow(() -> new GoogleAccessDeniedException("You must have to allow drive data to be accessed."));
    }

}
