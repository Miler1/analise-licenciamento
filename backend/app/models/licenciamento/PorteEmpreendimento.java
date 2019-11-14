package models.licenciamento;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "licenciamento", name = "porte_empreendimento")
public class PorteEmpreendimento extends Model implements Comparable<PorteEmpreendimento>{

	public String nome;
	
	public String codigo;

	@Override
	public int compareTo(PorteEmpreendimento porteEmpreendimento) {

		if(this.equals(porteEmpreendimento)) {

			return 0;

		}

		if(this.codigo.equals("MICRO")) {

			return -1;

		} else if(this.codigo.equals("PEQUENO")) {

			if(porteEmpreendimento.codigo.equals("MICRO")) {

				return 1;

			} else {

				return -1;

			}

		} else if(this.codigo.equals("MEDIO")) {

			if(porteEmpreendimento.codigo.equals("MICRO")
					|| porteEmpreendimento.codigo.equals("PEQUENO")) {

				return 1;

			} else {

				return -1;

			}

		} else if(this.codigo.equals("GRANDE")) {

			if(porteEmpreendimento.codigo.equals("MICRO")
					|| porteEmpreendimento.codigo.equals("PEQUENO")
					|| porteEmpreendimento.codigo.equals("MEDIO")) {

				return 1;

			} else {

				return -1;

			}

		} else if(this.codigo.equals("EXCEPCIONAL")) {

			if(porteEmpreendimento.codigo.equals("MICRO")
					|| porteEmpreendimento.codigo.equals("PEQUENO")
					|| porteEmpreendimento.codigo.equals("MEDIO")
					|| porteEmpreendimento.codigo.equals("GRANDE")) {

				return 1;

			} else {

				return -1;

			}

		}

		return 0;

	}
	
}
