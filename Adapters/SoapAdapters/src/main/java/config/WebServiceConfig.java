package config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "rooms")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema roomsSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("RoomsPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://rentafield.com/soap/rooms");
        wsdl11Definition.setSchema(roomsSchema);
        return wsdl11Definition;
    }

    @Bean(name = "auth")
    public DefaultWsdl11Definition authWsdl11Definition(XsdSchema authSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("AuthPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://localhost:5173/tks/soap/auth");
        wsdl11Definition.setSchema(authSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema authSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/auth.xsd"));
    }

    @Bean
    public XsdSchema roomsSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/rooms.xsd"));
    }
}