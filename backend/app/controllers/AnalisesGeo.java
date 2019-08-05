package controllers;

import models.*;
import models.geocalculo.Geoserver;
import models.licenciamento.Empreendimento;
import org.apache.commons.io.FileUtils;
import security.Acao;
import serializers.AnaliseGeoSerializer;
import serializers.EmpreendimentoSerializer;
import utils.Mensagem;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

public class AnalisesGeo extends InternalController {

    public static void iniciar(AnaliseGeo analise) {

        verificarPermissao(Acao.INICIAR_PARECER_GEO);

        AnaliseGeo analiseAAlterar = AnaliseGeo.findById(analise.id);

        UsuarioAnalise usuarioExecutor = getUsuarioSessao();

        analiseAAlterar.iniciar(usuarioExecutor);

        renderMensagem(Mensagem.ANALISE_GEO_INICIADA_SUCESSO);

    }

    public static void concluir(AnaliseGeo analise) {

        verificarPermissao(Acao.INICIAR_PARECER_GEO);

        AnaliseGeo analiseAAlterar = AnaliseGeo.findById(analise.id);

        UsuarioAnalise usuarioExecutor = getUsuarioSessao();

        analiseAAlterar.finalizar(analise, usuarioExecutor);

        renderMensagem(Mensagem.ANALISE_CONCLUIDA_SUCESSO);

    }

    public static void findByNumeroProcesso() {

        verificarPermissao(Acao.INICIAR_PARECER_GEO);

        String numeroProcesso = getParamAsString("numeroProcesso");

        AnaliseGeo analise = AnaliseGeo.findByNumeroProcesso(numeroProcesso);

        renderJSON(analise, AnaliseGeoSerializer.parecer);

    }

    public static void alterar(AnaliseGeo analise) {

        verificarPermissao(Acao.INICIAR_PARECER_GEO);

        AnaliseGeo analiseAAlterar = AnaliseGeo.findById(analise.id);

        analiseAAlterar.update(analise);

        renderMensagem(Mensagem.ANALISE_CADASTRADA_SUCESSO);
    }

    public static void findById(Long idAnaliseGeo) {

        verificarPermissao(Acao.VALIDAR_PARECER_GEO, Acao.INICIAR_PARECER_GEO, Acao.VALIDAR_PARECERES);

        AnaliseGeo analise = AnaliseGeo.findById(idAnaliseGeo);

        renderJSON(analise, AnaliseGeoSerializer.findInfo);

    }

    public static void getRestricoesGeo(Long idAnaliseGeo) throws Exception {

        verificarPermissao(Acao.INICIAR_PARECER_GEO);

        AnaliseGeo analiseGeo = AnaliseGeo.findById(idAnaliseGeo);

        File file = Geoserver.verificarRestricoes(
                analiseGeo.analise.processo.empreendimento.coordenadas,
                analiseGeo.analise.processo.empreendimento.imovel,
                "analise-geo-id-" + idAnaliseGeo
        );

        renderJSON(FileUtils.readFileToString(file, Charset.defaultCharset()));
    }

    public static void validarParecer(AnaliseGeo analise) {

        verificarPermissao(Acao.VALIDAR_PARECER_GEO);

        AnaliseGeo analiseAValidar = AnaliseGeo.findById(analise.id);

        UsuarioAnalise usuarioExecutor = getUsuarioSessao();

        analiseAValidar.validaParecer(analise, usuarioExecutor);

        renderMensagem(Mensagem.VALIDACAO_PARECER_GEO_CONCLUIDA_SUCESSO);
    }

    public static void validarParecerGerente(AnaliseGeo analise) {

        verificarPermissao(Acao.VALIDAR_PARECER_GEO);

        AnaliseGeo analiseAValidar = AnaliseGeo.findById(analise.id);

        UsuarioAnalise usuarioExecutor = getUsuarioSessao();

        analiseAValidar.validaParecerGerente(analise, usuarioExecutor);

        renderMensagem(Mensagem.VALIDACAO_PARECER_GEO_CONCLUIDA_SUCESSO);
    }

    public static void validarParecerAprovador(AnaliseGeo analise) {

        verificarPermissao(Acao.VALIDAR_PARECERES);

        AnaliseGeo analiseAValidar = AnaliseGeo.findById(analise.id);

        UsuarioAnalise usuarioExecutor = getUsuarioSessao();

        analiseAValidar.validarParecerValidacaoAprovador(analise, usuarioExecutor);

        renderMensagem(Mensagem.VALIDACAO_PARECER_APROVADOR_CONCLUIDA_SUCESSO);
    }

    public static void verificaAnexosEmpreendimento(String cpfCnpjEmpreendimento) {

        Empreendimento empreendimento = Empreendimento.buscaEmpreendimentoByCpfCnpj(cpfCnpjEmpreendimento);

        renderJSON(empreendimento.possuiShape, EmpreendimentoSerializer.getDadosEmpreendimento);
    }

    public static void downloadPDFParecer(AnaliseGeo analiseGeo) throws Exception {

        verificarPermissao(Acao.INICIAR_PARECER_GEO);

        String novoParecer = analiseGeo.parecer;

        AnaliseGeo analiseGeoSalva = AnaliseGeo.findById(analiseGeo.id);

        analiseGeoSalva.parecer = novoParecer;

        Documento pdfParecer = analiseGeoSalva.gerarPDFParecer();

        String nome = pdfParecer.tipo.nome +  "_" + analiseGeoSalva.id + ".pdf";
        nome = nome.replace(' ', '_');
        response.setHeader("Content-Disposition", "attachment; filename=" + nome);
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Type", "application/pdf");

        renderBinary(pdfParecer.arquivo, nome);

    }

    public static void downloadPDFNotificacao(AnaliseGeo analiseGeo) throws Exception {

        verificarPermissao(Acao.INICIAR_PARECER_GEO);

        analiseGeo.analise = Analise.findById(analiseGeo.analise.id);

        List<Notificacao> notificacaos = Notificacao.gerarNotificacoesTemporarias(analiseGeo);

        Documento pdfNotificacao = Notificacao.gerarPDF(notificacaos, analiseGeo);

        String nome = pdfNotificacao.tipo.nome +  "_" + analiseGeo.id + ".pdf";
        nome = nome.replace(' ', '_');
        response.setHeader("Content-Disposition", "attachment; filename=" + nome);
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Type", "application/pdf");

        renderBinary(pdfNotificacao.arquivo, nome);

    }

    public static void buscaDadosAreaProjeto(String numeroProcesso) {

        returnIfNull(numeroProcesso, "String");

        Processo processo = Processo.findByNumProcesso(numeroProcesso);

        processo.getDadosAreaProjeto();

        //renderJSON(empreendimento.possuiShape, EmpreendimentoSerializer.getDadosEmpreendimento);
    }

}