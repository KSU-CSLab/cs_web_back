package kr.ac.ks.cs_web_back.global.config;

import kr.ac.ks.cs_web_back.global.resolver.IdentifiedUserArgumentResolver;
import kr.ac.ks.cs_web_back.global.resolver.ResolvedUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final IdentifiedUserArgumentResolver identifiedUserArgumentResolver;
    private final ResolvedUserArgumentResolver resolvedUserArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(identifiedUserArgumentResolver);
        resolvers.add(resolvedUserArgumentResolver);
    }
}
