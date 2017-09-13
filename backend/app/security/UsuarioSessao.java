package security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import models.licenciamento.PessoaFisica;
import models.portalSeguranca.Perfil;
import models.portalSeguranca.Setor;

public class UsuarioSessao implements Serializable {

	public Long id;
	public String nome;
	public Long idPessoa;
	public String cpfCnpj;
	public List<Acao> acoesPermitidas;
	public Perfil perfilSelecionado;
	public Setor setorSelecionado;
	public Boolean autenticadoViaToken;
	
	public UsuarioSessao() {
		
	}
	
	public UsuarioSessao(Long id, String nome, Long idPessoa, String cpfCnpj, List<Acao> acoesPermitidas, Perfil perfilSelecionado, Setor setorSelecionado, Boolean autenticadoViaToken) {
	
		this.id = id;
		this.nome = nome;
		this.idPessoa = idPessoa;
		this.cpfCnpj = cpfCnpj;
		this.acoesPermitidas = acoesPermitidas;
		this.perfilSelecionado = perfilSelecionado;
		this.setorSelecionado = setorSelecionado;
		this.autenticadoViaToken = autenticadoViaToken;
	}
	
	public boolean possuiPermissao(Acao acao) {
		
		return this.acoesPermitidas.contains(acao);
	}
	
	public List<Acao> getPermissoes() {
		
		return this.acoesPermitidas;
	}

	public void setAcoesPermitidas(List<Acao> acoes) {
		
		this.acoesPermitidas = new ArrayList<>(acoes);
	}
	
	public PessoaFisica findPessoa() {
		
		if (idPessoa == null)
			return null;
		
		return PessoaFisica.findById(idPessoa);
	}
}