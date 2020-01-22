package controllers;

import builders.ProcessoBuilder.FiltroProcesso;
import models.AnaliseJuridica;
import models.Processo;
import models.licenciamento.Caracterizacao;
import models.licenciamento.Questionario3;
import security.Acao;
import security.Auth;
import serializers.AnaliseJuridicaSerializer;
import serializers.ProcessoSerializer;
import serializers.Questionario3Serializer;

import java.util.List;

public class Processos extends InternalController {

	public static void listWithFilter(FiltroProcesso filtro){
		
		verificarPermissao(Acao.LISTAR_PROCESSO);
		
		List processosList = Processo.listWithFilter(filtro, Auth.getUsuarioSessao());
		
		renderJSON(processosList);
	}
	
	public static void  countWithFilter(FiltroProcesso filtro){
		
		verificarPermissao(Acao.LISTAR_PROCESSO);
		
		renderJSON(Processo.countWithFilter(filtro, Auth.getUsuarioSessao()));
	}
	
	public static void findById(Long idProcesso) {
			
		renderJSON(Processo.findById(idProcesso), ProcessoSerializer.list);
	}

	public static void findByNumProcesso(String numProcesso) {

		verificarPermissao(Acao.VALIDAR_PARECER_JURIDICO, Acao.VALIDAR_PARECER_TECNICO, Acao.INICIAR_PARECER_JURIDICO, Acao.INICIAR_PARECER_TECNICO, Acao.VALIDAR_PARECERES_JURIDICO_TECNICO);

		renderJSON(Processo.findByNumProcesso(numProcesso.replace('-','/')), ProcessoSerializer.getInfo);
	}

	public static void getInfoProcesso(Long id) {

		verificarPermissao(Acao.VALIDAR_PARECER_GEO, Acao.INICIAR_PARECER_GEO,Acao.VALIDAR_PARECERES);

		Processo processo = Processo.findById(id);

		renderJSON(processo.getInfoProcesso(), ProcessoSerializer.getInfo);

	}

	public static void findAnaliseJuridica(Long idProcesso) {
		
		verificarPermissao(Acao.VALIDAR_PARECER_JURIDICO, Acao.VALIDAR_PARECER_TECNICO, Acao.INICIAR_PARECER_JURIDICO);
		
		Processo processo = Processo.findById(idProcesso);
		
		AnaliseJuridica analise = AnaliseJuridica.findByProcessoAtivo(processo);
		
		renderJSON(analise, AnaliseJuridicaSerializer.findInfo);

	}

}
