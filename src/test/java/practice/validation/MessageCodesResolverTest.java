package practice.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.ObjectError;

public class MessageCodesResolverTest {

    MessageCodesResolver codesResolver=new DefaultMessageCodesResolver();

    @Test
    void messageCodesResolverObject(){
        String[] messageCodes=codesResolver.resolveMessageCodes("required","item");
        for(String messageCode: messageCodes){
            System.out.println("messageCode = "+messageCode);
        }
        Assertions.assertThat(messageCodes).containsExactly("required.item","required");
    }
    // 결과
    //      messageCode = required.item
    //      messageCode = required

    @Test
    void messageCodesResolverField(){
        String[] messageCodes=codesResolver.resolveMessageCodes("required","item","itemName",String.class);
        for(String messageCode: messageCodes){
            System.out.println("messageCode = "+messageCode);
        }
        Assertions.assertThat(messageCodes).containsExactly(
                "required.item.itemName",
                "required.itemName",
                "required.java.lang.String",
                "required"
        );
    }
    // 결과 (위에 있을수록 우선순위 높음)
    //      messageCode = required.item.itemName
    //      messageCode = required.itemName
    //      messageCode = required.java.lang.String
    //      messageCode = required

    // DefaultMessageCodesResolver 기본 메시지 생성 순서
    //      코드.세부적인 사항
    //      코드.간편화된 사항
    //      코드
}
