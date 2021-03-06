package controllers;

import builders.ProcessoManejoBuilder.FiltroProcessoManejo;
import models.Documento;
import models.manejoDigital.ProcessoManejo;
import models.UsuarioAnalise;
import security.Acao;
import security.Auth;
import security.AuthManejo;
import serializers.ProcessoManejoSerializer;
import utils.Mensagem;

import java.util.List;

public class ProcessosManejo extends InternalController {

	public static void save(ProcessoManejo processo) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		notFoundIfNull(processo);

		processo.save();

		renderMensagem(Mensagem.PROCESSO_MANEJO_CADASTRADO_COM_SUCESSO);
	}

	public static void findById(Long id) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO, Acao.VISUALIZAR_PROCESSO_MANEJO);

		notFoundIfNull(id);

		ProcessoManejo processo = ProcessoManejo.findById(id);

		renderJSON(processo, ProcessoManejoSerializer.findById);
	}

	public static void iniciarAnaliseShape(ProcessoManejo processo) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		notFoundIfNull(processo);

		ProcessoManejo processoSalvo = ProcessoManejo.findById(processo.id);

		notFoundIfNull(processoSalvo);

		String token = AuthManejo.getToken(session.current().getId());

		processoSalvo.iniciarAnaliseShape(processo, token);

		renderJSON(Mensagem.ANALISE_SHAPE_INICIADA_COM_SUCESSO);
	}

	public static void iniciarAnaliseTecnica(ProcessoManejo processo) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		notFoundIfNull(processo);

		ProcessoManejo processoSalvo = ProcessoManejo.findById(processo.id);

		notFoundIfNull(processoSalvo);

		processoSalvo.iniciarAnaliseTecnica();

		renderJSON(processoSalvo, ProcessoManejoSerializer.iniciarAnalise);
	}

	public static void downloadPdfAnalise(ProcessoManejo processoManejo) throws Exception {

		verificarPermissao(Acao.VISUALIZAR_PROCESSO_MANEJO);

		notFoundIfNull(processoManejo);

		ProcessoManejo processoManejoSalvo = ProcessoManejo.find("numeroProcesso", processoManejo.numeroProcesso).first();

		notFoundIfNull(processoManejoSalvo);

		Documento pdfAnalise = processoManejoSalvo.getAnaliseTecnica().gerarPDFAnalise();

		String nome = pdfAnalise.tipo.prefixoNomeArquivo + "_" + processoManejoSalvo.getAnaliseTecnica().id + ".pdf";
		nome = nome.replace(' ', '_');
		response.setHeader("Content-Disposition", "attachment; filename=" + nome);
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Type", "application/pdf");

		renderBinary(pdfAnalise.arquivo, nome);
	}

	public static void listWithFilter(FiltroProcessoManejo filtro) {

		verificarPermissao(Acao.LISTAR_PROCESSO_MANEJO);

		List processosList = ProcessoManejo.listWithFilter(filtro);

		renderJSON(processosList);
	}

	public static void findProcessoCompletoById(Long id) {

		verificarPermissao(Acao.VISUALIZAR_PROCESSO_MANEJO);

		notFoundIfNull(id);

		ProcessoManejo processo = ProcessoManejo.findById(id);

		renderJSON(processo, ProcessoManejoSerializer.findCompletoById);
	}

	public static void countWithFilter(FiltroProcessoManejo filtro) {

		verificarPermissao(Acao.LISTAR_PROCESSO_MANEJO);

		renderJSON(ProcessoManejo.countWithFilter(filtro));
	}

	public static void findByNumeroProcesso(String numeroProcesso) {

		notFoundIfNull(numeroProcesso);

		verificarPermissao(Acao.CADASTRAR_PROCESSO_MANEJO);

		renderJSON(ProcessoManejo.findByNumeroProcesso(numeroProcesso));
	}

	public static void indeferir(ProcessoManejo processoManejo) {

		notFoundIfNull(processoManejo);
		notFoundIfNull(processoManejo.id);

		ProcessoManejo processoSalvo = ProcessoManejo.findById(processoManejo.id);

		notFoundIfNull(processoSalvo);

		processoSalvo.indeferir(processoManejo, (UsuarioAnalise) UsuarioAnalise.find("login", Auth.getUsuarioSessao().login).first());

		renderMensagem(Mensagem.PROCESSO_MANEJO_INDEFERIDO_COM_SUCESSO);
	}

}