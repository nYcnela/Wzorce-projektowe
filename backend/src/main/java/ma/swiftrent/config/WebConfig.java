
package ma.swiftrent.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
    Tydzień 7, Zasada pojedyńczej odpowiedzialności 3
    Ta klasa odpowiada wyłącznie za konfigurację udostępniania zasobów
    i trzeba będzie wprowadzić w niej zmiany jedynie jeżeli zajdzie potrzeba zmiany
    konfiguracji zasobów
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Udostępnia pliki z folderu "uploads" pod ścieżką /uploads/**
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
//Koniec Tydzień 7, Zasada pojedyńczej odpowiedzialności 3