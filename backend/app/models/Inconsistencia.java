package models;

import exceptions.ValidacaoException;
import models.licenciamento.*;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import serializers.InconsistenciaSerializer;
import utils.*;

import javax.persistence.*;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import models.licenciamento.Caracterizacao.OrigemSobreposicao;

@Entity
@Table(schema="analise", name="inconsistencia")
public class Inconsistencia extends GenericModel{

    public static final String SEQ = "analise.inconsistencia_id_seq";

    public enum Categoria {

        PROPRIEDADE,
        COMPLEXO,
        ATIVIDADE,
        RESTRICAO;

        public static Categoria preencheCategoria(Caracterizacao caracterizacao) {

            if(caracterizacao.origemSobreposicao.equals(OrigemSobreposicao.EMPREENDIMENTO)) {

                return Categoria.PROPRIEDADE;

            } else if(caracterizacao.origemSobreposicao.equals(OrigemSobreposicao.COMPLEXO)) {

                return Categoria.COMPLEXO;

            } else if(caracterizacao.origemSobreposicao.equals(OrigemSobreposicao.ATIVIDADE)) {

                return Categoria.ATIVIDADE;

            } else {

                boolean isAtividadeDentroEmpreendimento = caracterizacao.atividadesCaracterizacao.stream().allMatch(AtividadeCaracterizacao::isAtividadeDentroEmpreendimento);

                if(isAtividadeDentroEmpreendimento) {

                    return Categoria.PROPRIEDADE;

                } else if(caracterizacao.isComplexo()){

                    return Categoria.COMPLEXO;

                } else {

                    return Categoria.ATIVIDADE;

                }

            }

        }

    }

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator=SEQ)
    @SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
    public Long id;

    @Required
    @Column(name="descricao_inconsistencia")
    public String descricaoInconsistencia;

    @Required
    @Column(name="tipo_inconsistencia")
    public String tipoInconsistencia;

    @ManyToOne
    @JoinColumn(name="id_analise_geo")
    public AnaliseGeo analiseGeo;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(schema="analise", name="rel_documento_inconsistencia",
            joinColumns=@JoinColumn(name="id_inconsistencia"),
            inverseJoinColumns=@JoinColumn(name="id_documento"))
    public List<Documento> anexos;

    @Required
    @Enumerated(EnumType.STRING)
    @Column(name="categoria")
    public Categoria categoria;

    @OneToOne
    @JoinColumn(name="id_caracterizacao")
    public Caracterizacao caracterizacao;

    @OneToOne
    @JoinColumn(name = "id_atividade_caracterizacao")
    public AtividadeCaracterizacao atividadeCaracterizacao;

    @OneToOne
    @JoinColumn(name="id_sobreposicao")
    public SobreposicaoCaracterizacaoAtividade sobreposicaoCaracterizacaoAtividade;

    @OneToOne
    @JoinColumn(name="id_sobreposicao_empreendimento")
    public SobreposicaoCaracterizacaoEmpreendimento sobreposicaoCaracterizacaoEmpreendimento;

    @OneToOne
    @JoinColumn(name="id_sobreposicao_complexo")
    public SobreposicaoCaracterizacaoComplexo sobreposicaoCaracterizacaoComplexo;

    public Inconsistencia(AnaliseGeo analiseGeo) {

        this.analiseGeo = analiseGeo;
        this.descricaoInconsistencia = "";
        this.tipoInconsistencia = "";
        this.anexos = null;

    }

    public Inconsistencia() {

    }

    public Inconsistencia(String descricaoInconsistencia, String tipoInconsistencia, Categoria categoria, AnaliseGeo analiseGeo) {

        this.analiseGeo = analiseGeo;
        this.descricaoInconsistencia = descricaoInconsistencia;
        this.tipoInconsistencia = tipoInconsistencia;
        this.categoria = categoria;

    }

    public Inconsistencia(String descricaoInconsistencia, String tipoInconsistencia, Categoria categoria, AnaliseGeo analiseGeo, Caracterizacao caracterizacao) {

        this.analiseGeo = analiseGeo;
        this.descricaoInconsistencia = descricaoInconsistencia;
        this.tipoInconsistencia = tipoInconsistencia;
        this.categoria = categoria;
        this.caracterizacao = caracterizacao;

    }

    public Inconsistencia(String descricaoInconsistencia, String tipoInconsistencia, Categoria categoria, AnaliseGeo analiseGeo, AtividadeCaracterizacao atividadeCaracterizacao) {

        this.analiseGeo = analiseGeo;
        this.descricaoInconsistencia = descricaoInconsistencia;
        this.tipoInconsistencia = tipoInconsistencia;
        this.categoria = categoria;
        this.atividadeCaracterizacao = atividadeCaracterizacao;

    }

    public Inconsistencia(String descricaoInconsistencia, String tipoInconsistencia, Categoria categoria, AnaliseGeo analiseGeo, Caracterizacao caracterizacao, SobreposicaoCaracterizacaoAtividade sobreposicaoCaracterizacaoAtividade) {

        this.analiseGeo = analiseGeo;
        this.descricaoInconsistencia = descricaoInconsistencia;
        this.tipoInconsistencia = tipoInconsistencia;
        this.categoria = categoria;
        this.caracterizacao = caracterizacao;
        this.sobreposicaoCaracterizacaoAtividade = sobreposicaoCaracterizacaoAtividade;

    }

    public Inconsistencia(String descricaoInconsistencia, String tipoInconsistencia, Categoria categoria, AnaliseGeo analiseGeo, Caracterizacao caracterizacao, SobreposicaoCaracterizacaoEmpreendimento sobreposicaoCaracterizacaoEmpreendimento) {

        this.analiseGeo = analiseGeo;
        this.descricaoInconsistencia = descricaoInconsistencia;
        this.tipoInconsistencia = tipoInconsistencia;
        this.categoria = categoria;
        this.caracterizacao = caracterizacao;
        this.sobreposicaoCaracterizacaoEmpreendimento = sobreposicaoCaracterizacaoEmpreendimento;

    }

    public Inconsistencia(String descricaoInconsistencia, String tipoInconsistencia, Categoria categoria, AnaliseGeo analiseGeo, Caracterizacao caracterizacao, SobreposicaoCaracterizacaoComplexo sobreposicaoCaracterizacaoComplexo) {

        this.analiseGeo = analiseGeo;
        this.descricaoInconsistencia = descricaoInconsistencia;
        this.tipoInconsistencia = tipoInconsistencia;
        this.categoria = categoria;
        this.caracterizacao = caracterizacao;
        this.sobreposicaoCaracterizacaoComplexo = sobreposicaoCaracterizacaoComplexo;

    }

    public void saveAnexos(List<Documento> novosAnexos) {

        TipoDocumento tipo = TipoDocumento.findById(TipoDocumento.DOCUMENTO_INCONSISTENCIA);

        if (this.anexos == null)
            this.anexos = new ArrayList<>();

        Iterator<Documento> docsCadastrados = anexos.iterator();
        List<Documento> documentosDeletar = new ArrayList<>();

        while (docsCadastrados.hasNext()) {

            Documento docCadastrado = docsCadastrados.next();

            if (ListUtil.getById(docCadastrado.id, novosAnexos) == null) {

                docsCadastrados.remove();

                // remove o documeto do banco apenas se ele n??o estiver relacionado
                // com outra an??lises
                List<AnaliseGeo> analiseGeoRelacionadas = docCadastrado.getAnaliseGeoRelacionadas();
                if(analiseGeoRelacionadas.size() == 0) {

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

    public void deleteAnexos() {

        for (Documento anexo : this.anexos) {

            File documento = anexo.getFile();
            documento.delete();
        }

    }

    public Inconsistencia buscarInconsistenciaGeo(){

        Inconsistencia inconsistencia = null;

        if(this.categoria == null || (this.categoria.equals(Inconsistencia.Categoria.RESTRICAO) && (this.sobreposicaoCaracterizacaoEmpreendimento != null && this.sobreposicaoCaracterizacaoEmpreendimento.id == null))) {
            throw new ValidacaoException(Mensagem.CAMPOS_OBRIGATORIOS);
        }

        if(this.categoria.equals(Inconsistencia.Categoria.PROPRIEDADE)){
            inconsistencia = Inconsistencia.find("analiseGeo.id = :idAnaliseGeo and categoria = :categoria")
                    .setParameter("idAnaliseGeo",this.analiseGeo.id)
                    .setParameter("categoria",this.categoria).first();
        }

        if(this.categoria.equals(Inconsistencia.Categoria.ATIVIDADE)) {
            inconsistencia = Inconsistencia.find("analiseGeo.id = :idAnaliseGeo and categoria = :categoria and caracterizacao.id = :caracterizacao")
                    .setParameter("idAnaliseGeo",this.analiseGeo.id)
                    .setParameter("caracterizacao",this.caracterizacao.id)
                    .setParameter("categoria",this.categoria).first();
        }

        if(this.categoria.equals(Inconsistencia.Categoria.RESTRICAO)) {

            if(this.sobreposicaoCaracterizacaoAtividade != null) {

                inconsistencia = Inconsistencia.find("analiseGeo.id = :idAnaliseGeo and categoria = :categoria and caracterizacao.id = :caracterizacao and sobreposicaoCaracterizacaoAtividade.id = :sobreposicaoCaracterizacaoAtividade")
                        .setParameter("idAnaliseGeo",this.analiseGeo.id)
                        .setParameter("caracterizacao",this.caracterizacao.id)
                        .setParameter("categoria",this.categoria)
                        .setParameter("sobreposicaoCaracterizacaoAtividade", this.sobreposicaoCaracterizacaoAtividade.id).first();

            } else if(this.sobreposicaoCaracterizacaoEmpreendimento != null) {

                inconsistencia = Inconsistencia.find("analiseGeo.id = :idAnaliseGeo and categoria = :categoria and caracterizacao.id = :caracterizacao and sobreposicaoCaracterizacaoEmpreendimento.id = :sobreposicaoCaracterizacaoEmpreendimento")
                        .setParameter("idAnaliseGeo",this.analiseGeo.id)
                        .setParameter("caracterizacao",this.caracterizacao.id)
                        .setParameter("categoria",this.categoria)
                        .setParameter("sobreposicaoCaracterizacaoEmpreendimento", this.sobreposicaoCaracterizacaoEmpreendimento.id).first();

            } else if(this.sobreposicaoCaracterizacaoComplexo != null) {

                inconsistencia = Inconsistencia.find("analiseGeo.id = :idAnaliseGeo and categoria = :categoria and caracterizacao.id = :caracterizacao and sobreposicaoCaracterizacaoComplexo.id = :sobreposicaoCaracterizacaoComplexo")
                        .setParameter("idAnaliseGeo",this.analiseGeo.id)
                        .setParameter("caracterizacao",this.caracterizacao.id)
                        .setParameter("categoria",this.categoria)
                        .setParameter("sobreposicaoCaracterizacaoComplexo", this.sobreposicaoCaracterizacaoComplexo.id).first();

            }

        }

        return inconsistencia;

    }

    public Inconsistencia salvaInconsistenciaGeo (){

        if (this.descricaoInconsistencia == null || this.descricaoInconsistencia.equals("")) {

            throw new ValidacaoException(Mensagem.CAMPOS_OBRIGATORIOS);
        }

        if(this.tipoInconsistencia == null || this.tipoInconsistencia.equals("")){

            throw new ValidacaoException(Mensagem.CAMPOS_OBRIGATORIOS);
        }

        if(this.categoria.equals(Inconsistencia.Categoria.RESTRICAO) && this.sobreposicaoCaracterizacaoAtividade == null && this.sobreposicaoCaracterizacaoEmpreendimento == null && this.sobreposicaoCaracterizacaoComplexo == null) {

            throw new ValidacaoException(Mensagem.CAMPOS_OBRIGATORIOS);

        }

        if (this.id != null) {

            Inconsistencia inconsistencia = Inconsistencia.findById(this.id);
            inconsistencia.descricaoInconsistencia = this.descricaoInconsistencia;
            inconsistencia.tipoInconsistencia = this.tipoInconsistencia;
            inconsistencia.categoria = this.categoria;
            inconsistencia.analiseGeo = this.analiseGeo;
            inconsistencia.id = this.id;
            inconsistencia.caracterizacao = Objects.nonNull(this.caracterizacao) && Objects.nonNull(this.caracterizacao.id) ? this.caracterizacao : null;
            inconsistencia.atividadeCaracterizacao = Objects.nonNull(this.atividadeCaracterizacao) && Objects.nonNull(this.atividadeCaracterizacao.id) ? AtividadeCaracterizacao.findById(this.atividadeCaracterizacao.id) : null;
            inconsistencia.saveAnexos(this.anexos);

            return inconsistencia.save();

        } else {

            Inconsistencia novaInconsistencia;

            if(this.categoria.equals(Inconsistencia.Categoria.PROPRIEDADE) || this.categoria.equals(Inconsistencia.Categoria.COMPLEXO)){

                novaInconsistencia = new Inconsistencia(this.descricaoInconsistencia, this.tipoInconsistencia, this.categoria, this.analiseGeo);
                novaInconsistencia.saveAnexos(this.anexos);

                return novaInconsistencia.save();

            } else if(this.categoria.equals(Inconsistencia.Categoria.ATIVIDADE)) {

                this.atividadeCaracterizacao = AtividadeCaracterizacao.findById(this.atividadeCaracterizacao.id);
                novaInconsistencia = new Inconsistencia(this.descricaoInconsistencia, this.tipoInconsistencia, this.categoria, this.analiseGeo, this.atividadeCaracterizacao);
                novaInconsistencia.saveAnexos(this.anexos);

                return novaInconsistencia.save();

            } else {

                if(this.sobreposicaoCaracterizacaoAtividade != null) {

                    novaInconsistencia = new Inconsistencia(this.descricaoInconsistencia, this.tipoInconsistencia, this.categoria, this.analiseGeo, this.caracterizacao, this.sobreposicaoCaracterizacaoAtividade);

                } else if(this.sobreposicaoCaracterizacaoEmpreendimento != null) {

                    novaInconsistencia = new Inconsistencia(this.descricaoInconsistencia, this.tipoInconsistencia, this.categoria, this.analiseGeo, this.caracterizacao, this.sobreposicaoCaracterizacaoEmpreendimento);

                } else {

                    novaInconsistencia = new Inconsistencia(this.descricaoInconsistencia, this.tipoInconsistencia, this.categoria, this.analiseGeo, this.caracterizacao, this.sobreposicaoCaracterizacaoComplexo);

                }

                novaInconsistencia.saveAnexos(this.anexos);

                return novaInconsistencia.save();

            }

        }

    }

}
