package utils.validacao;

import java.lang.annotation.ElementType;
import java.util.Collection;

import net.sf.oval.Validator;
import net.sf.oval.context.OValContext;
import play.Logger;
import play.Play;
import play.Play.Mode;
import play.data.validation.RequiredCheck;
import play.data.validation.Validation;
import play.data.validation.Error;
import play.db.Model.BinaryField;
import play.exceptions.UnexpectedException;
import utils.Mensagem;
import exceptions.ValidacaoException;

public class Validacao {

	public static void validar(Object obj) {
		
		Validation validation = Validation.current();
		validation.valid(obj);
		
		if (validation.hasErrors()) {
			
			if (Play.mode == Mode.DEV && Play.runingInTestMode())
				logErrors(validation);
			
			throw new ValidacaoException(Mensagem.ERRO_VALIDACAO, validation);
		}
	}
	
	private static void logErrors(Validation validation) {
		
		Logger.info("-- Erros de validação:");
		
		for (Error e : validation.errors()) {
			
			Logger.info(e.getKey() + ": " + e.message());
		}
	}
	
	public static void validateRequired(Object ... values) {
		
		if (!checkRequired(values))
			throw new ValidacaoException(Mensagem.ERRO_VALIDACAO);
	}
	
	public static boolean checkRequired(Object ... values) {

		for (Object obj : values) {
			
			if (!checkRequiredValue(obj))
				return false;
		}
		
		return true;
	}
	
	private static boolean checkRequiredValue(Object value) {
		
        if (value == null) {
            return false;
        }
        if (value instanceof String) {
            return value.toString().trim().length() > 0;
        }
        if (value instanceof Collection<?>) {
            return ((Collection<?>)value).size() > 0;
        }
        if (value instanceof BinaryField) {
            return ((BinaryField)value).exists();
        }
        if (value.getClass().isArray()) {
            try {
                return java.lang.reflect.Array.getLength(value) > 0;
            } catch(Exception e) {
                throw new UnexpectedException(e);
            }
        }
        
	    return true;
	}
}
