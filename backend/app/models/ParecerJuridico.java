package models;

import java.util.*;

import models.licenciamento.*;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import play.db.jpa.GenericModel;
import utils.*;

import javax.persistence.*;
import javax.validation.ValidationException;

@Entity
@Table(schema="analise", name="parecer_juridico")
public class ParecerJuridico extends GenericModel {

    private final static String SEQ = "analise.parecer_juridico_id_seq";

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator=SEQ)
    @SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
    public Long id;

    @ManyToOne
    @JoinColumn(name="id_analise_geo", nullable=true)
    public AnaliseGeo analiseGeo;

    @ManyToOne
    @JoinColumn(name="id_analise_tecnica", nullable=true)
    public AnaliseTecnica analiseTecnica;

    @OneToOne
    @JoinColumn(name = "id_parecer_analista_geo", referencedColumnName = "id")
    public ParecerAnalistaGeo parecerAnalistaGeo;

    @OneToOne
    @JoinColumn(name = "id_documento_fundiario", referencedColumnName = "id")
    public DocumentoLicenciamento documentoFundiario;

    @ManyToOne
    @JoinColumn(name = "id_tipo_resultado_validacao_juridica")
    public TipoResultadoAnalise tipoResultadoAnalise;

    @Column(name="data_cadastro")
    @Temporal(TemporalType.TIMESTAMP)
    public Date dataCadastro;

    @Column(name="data_resposta")
    @Temporal(TemporalType.TIMESTAMP)
    public Date dataResposta;

    @Column(name="parecer")
    public String parecer;

    @Column(name = "id_historico_tramitacao")
    public Long idHistoricoTramitacao;

    @Column(name="resolvido")
    public Boolean resolvido;

    @Column(name="ativo")
    public Boolean ativo;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(schema="analise", name="rel_documento_juridico",
            joinColumns=@JoinColumn(name="id_parecer_juridico"),
            inverseJoinColumns=@JoinColumn(name="id_documento"))
    public List<Documento> anexos;

    @Transient
    public String linkParecerJuridico;

    public ParecerJuridico(AnaliseGeo analiseGeo, ParecerAnalistaGeo parecerAnalistaGeo, AnaliseTecnica analiseTecnica, DocumentoLicenciamento documentoFundiario){

        this.dataCadastro = new Date();
        this.analiseGeo = analiseGeo;
        this.ativo = true;
        this.resolvido = false;
        this.parecerAnalistaGeo = parecerAnalistaGeo;
        this.analiseTecnica = analiseTecnica;
        this.documentoFundiario = documentoFundiario;

    }

    public static List<Comunicado> findByAnaliseGeo(Long idAnaliseGeo) {
        List<Comunicado> listaComunicados = null;
        return listaComunicados = Comunicado.find("id_analise_geo = :analiseGeo")
                .setParameter("analiseGeo", idAnaliseGeo).fetch();
    }


    public void saveAnexos(List<Documento> novosAnexos) {

        TipoDocumento tipo = TipoDocumento.findById(TipoDocumento.DOCUMENTO_JURIDICO);

        if (this.anexos == null)
            this.anexos = new ArrayList<>();

        Iterator<Documento> docsCadastrados = anexos.iterator();
        List<Documento> documentosDeletar = new ArrayList<>();

        while (docsCadastrados.hasNext()) {

            Documento docCadastrado = docsCadastrados.next();

            if (ListUtil.getById(docCadastrado.id, novosAnexos) == null) {

                docsCadastrados.remove();

                // remove o documeto do banco apenas se ele não estiver relacionado
                // com outra análises
                List<ParecerJuridico> pareceresJuridicosRelacionados = docCadastrado.getPareceresJuridicosRelacionados();
                if (pareceresJuridicosRelacionados.isEmpty()) {

                    documentosDeletar.add(docCadastrado);
                }

            }
        }
        for (Documento novoAnexo : novosAnexos) {

            if (novoAnexo.id == null) {

                novoAnexo.tipo = tipo;

                novoAnexo.save();
                this.anexos.add(novoAnexo);
            }
        }

        ModelUtil.deleteAll(documentosDeletar);
    }

    public static void finalizar(ParecerJuridico parecerJuridico) throws Exception {

        if(parecerJuridico.id != null){

            ParecerJuridico parecerJuridicoBanco = ParecerJuridico.findById(parecerJuridico.id);
            parecerJuridicoBanco.tipoResultadoAnalise = parecerJuridico.tipoResultadoAnalise;
            parecerJuridicoBanco.parecer = parecerJuridico.parecer;

            parecerJuridicoBanco.resolvido =true;
            parecerJuridicoBanco.ativo = false;

            parecerJuridicoBanco.saveAnexos(parecerJuridico.anexos);
            parecerJuridicoBanco.dataResposta = new Date();

            ParecerCoordenadorAnaliseGeo parecerCoordenadorAnaliseGeo = parecerJuridicoBanco.analiseGeo.pareceresCoordenadorAnaliseGeo.stream().max(Comparator.comparing(ParecerCoordenadorAnaliseGeo::getDataParecer)).orElseThrow(ValidationException::new);

            UsuarioAnalise coordenador = UsuarioAnalise.findById(parecerCoordenadorAnaliseGeo.usuario.id);

            parecerJuridicoBanco.analiseGeo.analise.processo.tramitacao.tramitar(parecerJuridicoBanco.analiseGeo.analise.processo, AcaoTramitacao.RESOLVER_ANALISE_JURIDICA, coordenador);
            HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(parecerJuridicoBanco.analiseGeo.analise.processo.objetoTramitavel.id), parecerJuridicoBanco.analiseGeo);

            HistoricoTramitacao historicoTramitacao = HistoricoTramitacao.getUltimaTramitacao(parecerJuridicoBanco.analiseGeo.analise.processo.objetoTramitavel.id);
            parecerJuridicoBanco.idHistoricoTramitacao = historicoTramitacao.idHistorico;

            parecerJuridicoBanco.save();

        }
    }

    public static ParecerJuridico getParecerJuridicoByAnaliseTecnica(Long idAnaliseTecnica) {

        List<ParecerJuridico> pareceresJuridicos = ParecerJuridico.find("id_analise_tecnica = :analiseTecnica ")
                .setParameter("analiseTecnica", idAnaliseTecnica).fetch();

        return pareceresJuridicos.stream().max( Comparator.comparing(parecer1 -> parecer1.id )).get();

    }
}

