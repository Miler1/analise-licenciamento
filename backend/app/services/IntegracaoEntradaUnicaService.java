package services;

import br.ufla.lemaf.beans.historico.EmpreendimentoSobreposicao;
import br.ufla.lemaf.beans.historico.EmpreendimentoSobreposicaoVO;
import br.ufla.lemaf.beans.pessoa.Setor;
import exceptions.AppException;
import exceptions.ValidacaoException;
import exceptions.WebServiceException;
import br.ufla.lemaf.beans.EmpreendimentoFiltroResult;
import br.ufla.lemaf.beans.FiltroEmpreendimento;
import br.ufla.lemaf.beans.pessoa.FiltroPessoa;
import br.ufla.lemaf.beans.pessoa.Pessoa;
import br.ufla.lemaf.beans.pessoa.Usuario;
import models.UsuarioAnalise;
import models.licenciamento.Empreendimento;
import play.Logger;
import security.cadastrounificado.CadastroUnificadoWS;
import utils.Helper;
import utils.Mensagem;

import java.util.ArrayList;
import java.util.List;

public class IntegracaoEntradaUnicaService {


	public List<UsuarioAnalise> findUsuariosByPerfil(String codigoPerfil) {

		try {

			if(CadastroUnificadoWS.ws == null) {

				throw new AppException(Mensagem.ERRO_COMUNICACAO_ENTRADA_UNICA);
			}

			// Busca os usuários cadastrados e associados ao perfil informado
			FiltroPessoa filtroPessoa = new FiltroPessoa();
			filtroPessoa.codigoPerfil = codigoPerfil;
			filtroPessoa.isUsuario = true;

			return convertePessoaEuParaUsuarioAnalise(CadastroUnificadoWS.ws.getPessoasByFiltro(filtroPessoa));
		}
		catch (Exception e) {
			e.printStackTrace();
			Logger.error(e.getMessage());
			throw new WebServiceException(e.getMessage());
		}
	}

	public UsuarioAnalise findUsuarioByLogin(String login) {

		try {

			if(CadastroUnificadoWS.ws == null) {

				throw new AppException(Mensagem.ERRO_COMUNICACAO_ENTRADA_UNICA);
			}

			// Busca os usuários cadastrados e associados ao perfil informado
			FiltroPessoa filtroPessoa = new FiltroPessoa();
			filtroPessoa.login = login;
			filtroPessoa.isUsuario = true;

			List<UsuarioAnalise> usuariosAnalise = convertePessoaEuParaUsuarioAnalise(CadastroUnificadoWS.ws.getPessoasByFiltro(filtroPessoa));

			return usuariosAnalise != null && usuariosAnalise.size() > 0 ? usuariosAnalise.get(0) : null;
		}
		catch (Exception e) {
			e.printStackTrace();
			Logger.error(e.getMessage());
			throw new WebServiceException(e.getMessage());
		}
	}

	public List<UsuarioAnalise> findUsuariosByPerfilAndSetores(String codigoPerfil, List<String> siglaSetores) {

		try {

			if(CadastroUnificadoWS.ws == null) {

				throw new AppException(Mensagem.ERRO_COMUNICACAO_ENTRADA_UNICA);
			}

			StringBuilder stringBuilder = new StringBuilder();

			for(String sigla: siglaSetores){
				stringBuilder.append(',');
				stringBuilder.append(sigla);
			}

			return converteUsuarioEuParaUsuarioAnalise(CadastroUnificadoWS.ws.getUsuariosByPerfilAndSetores(codigoPerfil, stringBuilder.toString()));
		}
		catch (Exception e) {
			e.printStackTrace();
			Logger.error(e.getMessage());
			throw new WebServiceException(e.getMessage());
		}
	}

	public List<UsuarioAnalise> findUsuariosByPerfilAndSetor(String codigoPerfil, String siglaSetor) {

		try {

			if(CadastroUnificadoWS.ws == null) {

				throw new AppException(Mensagem.ERRO_COMUNICACAO_ENTRADA_UNICA);
			}

			return converteUsuarioEuParaUsuarioAnalise(CadastroUnificadoWS.ws.getUsuariosByPerfilAndSetores(codigoPerfil, siglaSetor));
		}
		catch (Exception e) {
			e.printStackTrace();
			Logger.error(e.getMessage());
			throw new WebServiceException(e.getMessage());
		}
	}

	public Setor getSetorBySigla(String siglaSetor) {

		try {

			if(CadastroUnificadoWS.ws == null) {

				throw new AppException(Mensagem.ERRO_COMUNICACAO_ENTRADA_UNICA);
			}

			return CadastroUnificadoWS.ws.getSetorBySigla(siglaSetor);
		}
		catch (Exception e) {
			e.printStackTrace();
			Logger.error(e.getMessage());
			throw new WebServiceException(e.getMessage());
		}
	}

	public List<String> getSiglasSetoresByNivel(String siglaSetor, int nivel){

		try {

			if(CadastroUnificadoWS.ws == null) {

				throw new AppException(Mensagem.ERRO_COMUNICACAO_ENTRADA_UNICA);
			}

			return CadastroUnificadoWS.ws.getSiglasSetoresByNivel(siglaSetor, nivel);
		}
		catch (Exception e) {
			e.printStackTrace();
			Logger.error(e.getMessage());
			throw new WebServiceException(e.getMessage());
		}
	}

	private  List<UsuarioAnalise> converteUsuarioEuParaUsuarioAnalise(List<Usuario> usuarios) {

		List<UsuarioAnalise> usuariosAnalise = new ArrayList<>();

		for (Usuario usuarioEU: usuarios) {

			UsuarioAnalise usuario = UsuarioAnalise.find("byLogin", Helper.desformatarCpfCnpj(usuarioEU.login)).first();

			if (usuario == null) {
				continue;
			}

			usuario.login = usuarioEU.login;
			usuario.usuarioEntradaUnica =  new models.EntradaUnica.Usuario(usuarioEU);
			usuariosAnalise.add(usuario);
		}

		return usuariosAnalise;
	}

	private  List<UsuarioAnalise> convertePessoaEuParaUsuarioAnalise(List<Pessoa> pessoas) {

		List<UsuarioAnalise> usuariosAnalise = new ArrayList<>();

		for (Pessoa pessoaEU: pessoas) {

			UsuarioAnalise usuario = UsuarioAnalise.find("byLogin", Helper.desformatarCpfCnpj(pessoaEU.usuario.login)).first();

			if (usuario == null) {
				usuario =  new UsuarioAnalise();
			}
			usuario.login = pessoaEU.usuario.login;
			usuario.nome = pessoaEU.nome != null ? pessoaEU.nome : pessoaEU.razaoSocial;
			usuario.usuarioEntradaUnica =  new models.EntradaUnica.Usuario(pessoaEU.usuario);
			usuariosAnalise.add(usuario);
		}

		return usuariosAnalise;
	}


	public br.ufla.lemaf.beans.Empreendimento findEmpreendimentosByCpfCnpj(String cpfCnpj) {

		try {

			FiltroEmpreendimento filtro = new FiltroEmpreendimento();

			filtro.cpfsCnpjs = new ArrayList<>();

			filtro.cpfsCnpjs.add(cpfCnpj);

			filtro.ordenacao = "DENOMINACAO_ASC";

			EmpreendimentoFiltroResult listaEmpEU = CadastroUnificadoWS.ws.buscarEmpreendimentosComFiltro(filtro);

			return listaEmpEU.totalItems > 0 ? listaEmpEU.pageItems.get(0) : null;

		} catch (WebServiceException e) {

			throw new AppException(Mensagem.ERRO_COMUNICACAO_ENTRADA_UNICA);

		}
	}

	public EmpreendimentoSobreposicao intersects(String cpfCnpj) {

		return CadastroUnificadoWS.ws.intersects(cpfCnpj);

	}

}
