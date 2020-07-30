package models.licenciamento;

import com.vividsolutions.jts.geom.Geometry;
import enums.CamadaGeoEnum;
import models.*;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.ParamDef;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.db.jpa.GenericModel;
import services.IntegracaoEntradaUnicaService;
import utils.GeoCalc;
import utils.GeoJsonUtils;
import utils.Helper;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema = "licenciamento", name = "empreendimento")
@FilterDefs(value = {
		@FilterDef( name = "empreendimentoAtivo", parameters = @ParamDef(name = "ativo", type = "boolean"), defaultCondition = "ativo = FALSE" )
})
public class Empreendimento extends GenericModel {

	private static final String SEQ = "licenciamento.empreendimento_id_seq";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;
//
//	@Required
//	public String denominacao;
	
//	@Column(name = "id_redesim")
//	public Long idRedeSim;

	@Column(name="tipo_localizacao")
	@Enumerated(EnumType.ORDINAL)
	public TipoLocalizacao localizacao;
	
//	@Column(name = "the_geom", columnDefinition = "Geometry")
//	public Geometry coordenadas;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "empreendimento", orphanRemoval = true)
	public ImovelEmpreendimento imovel;
	
	@Column(name = "data_cadastro")
	public Date dataCadastro;

	@OneToMany(mappedBy = "empreendimento", targetEntity = Caracterizacao.class, orphanRemoval = true)
	public List<Caracterizacao> caracterizacoes;
	
	public boolean ativo;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_municipio", referencedColumnName="id_municipio")
	public Municipio municipio;
	
	@Required
	@Column(name="tipo_esfera")
	@Enumerated(EnumType.ORDINAL)
	public Esfera jurisdicao;

//	@Column(name = "houve_alteracoes")
//	public boolean houveAlteracoes;
	
	@Transient
	public boolean possuiCaracterizacoes;

	@Column(name = "possui_shape")
	public Boolean possuiShape;

	@Transient
	public Double area;

	@Required
	@Column(name = "cpf_cnpj")
	public String cpfCnpj;

	@Required
	@Column(name = "cpf_cnpj_cadastrante")
	public String cpfCnpjCadastrante;

	@Transient
	public Pessoa pessoa;

	@Transient
	public Pessoa cadastrante;

	@Transient
	public br.ufla.lemaf.beans.Empreendimento empreendimentoEU;

	public static Empreendimento buscaEmpreendimentoByCpfCnpj(String cpfCnpj) {

		return Empreendimento.find("cpfCnpj", cpfCnpj).first();

	}

	public static  List<CamadaGeoAtividadeVO> buscaDadosGeoEmpreendimento(String cpfCnpj) {

		IntegracaoEntradaUnicaService integracaoEntradaUnica = new IntegracaoEntradaUnicaService();

		br.ufla.lemaf.beans.Empreendimento empreendimentoEU = integracaoEntradaUnica.findEmpreendimentosByCpfCnpj(cpfCnpj);

		Empreendimento empreendimentoLicenciamento = Empreendimento.buscaEmpreendimentoByCpfCnpj(cpfCnpj);

		List<CamadaGeoAtividadeVO> dadosGeoEmpreendimento = new ArrayList<>();

		Geometry geometriaEmpreendimento = GeoJsonUtils.toGeometry(empreendimentoEU.localizacao.geometria);
		Double areaEmpreendimento = GeoCalc.area(geometriaEmpreendimento) / 10000;

		GeometriaAtividadeVO geometriaAtividade = new GeometriaAtividadeVO(CamadaGeoEnum.PROPRIEDADE.nome, CamadaGeoEnum.PROPRIEDADE.tipo, Helper.formatBrDecimal(areaEmpreendimento, 2) + " ha", areaEmpreendimento, geometriaEmpreendimento);

		CamadaGeoAtividadeVO camadaGeoEmpreendimento = new CamadaGeoAtividadeVO(Arrays.asList(geometriaAtividade));

		dadosGeoEmpreendimento.add(camadaGeoEmpreendimento);

		List<EmpreendimentoCamandaGeo> listaAnexos = EmpreendimentoCamandaGeo.find("byEmpreendimento", empreendimentoLicenciamento).fetch();

		List<TipoAreaGeometria> tiposAreaGeometria = TipoAreaGeometria.findAll();

		for (TipoAreaGeometria tipoAreaGeometria : tiposAreaGeometria) {

			EmpreendimentoCamandaGeo empreendimentoCamandaGeo = listaAnexos.stream()
					.filter(g -> g.tipoAreaGeometria.codigo.equals(tipoAreaGeometria.codigo))
					.findAny()
					.orElse(null);

			if (empreendimentoCamandaGeo != null) {

				GeometriaAtividadeVO geometria = new GeometriaAtividadeVO(empreendimentoCamandaGeo.tipoAreaGeometria.nome, CamadaGeoEnum.tipoFromCodigo(empreendimentoCamandaGeo.tipoAreaGeometria.codigo), Helper.formatBrDecimal(empreendimentoCamandaGeo.areaGeometria, 2)+ " ha",empreendimentoCamandaGeo.areaGeometria, empreendimentoCamandaGeo.geometria);
				camadaGeoEmpreendimento = new CamadaGeoAtividadeVO(Arrays.asList(geometria));
				dadosGeoEmpreendimento.add(camadaGeoEmpreendimento);

			} else {

				GeometriaAtividadeVO geometria = new GeometriaAtividadeVO(tipoAreaGeometria.nome, CamadaGeoEnum.tipoFromCodigo(tipoAreaGeometria.codigo), "não possui", 0.00, null);
				camadaGeoEmpreendimento = new CamadaGeoAtividadeVO(Arrays.asList(geometria));
				dadosGeoEmpreendimento.add(camadaGeoEmpreendimento);
			}

		}

		return dadosGeoEmpreendimento;
	}

	public  String getCpfCnpj() {

		return this.cpfCnpj;
	}

}
