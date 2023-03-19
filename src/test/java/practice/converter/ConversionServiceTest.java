package practice.converter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;
import practice.web.IpPort;
import practice.web.converter.IntegerToStringConverter;
import practice.web.converter.IpPortToStringConverter;
import practice.web.converter.StringToIntegerConverter;
import practice.web.converter.StringToIpPortConverter;

import static org.assertj.core.api.Assertions.*;

public class ConversionServiceTest {

    @Test
    void conversionService(){
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new StringToIntegerConverter());
        conversionService.addConverter(new IntegerToStringConverter());
        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());

        assertThat(conversionService.convert("10",Integer.class)).isEqualTo(10);
        assertThat(conversionService.convert(10,String.class)).isEqualTo("10");

        IpPort ip = conversionService.convert("127.0.0.1:8080", IpPort.class);
        assertThat(ip).isEqualTo(new IpPort("127.0.0.1",8080));

        String ipString = conversionService.convert(new IpPort("127.0.0.1",8080),String.class);
        assertThat(ipString).isEqualTo("127.0.0.1:8080");
    }
}
