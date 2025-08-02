package kr.ac.ks.cs_web_back.global.swagger.error;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;

import static java.lang.annotation.ElementType.METHOD;

@Target(value = METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ErrorCase {
    String description();
    int code();
    String exampleMessage();
}
