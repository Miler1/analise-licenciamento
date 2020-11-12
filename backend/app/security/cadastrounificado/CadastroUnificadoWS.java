package security.cadastrounificado;

import br.ufla.lemaf.beans.pessoa.Setor;
import br.ufla.lemaf.beans.PessoaFiltroResult;
import br.ufla.lemaf.beans.pessoa.FiltroPessoa;
import br.ufla.lemaf.beans.pessoa.Pessoa;
import br.ufla.lemaf.beans.pessoa.Usuario;
import br.ufla.lemaf.services.CadastroUnificadoPessoaService;
import models.UsuarioAnalise;
import play.Logger;
import utils.Configuracoes;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CadastroUnificadoWS extends CadastroUnificadoPessoaService {

    private static final int TMR_INIT_CONNECTION = 5000; //5s
    private static final int TMR_PERIOD_CONNECTION = 30000; //30s

    private static final String LOG_PREFIX = "[CADASTRO-UNIFICADO-WS]";

    private static TimerTask taskTryConnection = new TimerTask() {
        @Override
        public void run() {
            try {

                tryConnection();
                cancel();
                Logger.info(LOG_PREFIX + " - conexão estabelecida com sucesso.");
            }
            catch (Exception e) {
                ws = null;
                Logger.error(LOG_PREFIX +" - erro ao tentar estabelecer a conexão.");
            }
        }
    };

    // Integraçao com o Entrada Unica
    public static CadastroUnificadoWS ws = null;
    static {

        try {

            tryConnection();
        }
        catch (Exception ex) {

            ex.printStackTrace();
            ws = null;

            // Inicia o timer para renovar a conexão com o Entrada Única
            new Timer().schedule(taskTryConnection, TMR_INIT_CONNECTION, TMR_PERIOD_CONNECTION);
        }
    }

    /**
     * Conexão com a aplicação Entrada Única
     */
    private static synchronized void tryConnection() {

        ws = new CadastroUnificadoWS(Configuracoes.ENTRADA_UNICA_CLIENTE_ID,
                Configuracoes.ENTRADA_UNICA_CLIENTE_SECRET,
                Configuracoes.ENTRADA_UNICA_URL_PORTAL_SEGURANCA,
                Configuracoes.ENTRADA_UNICA_URL_CADASTRO_UNIFICADO);
    }

    /**
     * Construtor
     * @param clientId
     * @param clientSecret
     * @param urlPortal
     * @param urlCadastro
     */
    public CadastroUnificadoWS(String clientId, String clientSecret, String urlPortal, String urlCadastro) {
        super(clientId, clientSecret, urlPortal, urlCadastro);
    }


    /**
     * Retorna as pessoas associados ao perfil
     * @return
     */
    public List<Pessoa> getPessoasByFiltro(FiltroPessoa filtroPessoa) {

        PessoaFiltroResult pessoasNoEntradaUnica = this.buscarPessoasComFiltro(filtroPessoa);

        if(pessoasNoEntradaUnica != null) {

            return pessoasNoEntradaUnica.pageItems;
        }

        return null;
    }

    /**
     * Retorna os usuários associados ao perfil
     * @return
     */

    public List<Usuario> getUsuariosByPerfil(String codigoPerfil) {

        Usuario[] usuarios = this.findUsuariosByPerfil(codigoPerfil);

        return Arrays.asList(usuarios);

    }

    /**
     * Retorna os usuários associados ao perfil e setores
     * @return
     */
    public List<Usuario> getUsuariosByPerfilAndSetores(String codigoPerfil, String siglaSetor) {

        Usuario[] usuarios = this.findUsuariosByPerfilAndSetores(codigoPerfil, siglaSetor);

        return Arrays.asList(usuarios);
    }

    /**
     * Busca setor por sigla.
     * @return
     */
    public Setor findBySigla(String siglaSetor) {

        return  this.getSetorBySigla(siglaSetor);
    }


    /**
     * Busca setor por sigla e nível.
     * @return
     */
    public List<String> getSiglasSetoresByNivel(String siglaSetor, int nivel){

        String [] siglas = this.getSiglaSetoresByNivel(siglaSetor, nivel);

        return  Arrays.asList(siglas);
    }

    /**
     * Busca pessoa por cpfCnpj.
     * @return
     */
    public Pessoa getPessoa(String cpfCnpj) {

        Pessoa pessoa = null;

        if(cpfCnpj.length() == 11) {
            pessoa = this.buscarPessoaFisicaPeloCpf(cpfCnpj);
        } else if(cpfCnpj.length() == 14) {
            pessoa = this.buscarPessoaJuridicaPeloCnpj(cpfCnpj);
        }

        return pessoa;
    }

    public List<String> getEmailProprietarioResponsaveis(List<Pessoa> proprietarios, List<Pessoa> responsaveisLegais, List<Pessoa> responsaveisTecnicos, List<String> destinatarios){

        proprietarios.forEach(proprietario -> {
            destinatarios.add(proprietario.contatos.stream().filter(contato -> contato.principal && contato.tipo.descricao.equals("Email")).findFirst().orElseThrow(null).valor);
        });

        responsaveisTecnicos.forEach(responsavelTecnico -> {
            destinatarios.add(responsavelTecnico.contatos.stream().filter(contato -> contato.principal && contato.tipo.descricao.equals("Email")).findFirst().orElseThrow(null).valor);
        });

        responsaveisLegais.forEach(responsavelLegal -> {
            destinatarios.add(responsavelLegal.contatos.stream().filter(contato -> contato.principal && contato.tipo.descricao.equals("Email")).findFirst().orElseThrow(null).valor);
        });

        return destinatarios;

    }

}
