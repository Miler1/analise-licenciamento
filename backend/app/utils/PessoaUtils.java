package utils;

import java.util.Collection;
import java.util.List;

import models.licenciamento.Pessoa;
import models.licenciamento.PessoaFisica;

public class PessoaUtils {

	public static interface IPessoa {

		Pessoa getPessoa();
	}
	
	public static interface IPessoaFisica extends IPessoa {

		PessoaFisica getPessoa();
	}

	
	public static <T extends IPessoa> T getByPessoa(Pessoa pessoa, Collection<T> objs) {
		
		for (T item : objs) {
			
			Pessoa pessoaItem = item.getPessoa();
			
			if (pessoaItem != null && pessoaItem.id != null && pessoaItem.id.equals(pessoa.id))
				return item;
		}
		
		return null;
	}
	
	public static <T extends IPessoa> boolean contemPessoa(Pessoa pessoa, Collection<T> objs) {
		
		return getByPessoa(pessoa, objs) != null;
	}
	
	public static <T extends IPessoa> boolean contemPessoasRepetidas(List<T> objs) {
		
		Pessoa p1 = null;
		Pessoa p2 = null;
		int size = objs.size();
		
		if (objs == null || objs.isEmpty())
			return false;
		
		for (int i = 0; i < size - 1; i++) {
			
			for (int j = i + 1; j < size; j++) {
				
				p1 = objs.get(i).getPessoa();
				p2 = objs.get(j).getPessoa();
				
				if (p1 != null && p2 != null && p1.id != null && p2.id != null && p1.id.equals(p2.id))
					return true;
			}
		}
		
		return false;
	}
	
	public static <T extends IPessoaFisica> T getByCpf(String cpf, Collection<T> pessoas) {
		
		for (T p : pessoas) {
			
			if (p.getPessoa() != null && cpf.equals(p.getPessoa().cpf))
				return p;
		}
		
		return null;
	}
}
