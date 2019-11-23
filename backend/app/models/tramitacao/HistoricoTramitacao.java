package models.tramitacao;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema="tramitacao", name = "historico_objeto_tramitavel")
public class HistoricoTramitacao {

	@Id
	@Column(name = "id_historico_objeto_tramitavel")
	public Long id;

	@Column(name = "id_condicao_inicial")
	public Long idCondicaoInicial;

	@Column(name = "id_condicao_final")
	public Long idCondicaoFinal;

	@Column(name = "id_usuario_destino")
	public Long idUsuarioDestino;

	@Column(name = "dt_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataCadastro;

	@ManyToOne
	@JoinColumn(name = "id_objeto_tramitavel", referencedColumnName = "id_objeto_tramitavel")
	public ObjetoTramitavel objetoTramitavel;

}
