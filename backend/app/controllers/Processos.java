package controllers;

import builders.ProcessoBuilder.FiltroProcesso;
import models.AnaliseJuridica;
import models.Processo;
import org.geotools.feature.SchemaException;
import security.Acao;
import security.Auth;
import serializers.AnaliseJuridicaSerializer;
import serializers.ProcessoSerializer;

import java.io.IOException;
import java.util.List;

public class Processos extends InternalController {

	public static void listWithFilter(FiltroProcesso filtro){
		
		verificarPermissao(Acao.LISTAR_PROCESSO);
		
		List processosList = Processo.listWithFilter(filtro, Auth.getUsuarioSessao());

		renderJSON(processosList);
	}

	public static void getProcessosAnteriores(Long idProcessoAnterior){

		verificarPermissao(Acao.VISUALIZAR_PROTOCOLO);

		Processo processoAntigo = Processo.findById(idProcessoAnterior);

		List processosList = Processo.getProcessosAnteriores(processoAntigo);

		renderJSON(processosList, ProcessoSerializer.getInfo);
	}
	
	public static void  countWithFilter(FiltroProcesso filtro){
		
		verificarPermissao(Acao.LISTAR_PROCESSO);
		
		renderJSON(Processo.countWithFilter(filtro, Auth.getUsuarioSessao()));
	}
	
	public static void findById(Long idProcesso) {
			
		renderJSON(Processo.findById(idProcesso), ProcessoSerializer.list);
	}

	public static void findByNumProcesso(String numProcesso) {

		renderJSON(Processo.findByNumProcesso(numProcesso.replace('-','/')), ProcessoSerializer.getInfo);
	}

	public static void getInfoProcesso(Long id) {

		//verificarPermissao(Acao.VISUALIZAR_PROTOCOLO);

		Processo processo = Processo.findById(id);

		renderJSON(processo.getInfoProcesso(), ProcessoSerializer.getInfo);

	}

	public static void findAnaliseJuridica(Long idProcesso) {
		
		verificarPermissao(Acao.VALIDAR_PARECER_JURIDICO, Acao.VALIDAR_PARECER_TECNICO, Acao.INICIAR_PARECER_JURIDICO);
		
		Processo processo = Processo.findById(idProcesso);
		
		AnaliseJuridica analise = AnaliseJuridica.findByProcessoAtivo(processo);
		
		renderJSON(analise, AnaliseJuridicaSerializer.findInfo);

	}

    public static void baixarShapefile(Long idProcesso) throws IOException, SchemaException {

        verificarPermissao(Acao.VISUALIZAR_PROTOCOLO);

        Processo processo = Processo.findById(idProcesso);
        renderBinary(processo.gerarShape());

    }

	public static void baixarShapefileAtividades(Long idProcesso) throws IOException, SchemaException {

		verificarPermissao(Acao.VISUALIZAR_PROTOCOLO);

		Processo processo = Processo.findById(idProcesso);
		renderBinary(processo.gerarShapeAtividades());

	}

}
