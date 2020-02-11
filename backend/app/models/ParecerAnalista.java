package models;

import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@MappedSuperclass
public abstract class ParecerAnalista extends GenericModel {

    @ManyToOne
    @JoinColumn(name = "id_tipo_resultado_analise")
    public TipoResultadoAnalise tipoResultadoAnalise;

    @Column(name = "data_parecer")
    @Temporal(TemporalType.TIMESTAMP)
    public Date dataParecer;

    @Column(name = "parecer")
    public String parecer;

    @Column(name = "id_historico_tramitacao")
    public Long idHistoricoTramitacao;

    public Date getDataParecer() {
        return this.dataParecer;
    }

    public abstract List<Documento> getDocumentos();

    public abstract List<Documento> getDocumentosParecer();
}
