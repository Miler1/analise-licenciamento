package models.validacaoParecer;

import models.portalSeguranca.Usuario;

public abstract class TipoResultadoAnaliseChain<T extends Analisavel> {
	
	protected TipoResultadoAnaliseChain next;
	protected Long idResultadoAnalise;
	protected T analise;
	
	public TipoResultadoAnaliseChain(Long idResultadoAnalise){
		
		this.next = null;
		this.idResultadoAnalise = idResultadoAnalise;
	}
	
	public void setNext(TipoResultadoAnaliseChain tipoResultadoAnalise) {
		
        if (next == null) {
            next = tipoResultadoAnalise;
        } else {
            next.setNext(tipoResultadoAnalise);
        }
    }
	
	public void validarParecer(T analise, T novaAnalise, Usuario usuarioExecultor) {
		
		if (novaAnalise.getTipoResultadoValidacao().id.equals(idResultadoAnalise)) {
							
			validaParecer(analise, novaAnalise, usuarioExecultor);
			
		} else if (next != null) {
			
			next.validarParecer(analise, novaAnalise, usuarioExecultor);
		}
	}
	
	protected abstract void validaParecer(T analise, T novaAnalise, Usuario usuarioExecultor);
}
