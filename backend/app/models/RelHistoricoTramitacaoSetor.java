package models;

import models.tramitacao.HistoricoTramitacao;
import play.db.jpa.GenericModel;

import javax.persistence.*;

@Entity
@Table(schema = "analise", name = "historico_tramitacao_setor")
public class RelHistoricoTramitacaoSetor extends GenericModel {

    @Id
    @JoinColumn(name = "id_historico_tramitacao")
    public HistoricoTramitacao historicoTramitacao;

    @Column(name="sigla_setor")
    public String siglaSetor;

}
