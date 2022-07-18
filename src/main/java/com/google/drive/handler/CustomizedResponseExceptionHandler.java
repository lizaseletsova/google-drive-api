package com.google.drive.handler;

import com.google.drive.exception.GoogleAccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@RestControllerAdvice
public class CustomizedResponseExceptionHandler {

    @ExceptionHandler(GoogleAccessDeniedException.class)
    public RedirectView accessDenied(final GoogleAccessDeniedException e, RedirectAttributes attributes) {
        e.getMessage();
        RedirectView redirectView = new RedirectView("/unauthorized",true);
        redirectView.addStaticAttribute("message",e.getMessage());
        attributes.addFlashAttribute("message", e.getMessage());
        return redirectView;
    }
}
