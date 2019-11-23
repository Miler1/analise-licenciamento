package models;

import models.tramitacao.ViewHistoricoTramitacao;
import play.db.jpa.GenericModel;

import javax.persistence.*;

@Entity
@Table(schema = "analise", name = "historico_tramitacao_setor")
public class RelHistoricoTramitacaoSetor extends GenericModel {

    @Id
    @OneToOne
    @JoinColumn(name="id_historico_tramitacao")
    public ViewHistoricoTramitacao viewHistoricoTramitacao;

    @Column(name="sigla_setor")
    public String siglaSetor;

}
