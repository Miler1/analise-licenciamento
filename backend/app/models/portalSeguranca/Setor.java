package models.portalSeguranca;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import models.tramitacao.HistoricoTramitacao;
import play.db.jpa.GenericModel;
import utils.Identificavel;

@Entity
@Table(schema = "portal_seguranca", name = "setor")
public class Setor extends GenericModel {
	
	@Id
	@Column(name="id")
	public Integer id;
	
	public String nome;
	
	public String sigla;
	
	@ManyToOne
	@JoinColumn(name="id_setor_pai")
	public Setor setorPai;
	
	@ManyToMany
	@JoinTable(schema = "portal_seguranca", name = "perfil_setor",
			joinColumns = @JoinColumn(name = "id_setor"),
			inverseJoinColumns = @JoinColumn(name = "id_perfil"))
	public List<Perfil> perfis;
	
	@Column(name="tipo_setor")
	@Enumerated(EnumType.ORDINAL)
	public TipoSetor tipoSetor;

	@OneToMany
	@JoinTable(schema = "portal_seguranca", name = "historico_tramitacao_setor",
			joinColumns = @JoinColumn(name = "id_setor"),
			inverseJoinColumns = @JoinColumn(name = "id_historico_tramitacao"))
	public List<HistoricoTramitacao> historicosTramitacao;
	
	/**
	 * Método para buscar os setores por nível. Por exemplo:
	 * nível 1 irá buscar os filhos do setor pesquisado;
	 * nível 2 irá buscar os netos do setor pesquisado;
	 * e assim por diante.
	 * Obs.: O nível mais baixo é o 1
	 * @param nivel
	 * @return
	 */
	public List<Setor> getSetoresByNivel(Integer nivel) {
		
		if (nivel <= 0) {
			
			return new ArrayList<>();
		}
		
		int i = 0;
		
		String sql = String.format("SELECT s%1$d FROM Setor s%1$d ", i);
		
		for (i = 1; i < nivel; i++) {
			
			sql+= String.format("JOIN s%d.setorPai s%d ", i-1, i);
		}
		
		sql+= String.format("WHERE s%d.setorPai = ?", --i);
		
		List<Setor> setoresByNivel = Setor.find(sql, this).fetch();
		
		return setoresByNivel;
	}
	
	
	public List<Integer> getIdsSetoresByNivel(Integer nivel) {
		
		List<Setor> setoresByNivel = getSetoresByNivel(nivel);
		
		ArrayList<Integer> idsSetoresByNivel = new ArrayList<>();
		
		for (Setor setor : setoresByNivel) {
			
			idsSetoresByNivel.add(setor.id);
		}
		
		return idsSetoresByNivel;
	}

	public static void setHistoricoTramitacao(HistoricoTramitacao historicoTramitacao, Usuario usuarioExecutor) {

		if (usuarioExecutor.setorSelecionado != null) {

			Setor setor = Setor.findById(usuarioExecutor.setorSelecionado.id);

			if (setor.historicosTramitacao == null) {

				setor.historicosTramitacao = new ArrayList<>();
			}

			if (!containsHistoricoSetor(setor.historicosTramitacao, historicoTramitacao)) {

				setor.historicosTramitacao.add(historicoTramitacao);
				setor._save();
			}
		}
	}

	private static boolean containsHistoricoSetor (List<HistoricoTramitacao> historicos, HistoricoTramitacao historicoAlvo) {

		for (HistoricoTramitacao historico : historicos) {

			if (historico.idHistorico.equals(historicoAlvo.idHistorico) && historico.setor.id.equals(historicoAlvo.setor.id)) {

				return true;
			}
		}

		return false;
	}

}