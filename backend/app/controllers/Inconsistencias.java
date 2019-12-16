package controllers;

import exceptions.ValidacaoException;
import models.Inconsistencia;
import models.InconsistenciaTecnica;
import models.licenciamento.AtividadeCaracterizacao;
import security.Acao;
import serializers.InconsistenciaSerializer;
import utils.Mensagem;

import java.util.Objects;

public class Inconsistencias extends InternalController{

    public static void salvarInconsistenciaGeo(Inconsistencia inconsistencia) {

        verificarPermissao(Acao.SALVAR_INCONSISTENCIA_GEO);

        returnIfNull(inconsistencia, "Inconsistencia");

        Inconsistencia novaInconsistenciaGeo = inconsistencia.salvaInconsistenciaGeo();

        renderJSON(novaInconsistenciaGeo,InconsistenciaSerializer.findInconsistencia);

    }

    public static void findInconsistenciaGeo(Inconsistencia inconsistencia) {

        verificarPermissao(Acao.BUSCAR_INCONSISTENCIA_GEO);

        returnIfNull(inconsistencia, "Inconsistencia");

        Inconsistencia novaInconsistenciaGeo = inconsistencia.buscarInconsistenciaGeo();

        renderJSON(novaInconsistenciaGeo, InconsistenciaSerializer.findInconsistencia);

    }

    public static void excluirInconsistenciaGeo(Long id) {

        returnIfNull(id, "Long");

        verificarPermissao(Acao.EXCLUIR_INCONSISTENCIA_GEO);

        Inconsistencia i = Inconsistencia.findById(id);

        i.deleteAnexos();

        i.delete();

        renderText(Mensagem.INCONSISTENCIA_EXCLUIDA_SUCESSO.getTexto());

    }

    public static void findById(Long id) {

        Inconsistencia inconsistencia = Inconsistencia.findById(id);

        renderJSON(inconsistencia, InconsistenciaSerializer.findInconsistencia);

    }

    public static void salvarInconsistenciaTecnica(InconsistenciaTecnica inconsistenciaTecnica) {

        verificarPermissao(Acao.SALVAR_INCONSISTENCIA_TECNICA);

        returnIfNull(inconsistenciaTecnica,"InconsistenciaTecnica");

        InconsistenciaTecnica novaInconsistenciaTecnica = inconsistenciaTecnica.salvaInconsistenciaTecnica();

        renderJSON(novaInconsistenciaTecnica, InconsistenciaSerializer.findInconsistencia);

    }

    public static void findInconsistenciaTecnica(Long id){

        verificarPermissao(Acao.BUSCAR_INCONSISTENCIA_TECNICA);

        returnIfNull(id, "Long");

        InconsistenciaTecnica novaInconsistenciaTecnica =  InconsistenciaTecnica.findById(id);

        renderJSON(novaInconsistenciaTecnica, InconsistenciaSerializer.findInconsistencia);

    }

    public static void excluirInconsistenciaTecnica(InconsistenciaTecnica inconsistenciaTecnica) {

        verificarPermissao(Acao.EXCLUIR_INCONSISTENCIA_TECNICA);

        returnIfNull(inconsistenciaTecnica, "InconsistenciaTecnica");

        inconsistenciaTecnica.excluiInconsistenciaTecnica();

        renderText(Mensagem.INCONSISTENCIA_EXCLUIDA_SUCESSO.getTexto());

    }

}
