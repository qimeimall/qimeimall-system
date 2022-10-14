package com.qimeixun.config;

import com.qimeixun.filter.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截路径可自行配置多个 可用 ，分隔开
        String[] patters = new String[]{"/swagger-resources/**", "/v2/**", "/swagger-ui.html/**","/images/**","/webjars/**"};
        registry.addInterceptor(new JwtInterceptor()).addPathPatterns("/**").excludePathPatterns(patters);
    }

//    .antMatchers("/swagger-ui.html").permitAll()
//             .antMatchers("/swagger-resources/**").permitAll()
//             .antMatchers("/images/**").permitAll()
//             .antMatchers("/webjars/**").permitAll()
//             .antMatchers("/v2/api-docs").permitAll()
//             .antMatchers("/configuration/ui").permitAll()
//             .antMatchers("/configuration/security").permitAll()

    /**
     * 跨域支持
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS", "HEAD")
                .maxAge(3600 * 24)
                .allowedHeaders("*");
    }
}
