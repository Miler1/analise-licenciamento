package models.licenciamento;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;


@Entity
@Table(schema = "licenciamento", name = "potencial_poluidor")
public class PotencialPoluidor extends Model implements Comparable<PotencialPoluidor>{

	public String nome;
	
	public String codigo;
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PotencialPoluidor other = (PotencialPoluidor) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public int compareTo(PotencialPoluidor potencialPoluidor) {
		
		if(this.equals(potencialPoluidor)) {
			return 0;
		}
		
		if(this.codigo.equals("I")) {
			
			return -1;	
			
		} else if(this.codigo.equals("II")) {
			
			if(potencialPoluidor.codigo.equals("I")) {
				
				return 1;
				
			} else if(potencialPoluidor.codigo.equals("III")) {
				
				return -1;
				
			}
			
		} else if(this.codigo.equals("III")) {
			
			return 1;
			
		}
		
		return 0;
	}
}
