package utils.validacao;

import java.util.Collection;

import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.OValContext;
import play.db.Model.BinaryField;
import play.exceptions.UnexpectedException;

@SuppressWarnings("serial")
public class CustomValidationCheck extends AbstractAnnotationCheck<CustomValidation> {
    
    static final String mes = "validation.required";

    @Override
    public boolean isSatisfied(Object validatedObject, Object value, OValContext context, Validator validator) {
       
    	if (value == null)
            return true;

    	if (value instanceof ICustomValidation) {
            
    		return ((ICustomValidation) value).isValid();
            
        } else {
        	
        	throw new RuntimeException("Para utilizar a anotação @" + CustomValidation.class.getSimpleName()
        			+ " é necessário implementar a interface " + ICustomValidation.class.getSimpleName() + ".");
        }
    }
}