package jobs;

import play.db.jpa.JPA;
import play.jobs.Job;
import utils.Configuracoes;

public abstract class GenericJob extends Job {

	@Override
	public void doJob() throws Exception {
		
		if (!Configuracoes.JOBS_ENABLED)
			return;
		
		executar();
	}
	
	public abstract void executar() throws Exception;
	
	protected void commitTransaction() {
		
		if (JPA.isInsideTransaction()) {
			
			JPA.em().getTransaction().commit();
			JPA.em().getTransaction().begin();
		}
	}
	
	protected void rollbackTransaction() {
		
		if (JPA.isInsideTransaction()) {
			
			JPA.em().getTransaction().rollback();
			JPA.em().getTransaction().begin();
		}
	}
}
