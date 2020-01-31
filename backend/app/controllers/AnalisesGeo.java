package controllers;

import models.*;
import models.geocalculo.Geoserver;
import models.licenciamento.Empreendimento;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import security.Acao;
import serializers.AnaliseGeoSerializer;
import serializers.CamadaGeoAtividadeSerializer;
import serializers.EmpreendimentoSerializer;
import utils.Mensagem;

import javax.validation.ValidationException;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.List;

public class AnalisesGeo extends InternalController {

    public static void iniciar(AnaliseGeo analise) {

        verificarPermissao(Acao.INICIAR_PARECER_GEO);

        AnaliseGeo analiseAlterar = AnaliseGeo.findById(analise.id);

        UsuarioAnalise usuarioExecutor = getUsuarioSessao();

        analiseAlterar.iniciar(usuarioExecutor);

        renderMensagem(Mensagem.ANALISE_GEO_INICIADA_SUCESSO);

    }

    public static void iniciarAnaliseGerente(AnaliseGeo analise) {

        AnaliseGeo analiseAlterar = AnaliseGeo.findById(analise.id);

        UsuarioAnalise usuarioExecutor = getUsuarioSessao();

        analiseAlterar.iniciarAnaliseGerente(usuarioExecutor);

        renderMensagem(Mensagem.GERENTE_INICIOU_ANALISE_SUCESSO);

    }

    public static void findByNumeroProcesso() {

        verificarPermissao(Acao.INICIAR_PARECER_GEO);

        String numeroProcesso = getParamAsString("numeroProcesso");

        AnaliseGeo analise = AnaliseGeo.findByNumeroProcesso(numeroProcesso);

        if(analise == null) {

            renderMensagem(Mensagem.PARECER_NAO_ENCONTRADO);

        } else if(!analise.inconsistencias.isEmpty()) {

            renderMensagem(Mensagem.CLONAR_PARECER_COM_INCONSISTENCIA);

        } else {

            renderJSON(analise, AnaliseGeoSerializer.parecer);
        }

    }

    public static void findAnalisesGeoByNumeroProcesso(String numero) {

        String numeroDecodificado = new String(Base64.decodeBase64(numero.getBytes()));

        renderJSON(AnaliseGeo.findAnalisesByNumeroProcesso(numeroDecodificado), AnaliseGeoSerializer.findInfo);

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

        AnaliseGeo analiseGeoSalva = AnaliseGeo.findById(analiseGeo.id);
        ParecerAnalistaGeo ultimoParecer = analiseGeoSalva.pareceresAnalistaGeo.stream().max(Comparator.comparing(ParecerAnalistaGeo::getDataParecer)).orElseThrow(ValidationException::new);
        Documento pdfParecer = analiseGeoSalva.gerarPDFParecer(ultimoParecer);
        //ultimoParecer.documentoParecer = analiseGeoSalva.gerarPDFParecer(ultimoParecer);

        String nome = pdfParecer.tipo.nome +  "_" + analiseGeoSalva.id + ".pdf";
        //String nome = ultimoParecer.documentoParecer.tipo.nome +  "_" + analiseGeoSalva.id + ".pdf";
        nome = nome.replace(' ', '_');
        response.setHeader("Content-Disposition", "inline; filename=" + nome);
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Type", "application/pdf");

        renderBinary(pdfParecer.arquivo, nome);

//        ultimoParecer._save();
//
//        renderBinary(ultimoParecer.documentoParecer.arquivo, nome);

    }

    public static void downloadPDFCartaImagem(AnaliseGeo analiseGeo) {

        AnaliseGeo analiseGeoSalva = AnaliseGeo.findById(analiseGeo.id);

        Documento pdfParecer = analiseGeoSalva.gerarPDFCartaImagem();

        String nome = pdfParecer.tipo.nome +  "_" + analiseGeoSalva.id + ".pdf";
        nome = nome.replace(' ', '_');
        response.setHeader("Content-Disposition", "inline; filename=" + nome);
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Type", "application/pdf");

        renderBinary(pdfParecer.arquivo, nome);

    }

   public static void downloadPDFNotificacao(AnaliseGeo analiseGeo) throws Exception {

       verificarPermissao(Acao.INICIAR_PARECER_GEO);

       analiseGeo.analise = Analise.findById(analiseGeo.analise.id);

       List<Notificacao> notificacoes = Notificacao.gerarNotificacoesTemporarias(analiseGeo);

       Documento pdfNotificacao = Notificacao.gerarPDF(notificacoes, analiseGeo);

       String nome = pdfNotificacao.tipo.nome +  "_" + analiseGeo.id + ".pdf";
       nome = nome.replace(' ', '_');
       response.setHeader("Content-Disposition", "inline; filename=" + nome);
       response.setHeader("Content-Transfer-Encoding", "binary");
       response.setHeader("Content-Type", "application/pdf");

       renderBinary(pdfNotificacao.arquivo, nome);

   }

    public static void downloadPDFOficioOrgao(Long id) {

//        verificarPermissao(Acao.INICIAR_PARECER_GEO);
        Comunicado comunicado = Comunicado.findById(id);

        AnaliseGeo analiseGeoSalva = AnaliseGeo.findById(comunicado.analiseGeo.id);

        Documento pdfParecer = analiseGeoSalva.gerarPDFOficioOrgao(comunicado);

        String nome = pdfParecer.tipo.nome + "_" + analiseGeoSalva.id + ".pdf";
        nome = nome.replace(' ', '_');
        response.setHeader("Content-Disposition", "inline; filename=" + nome);
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Type", "application/pdf");

        renderBinary(pdfParecer.arquivo, nome);
    }

    public static void buscaDadosProcesso(Long idProcesso) {

        returnIfNull(idProcesso, "Long");

        Processo processo = Processo.findById(idProcesso);

        renderJSON(processo.getDadosProcesso(), CamadaGeoAtividadeSerializer.getDadosProjeto);

    }

    public static void buscaAnaliseGeoByAnalise(Long idAnalise) {

        AnaliseGeo analiseGeo = AnaliseGeo.find("id_analise = :id_analise")
                .setParameter("id_analise", idAnalise).first();

        renderJSON(analiseGeo, AnaliseGeoSerializer.findInfo);

    }

    public static void findAllRestricoesById(Long idProcesso) {

        returnIfNull(idProcesso, "Long");

        Processo processo = Processo.findById(idProcesso);

        renderJSON(Processo.preencheListaRestricoes(processo.caracterizacao), CamadaGeoAtividadeSerializer.getDadosRestricoesProjeto);

    }

}
