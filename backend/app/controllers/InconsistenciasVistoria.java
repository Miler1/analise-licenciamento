package controllers;

import models.InconsistenciaVistoria;
import security.Acao;
import serializers.InconsistenciaVistoriaSerializer;

public class InconsistenciasVistoria extends InternalController {

	public static void salvar(InconsistenciaVistoria inconsistenciaVistoria) {

		verificarPermissao(Acao.SALVAR_INCONSISTENCIA_VISTORIA, Acao.BUSCAR_INCONSISTENCIA_VISTORIA);

		renderJSON(inconsistenciaVistoria.salvar(), InconsistenciaVistoriaSerializer.find);

	}

	public static void deletar(Long id) {

		verificarPermissao(Acao.DELETAR_INCONSISTENCIA_VISTORIA);

		InconsistenciaVistoria inconsistenciaVistoria = InconsistenciaVistoria.findById(id);

		renderText(inconsistenciaVistoria.deletar());

	}

}
