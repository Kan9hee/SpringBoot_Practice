package practice.web.resolver.argumentresolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {
}
// 임의로 만든 어노테이션. 파라미터에만 적용되고, 정보가 런타임동안 남아있다.
