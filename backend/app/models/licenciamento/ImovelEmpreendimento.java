package models.licenciamento;

import com.vividsolutions.jts.geom.Geometry;
import org.hibernate.annotations.Type;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "licenciamento", name = "imovel_empreendimento")
public class ImovelEmpreendimento extends GenericModel {
	
	private static final String SEQ = "licenciamento.imovel_empreendimento_id_seq";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;
	
	@Required
	@Column(name = "codigo_imovel")
	public String codigo;
	
	@Column(name = "id_imovel_car")
	public Long idCar;
	
	public String nome;
	
	@Column(name = "the_geom", columnDefinition = "Geometry")
	public Geometry limite;
	
	@ManyToOne
	@JoinColumn(name="id_municipio", referencedColumnName="id_municipio")
	public Municipio municipio;
	
	@OneToOne
	@JoinColumn(name = "id_empreendimento", referencedColumnName = "id", nullable = false)
	public Empreendimento empreendimento;
	
	@Column(name = "data_cadastro")
	public Date dataCadastro;
	
	@Column(name = "area_total")
	public Double areaTotal;
	
	@Column(name = "area_app")
	public Double areaAPP;
	
	@Column(name = "area_reserva_legal")
	public Double areaReservaLegal;
	
	@Column(name = "area_uso_alternativo_solo")
	public Double areaUsoAlternativoSolo;
	
	@Transient
	public String imagemMapa;
	
	public ImovelEmpreendimento() {
		
	}
}