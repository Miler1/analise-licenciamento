package models.manejoDigital;

import models.tramitacao.ObjetoTramitavel;
import models.tramitacao.AcaoDisponivelObjetoTramitavel;
import models.tramitacao.Tramitacao;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import security.InterfaceTramitavel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "analise", name = "processo_manejo")
public class ProcessoManejo extends GenericModel implements InterfaceTramitavel {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise.processo_manejo_id_seq")
    @SequenceGenerator(name="analise.processo_manejo_id_seq", sequenceName="analise.processo_manejo_id_seq", allocationSize=1)
    public Long id;

    @Required
    @Column(name="num_processo")
    public String numeroProcesso;

    @Required
    @Column(name="cpf_cnpj_empreendimento")
    public String cpfCnpj;

    @Required
    @Column(name="denominacao_empreendimento_simlam")
    public String denominacaoEmpreendimentoSimlam;

    @Required
    @Column(name="id_empreendimento_simlam")
    public Integer idEmpreendimento;

    @Required
    @Column(name="id_municipio_simlam")
    public Integer idMunicipio;

    @Required
    @Column(name="nome_municipio_simlam")
    public String nomeMunicipioSimlam;

    @Required
    @Column(name="id_tipo_licenca")
    public Integer idTipoLicenca;

    @Required
    @Column(name="nome_tipo_licenca")
    public String nomeTipoLicenca;

    @Required
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="id_imovel_manejo")
    public ImovelManejo imovelManejo;

    @OneToOne
    @JoinColumn(name = "id_analise_manejo")
    public AnaliseManejo analiseManejo;

    @Column(name = "id_objeto_tramitavel")
    public Long idObjetoTramitavel;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_objeto_tramitavel", referencedColumnName = "id_objeto_tramitavel", insertable=false, updatable=false)
    public ObjetoTramitavel objetoTramitavel;
    @Transient
    public transient Tramitacao tramitacao = new Tramitacao();


    @Override
    public ProcessoManejo save() {

        tramitacao.iniciar(this, null, Tramitacao.MANEJO_DIGITAL);

        return this;
    }

    @Override
    public Long getIdObjetoTramitavel() {

        return this.idObjetoTramitavel;
    }

    @Override
    public void setIdObjetoTramitavel(Long idObjetoTramitavel) {

        this.idObjetoTramitavel = idObjetoTramitavel;
    }

    @Override
    public List<AcaoDisponivelObjetoTramitavel> getAcoesDisponiveisTramitacao() {

        if (this.idObjetoTramitavel == null)
            return null;

        ObjetoTramitavel objetoTramitavel = ObjetoTramitavel.findById(this.idObjetoTramitavel);
        return objetoTramitavel.acoesDisponiveis;
    }

    @Override
    public void salvaObjetoTramitavel() {

        super.save();
    }
}
