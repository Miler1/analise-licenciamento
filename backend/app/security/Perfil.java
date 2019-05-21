package security;

import java.util.ArrayList;
import java.util.List;

public class Perfil {

	public Integer id;
	public String nome;
	public String codigo;
	public List<Permissao> permissoes;

	public Perfil(main.java.br.ufla.lemaf.beans.pessoa.Perfil perfil) {
		this.nome = perfil.nome;
		this.codigo = perfil.codigo;
		this.permissoes = new ArrayList<>();
		for(main.java.br.ufla.lemaf.beans.pessoa.Permissao permissaoEU : perfil.permissoes) {
			Permissao permissao = new Permissao();
			permissao.codigo = permissaoEU.codigo;
			this.permissoes.add(permissao);
		}
	}
}
