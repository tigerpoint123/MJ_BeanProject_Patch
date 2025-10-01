package global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({"classpath:/xmlForProperties/SearchController.xml"})
public class SearchControllerXmlConfig {
}


