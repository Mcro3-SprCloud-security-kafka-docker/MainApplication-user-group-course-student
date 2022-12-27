package com.quanzip.filemvc.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

// cau hinh object luu lai ngon ngu user chon vao session, mac dinh sd theo config chung ta set cho LocaleResolver
// interceptor duyet tat ca cac request gui len, check param LANG de xac dinh ngon ngu user dang dung.
// Neu ngon ngu thay doi, session se luu lai ngon ngu do vao session va dc add lai vao registry cua spring
// Chi khi nao dc add vao registry thi ngon ngu moi dc thay doi.

@Configuration
public class BeanFactory implements WebMvcConfigurer {

//    khi ta chon 1 ngon ngu,
//    thang nay se luu ngon ngu do trong session cua ta khi login
//    Mac dinh sd tieng anh:
    @Bean
    public LocaleResolver localeResolver(){
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(new Locale("en"));
        return sessionLocaleResolver;
    }

    // like a filter
//     dong vai tro Khi ma request den backend , co param nao ten la LANG tren path hoac trong form
//     hay khong, giup client chi ra ngon ngu user dang sd
//    vd http://localhost:8080/hello?lang=vi
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor(){
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
