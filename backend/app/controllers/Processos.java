package controllers;

import java.util.List;

import javax.persistence.Query;

import builders.ProcessoBuilder;
import models.Processo;
import play.db.jpa.JPA;
import play.mvc.Controller;
import serializers.ProcessoSerializer;

public class Processos extends GenericController {

	public void list(){
		
		List list = new ProcessoBuilder()
			.comNumeroProcesso()
			.comCpfCnpjEmpreendimento()
			.comDenominacaoEmpreendimento()
			.comMunicipioEmpreendimento()
			.comDataVencimentoPrazoAnalise()
			.comDataVencimentoPrazoAnaliseJuridica()
			.list();
		
		renderJSON(list);
	}
}
