package models.manejoDigital;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;

@Entity
@Table(schema = "analise", name = "processo_manejo")
public class ProcessoManejo extends GenericModel {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="processo_manejo_id_seq")
    @SequenceGenerator(name="processo_manejo_id_seq", sequenceName="processo_manejo_id_seq", allocationSize=1)
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
    @ManyToOne
    @JoinColumn(name="id_analise_manejo")
    public Integer idAnaliseManejo;

}
