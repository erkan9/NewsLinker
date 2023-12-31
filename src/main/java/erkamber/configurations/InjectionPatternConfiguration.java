package erkamber.configurations;

import org.springframework.context.annotation.Configuration;

@Configuration
public class InjectionPatternConfiguration {

    String injectionPattern = ".*[;|&$`\\\'\"<>].*";

    String sqlInjectionPattern = ".*\\b(ALTER|CREATE|DELETE|DROP|EXEC(UTE){0,1}|INSERT( +INTO){0,1}|MERGE|SELECT|UPDATE)\\b.*";

    public String getInjectionPattern() {

        return injectionPattern;
    }

    public String getSqlInjectionPattern() {

        return sqlInjectionPattern;
    }
}
