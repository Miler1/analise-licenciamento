package models.manejoDigital.analise.analiseJuridica;

import models.manejoDigital.ProcessoManejo;
import play.data.validation.Required;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema = "analise", name = "analise_juridica_manejo")
public class AnaliseJuridicaManejo {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise.analise_juridica_manejo_id_seq")
    @SequenceGenerator(name="analise.analise_juridica_manejo_id_seq", sequenceName="analise.analise_juridica_manejo_id_seq", allocationSize=1)
    public Long id;

    @Required
    @Column(name="data")
    public Date dataAnalise;

    @Column(name = "resultado_analise")
    public boolean resultadoAnalise;

    @Required
    @ManyToOne
    @JoinColumn(name = "id_processo_manejo")
    public ProcessoManejo processoManejo;

    @OneToOne(mappedBy = "analiseJuridicaManejo", cascade = CascadeType.ALL, orphanRemoval = true)
    public ConsultorJuridicoManejo consultorJuridico;

    @OneToMany(mappedBy = "analiseJuridicaManejo", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Consideracao> consideracoes;
}
