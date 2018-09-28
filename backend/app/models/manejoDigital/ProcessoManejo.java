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
    @Column(name="numero_processo")
    public String numeroProcesso;

    @Required
    @Column(name="cpf_cnpj")
    public String cpfCnpj;

    @Required
    @Column(name="id_empreendimento")
    public Long idEmpreendimento;

    @Required
    @Column(name="id_municipio")
    public Long idMunicipio;

    @Required
    @JoinColumn(name="id_tipo_licensa_manejo")
    public TipoLicencaManejo tipoLicensaManejo;

    @Required
    @JoinColumn(name="id_analise_manejo")
    public AnaliseManejo analiseManejo;



}
