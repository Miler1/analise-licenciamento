package models.EntradaUnica;

import main.java.br.ufla.lemaf.beans.pessoa.Perfil;
import main.java.br.ufla.lemaf.beans.pessoa.Permissao;

import java.io.Serializable;
import java.util.List;

public class Usuario implements Serializable{
	public Integer id;
	public String login;
	public String nome;
	public String email;
	public List<Perfil> perfis;
	public List<br.ufla.lemaf.beans.pessoa.Setor> setores;
	public Perfil perfilSelecionado;
	public br.ufla.lemaf.beans.pessoa.Setor setorSelecionado;
	public String sessionKeyEntradaUnica;
	public boolean autenticadoViaToken;

	public Usuario() {
	}
	public Usuario(main.java.br.ufla.lemaf.beans.pessoa.Usuario usuario) {

		this.id = usuario.id;
		this.login = usuario.login;
		this.nome = usuario.nome;
		this.email = usuario.email;
		this.perfis = usuario.perfis;
		this.setores = usuario.setores;
		this.sessionKeyEntradaUnica = usuario.sessionKeyEntradaUnica;

	}
	public boolean hasPermissao(String codigoPermissao) {

		for (Permissao permissao : this.perfilSelecionado.permissoes) {

			if (permissao.codigo.equals(codigoPermissao)) {

				return true;
			}
		}

		return false;
	}

	public boolean possuiPerfil(String codigoPerfil) {

		for (main.java.br.ufla.lemaf.beans.pessoa.Perfil perfilUsuario : perfis) {

			if (perfilUsuario.codigo.equals(codigoPerfil)) {

				return true;
			}
		}

		return false;
	}

}
