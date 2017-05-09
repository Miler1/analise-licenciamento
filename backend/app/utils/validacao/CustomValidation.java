package utils.validacao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import play.data.validation.ValidCheck;
import net.sf.oval.configuration.annotation.Constraint;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Constraint(checkWith = CustomValidationCheck.class)
public @interface CustomValidation {

}