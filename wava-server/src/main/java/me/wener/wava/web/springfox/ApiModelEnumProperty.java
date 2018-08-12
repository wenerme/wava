package me.wener.wava.web.springfox;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 实体属性上的枚举属性引用, 当实体上为原子类型, 但实际为一个枚举是使用 <br>
 * NOTE: 尽量避免使用该注解,应该使用 JsonFormat 等方式来控制序列化和反序列化的结果, 实际字段尽量使用枚举类型
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 16/6/16
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RUNTIME)
public @interface ApiModelEnumProperty {

  Class<? extends Enum> value();
}
