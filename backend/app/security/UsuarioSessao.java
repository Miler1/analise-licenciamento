package security;


import main.java.br.ufla.lemaf.beans.pessoa.Permissao;
import main.java.br.ufla.lemaf.beans.pessoa.Usuario;

import java.util.List;

public class UsuarioSessao extends Usuario {

	public Perfil perfil;
	public Boolean primeiroAcesso = false;
	public Boolean acessoLiberado = false;

	public UsuarioSessao() {}

	public UsuarioSessao(Usuario usuario) {

		this.id = usuario.id;
		this.login = usuario.login;
		this.nome = usuario.nome;
		this.email = usuario.email;
		this.perfis = usuario.perfis;
		this.sessionKeyEntradaUnica = usuario.sessionKeyEntradaUnica;

	}
	
	public List<Permissao> getPermissoes() {
		
		return this.perfilSelecionado.permissoes;
	}

	public boolean possuiPerfil(String codigoPerfil) {

		for (main.java.br.ufla.lemaf.beans.pessoa.Perfil perfilUsuario : perfis) {

			if (perfilUsuario.codigo.equals(codigoPerfil)) {

				return true;
			}
		}

		return false;
	}

	public main.java.br.ufla.lemaf.beans.pessoa.Perfil getPerfilUsuario(String codigoPerfil) {

		for (main.java.br.ufla.lemaf.beans.pessoa.Perfil perfilUsuario : perfis) {

			if (perfilUsuario.codigo.equals(codigoPerfil)) {

				return perfilUsuario;
			}
		}

		return null;
	}
	
}