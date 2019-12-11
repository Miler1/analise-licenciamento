package models.EntradaUnica;

import enums.PerfilEnum;
import enums.SetorEnum;
import main.java.br.ufla.lemaf.beans.pessoa.Perfil;
import main.java.br.ufla.lemaf.beans.pessoa.Permissao;
import models.PerfilUsuarioAnalise;
import models.SetorUsuarioAnalise;
import models.UsuarioAnalise;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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

	public List<PerfilUsuarioAnalise> salvarPerfis(UsuarioAnalise usuarioAnalise) {

		List<PerfilUsuarioAnalise> perfisUsuarioAnalise = new ArrayList<>();

		if(usuarioAnalise.perfis == null || usuarioAnalise.perfis.isEmpty()){
			this.perfis.forEach(perfil -> {

				  PerfilUsuarioAnalise perfilUsuarioAnalise = new PerfilUsuarioAnalise(perfil,usuarioAnalise);
				  perfisUsuarioAnalise.add(perfilUsuarioAnalise.save());
			});


			return perfisUsuarioAnalise;
		}

		this.perfis.forEach(perfil -> {

			if(PerfilEnum.getList().contains(perfil.codigo)){

				if(usuarioAnalise.containsPerfil(perfil.codigo, this)) {

					PerfilUsuarioAnalise perfilUsuarioAnalise = new PerfilUsuarioAnalise(perfil,usuarioAnalise);
					perfisUsuarioAnalise.add(perfilUsuarioAnalise.save());

				}
			}
		});
		usuarioAnalise.perfis = perfisUsuarioAnalise;

		usuarioAnalise.perfis.forEach(perfilUsuarioAnalise -> {

			if(this.containsPerfil(perfilUsuarioAnalise.codigoPerfil)){

				if(PerfilUsuarioAnalise.findById(perfilUsuarioAnalise.id) != null ){

					perfilUsuarioAnalise._delete();

				}
			}

		});

		return usuarioAnalise.perfis;

	}

	public List<SetorUsuarioAnalise> salvarSetores(UsuarioAnalise usuarioAnalise ) {

		List<SetorUsuarioAnalise> setores = new ArrayList<>();

		if(usuarioAnalise.setores == null || usuarioAnalise.setores.isEmpty()){
			this.setores.forEach(setor -> {
				if(SetorEnum.getList().contains(setor.sigla)){

					setores.add(new SetorUsuarioAnalise(setor, usuarioAnalise));
				}
			});

			return setores;
		}

		this.setores.forEach(setor -> {

			if(SetorEnum.getList().contains(setor.sigla)){

				if(usuarioAnalise.containsSetor(setor.sigla, this)) {

					setores.add(new SetorUsuarioAnalise(setor, usuarioAnalise));
				}
			}
		});
		usuarioAnalise.setores = setores;
		usuarioAnalise.setores.forEach(setorUsuarioAnalise -> {

			if(this.containsSetor(setorUsuarioAnalise.siglaSetor)){

				if(SetorUsuarioAnalise.findById(setorUsuarioAnalise.id) != null ){

					setorUsuarioAnalise._delete();

				}
			}else{
				setorUsuarioAnalise.save();
			}

		});

		return usuarioAnalise.setores;

	}

	public boolean containsSetor (String siglaSetor ){

		return this.setores.stream().filter(setor ->  setor.sigla.equals(siglaSetor)).collect(Collectors.toList()).size() > 0;

	}

	public boolean containsPerfil (String codigoPerfil ){

		return this.perfis.stream().anyMatch(perfil ->  perfil.codigo.equals(codigoPerfil));

	}
}
