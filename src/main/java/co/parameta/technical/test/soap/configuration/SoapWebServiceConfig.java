package co.parameta.technical.test.soap.configuration;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import static co.parameta.technical.test.soap.util.constant.Constant.*;

/**
 * SOAP Web Service configuration class.
 * <p>
 * This configuration enables Spring Web Services (Spring-WS) and defines
 * all required beans to expose the SOAP endpoint, including:
 * <ul>
 *   <li>JAXB marshalling/unmarshalling</li>
 *   <li>SOAP message dispatcher servlet</li>
 *   <li>WSDL generation based on XSD schema</li>
 * </ul>
 *
 * The WSDL is generated dynamically at runtime using the provided XSD file
 * and is exposed under the configured SOAP URL mappings.
 */
@EnableWs
@Configuration
public class SoapWebServiceConfig {

    /**
     * Configures the JAXB marshaller used to convert between Java objects
     * and XML payloads in SOAP requests and responses.
     *
     * @return configured {@link Jaxb2Marshaller} with the SOAP context path
     */
    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(CONTEXT_PATH);
        return marshaller;
    }

    /**
     * Registers the {@link MessageDispatcherServlet} responsible for
     * handling incoming SOAP requests.
     * <p>
     * This servlet delegates SOAP messages to the appropriate
     * endpoint based on payload mapping.
     *
     * @param applicationContext Spring application context
     * @return servlet registration bean with configured URL mappings
     */
    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(
            ApplicationContext applicationContext
    ) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, URL_MAPPINGS);
    }

    /**
     * Defines the WSDL configuration for the Employee SOAP service.
     * <p>
     * This bean generates a WSDL 1.1 definition at runtime based on the
     * provided XSD schema and exposes it using the configured namespace,
     * port type, and location URI.
     *
     * @param schema XSD schema defining the SOAP contract
     * @return configured {@link DefaultWsdl11Definition}
     */
    @Bean(name = NAME_BEAN_SOAP)
    public DefaultWsdl11Definition employeeWsdlDefinition(XsdSchema schema) {
        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setPortTypeName(PORT_TYPE_NAME);
        definition.setLocationUri(LOCATION_URI);
        definition.setTargetNamespace(NAMESPACE_URI);
        definition.setSchema(schema);
        return definition;
    }

    /**
     * Loads the XSD schema used to define the SOAP service contract.
     *
     * @return {@link XsdSchema} loaded from the classpath
     */
    @Bean
    public XsdSchema employeeSchema() {
        return new SimpleXsdSchema(new ClassPathResource(XSD_FILE));
    }
}
