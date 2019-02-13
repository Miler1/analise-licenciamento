package utils;

import main.java.br.ufla.lemaf.OAuthClientCadastroUnificadoException;
import main.java.br.ufla.lemaf.beans.Configuracao;
import main.java.br.ufla.lemaf.beans.Empreendimento;
import main.java.br.ufla.lemaf.beans.EmpreendimentoFiltroResult;
import main.java.br.ufla.lemaf.beans.EmpreendimentosPessoa;
import main.java.br.ufla.lemaf.beans.FiltroEmpreendimento;
import main.java.br.ufla.lemaf.beans.Message;
import main.java.br.ufla.lemaf.beans.PessoaFiltroResult;
import main.java.br.ufla.lemaf.beans.pessoa.Estado;
import main.java.br.ufla.lemaf.beans.pessoa.FiltroPessoa;
import main.java.br.ufla.lemaf.beans.pessoa.Municipio;
import main.java.br.ufla.lemaf.beans.pessoa.Pais;
import main.java.br.ufla.lemaf.beans.pessoa.Perfil;
import main.java.br.ufla.lemaf.beans.pessoa.Pessoa;
import main.java.br.ufla.lemaf.beans.pessoa.TipoContato;

import java.util.HashMap;
import java.util.Map;

public class CadastroUnificadoPessoaServiceLicenciamento extends OAuthClientCadastroUnificadoLicenciamento {
    private static String URL_BUSCAR_PESSOA_FISICA = "/external/pessoaFisica/buscarPorCpf/";
    private static String URL_BUSCAR_PESSOA_JURIDICA = "/external/pessoaJuridica/buscarPorCnpj/";
    private static String URL_CADASTRAR_EMPREENDIMENTO_PESSOA = "/external/empreendimento";
    private static String URL_BUSCAR_EMPREENDIMENTOS_PESSOA = "/external/empreendimentos/";
    private static String URL_BUSCAR_EMPREENDIMENTOS_COM_FILTRO = "/external/empreendimentos";
    private static String URL_BUSCAR_PESSOA_COM_FILTRO = "/external/pessoas";
    private static String URL_EDITAR_PESSOA_FISICA = "/external/pessoaFisica";
    private static String URL_EDITAR_PESSOA_JURIDICA = "/external/pessoaJuridica";
    private static String URL_CRIAR_PESSOA_JURIDICA = "/external/pessoaJuridica";
    private static String URL_CRIAR_PESSOA_FISICA = "/external/pessoaFisica";
    private static String URL_BURCAR_PESSOA_COM_FILTRO_TODOS_MODULOS = "/external/pessoas/todosModulos";
    private static String URL_ATUALIZAR_PERFIL = "/external/pessoas/atualizaPerfil";
    private static String URL_ADICIONAR_PERFIL = "/external/pessoas/adicionaPerfil";
    private static String URL_REMOVER_PERFIL = "/external/pessoas/removePerfil";
    private static final String URL_BUSCA_USUARIO_SESSION_KEY = "/external/usuario/buscaPorSessionKey";
    private static String URL_TEM_PESSOA_CNPJ = "/public/pessoaJuridica/temPessoaComCnpj/";
    private static String URL_TEM_PESSOA_CPF = "/public/pessoaFisica/temPessoaComCpf/";
    private static String URL_PUBLIC_PAISES = "/public/paises";
    private static String URL_PUBLIC_PAIS_ESTADOS = "/public/pais/{id}/estados";
    private static String URL_PUBLIC_ESTADO_MUNICIPIOS = "/public/estado/{id}/municipios";
    private static String URL_PUBLIC_TIPOS_CONTATO = "/public/tiposContato";
    private static String URL_PUBLIC_CONFIG = "/public/config";
    private static String URL_USUARIO_POSSUI_LOGIN = "/public/temUsuarioComLogin/";
    private static String URL_PUBLIC_PERFIS = "/public/perfis ";
    private static String URL_PUBLIC_MOTIVOS = "/public/motivos ";
    private static String URL_PUBLIC_PERFIL_CODIGO = "/public/perfil/{codigo} ";
    private String urlCadastro;

    public CadastroUnificadoPessoaServiceLicenciamento(String clientId, String clientSecret, String urlPortal, String urlCadastro) {
        super(clientId, clientSecret, urlPortal);
        this.urlCadastro = urlCadastro;
    }

    public Pessoa buscarPessoaFisicaPeloCpf(String cpf) {
        try {
            return (Pessoa)this.executeRequestGet(this.urlCadastro + URL_BUSCAR_PESSOA_FISICA + cpf, Pessoa.class);
        } catch (OAuthClientCadastroUnificadoException var3) {
            return null;
        }
    }

    public Pessoa buscarPessoaJuridicaPeloCnpj(String cnpj) {
        try {
            return (Pessoa)this.executeRequestGet(this.urlCadastro + URL_BUSCAR_PESSOA_JURIDICA + cnpj, Pessoa.class);
        } catch (OAuthClientCadastroUnificadoException var3) {
            return null;
        }
    }

    public Message cadastrarEmpreendimentoPessoa(Empreendimento empreendimento) {
        try {
            String json = gson.toJson(empreendimento);
            return (Message)this.executeRequestPostJson(this.urlCadastro + URL_CADASTRAR_EMPREENDIMENTO_PESSOA, json, Message.class);
        } catch (OAuthClientCadastroUnificadoException var3) {
            return new Message("Error -> Status: " + var3.status + " Code: " + var3.code + " Message: " + var3.getMessage());
        }
    }

    public EmpreendimentosPessoa buscarEmpreendimentosPessoa(String cpfCnpj) {
        try {
            return (EmpreendimentosPessoa)this.executeRequestGet(this.urlCadastro + URL_BUSCAR_EMPREENDIMENTOS_PESSOA + cpfCnpj, EmpreendimentosPessoa.class);
        } catch (OAuthClientCadastroUnificadoException var3) {
            return null;
        }
    }

    public EmpreendimentoFiltroResult buscarEmpreendimentosComFiltro(FiltroEmpreendimento filtro) {
        try {
            String json = gson.toJson(filtro);
            return (EmpreendimentoFiltroResult)this.executeRequestPostJson(this.urlCadastro + URL_BUSCAR_EMPREENDIMENTOS_COM_FILTRO, json, EmpreendimentoFiltroResult.class);
        } catch (OAuthClientCadastroUnificadoException var3) {
            return null;
        }
    }

    public PessoaFiltroResult buscarPessoasComFiltro(FiltroPessoa filtro) {
        try {
            String json = gson.toJson(filtro);
            return (PessoaFiltroResult)this.executeRequestPostJson(this.urlCadastro + URL_BUSCAR_PESSOA_COM_FILTRO, json, PessoaFiltroResult.class);
        } catch (OAuthClientCadastroUnificadoException var3) {
            return null;
        }
    }

    public PessoaFiltroResult buscarPessoasComFiltroAll(FiltroPessoa filtro) {
        try {
            String json = gson.toJson(filtro);
            return (PessoaFiltroResult)this.executeRequestPostJson(this.urlCadastro + URL_BURCAR_PESSOA_COM_FILTRO_TODOS_MODULOS, json, PessoaFiltroResult.class);
        } catch (OAuthClientCadastroUnificadoException var3) {
            return null;
        }
    }

    public Message atualizarPerfilPeloCpfCnpj(String cpfCnpj, String codigoPerfil) {
        try {
            Map<String, String> params = new HashMap();
            params.put("cpfCnpj", cpfCnpj);
            params.put("codigoPerfil", codigoPerfil);
            Message message = (Message)this.executeRequestPost(this.urlCadastro + URL_ATUALIZAR_PERFIL, params, Message.class);
            return message;
        } catch (OAuthClientCadastroUnificadoException var5) {
            return null;
        }
    }

    public Message adicionarPerfilPeloCpfCnpj(String cpfCnpj, String codigoPerfil) {
        try {
            Map<String, String> params = new HashMap();
            params.put("cpfCnpj", cpfCnpj);
            params.put("codigoPerfil", codigoPerfil);
            Message message = (Message)this.executeRequestPost(this.urlCadastro + URL_ADICIONAR_PERFIL, params, Message.class);
            return message;
        } catch (OAuthClientCadastroUnificadoException var5) {
            return null;
        }
    }

    public Message removerPerfilPeloCpfCnpj(String cpfCnpj, String codigoPerfil) {
        try {
            Map<String, String> params = new HashMap();
            params.put("cpfCnpj", cpfCnpj);
            params.put("codigoPerfil", codigoPerfil);
            Message message = (Message)this.executeRequestPost(this.urlCadastro + URL_REMOVER_PERFIL, params, Message.class);
            return message;
        } catch (OAuthClientCadastroUnificadoException var5) {
            return null;
        }
    }

    public Message alterarDadosPessoaFisica(Pessoa pessoa) {
        try {
            String json = gson.toJson(pessoa);
            return (Message)this.executeRequestPutJson(this.urlCadastro + URL_EDITAR_PESSOA_FISICA, json, Message.class);
        } catch (OAuthClientCadastroUnificadoException var3) {
            return null;
        }
    }

    public Message alterarDadosPessoaJuridica(Pessoa pessoa) {
        try {
            String json = gson.toJson(pessoa);
            return (Message)this.executeRequestPutJson(this.urlCadastro + URL_EDITAR_PESSOA_JURIDICA, json, Message.class);
        } catch (OAuthClientCadastroUnificadoException var3) {
            return null;
        }
    }

    public Message cadastrarPessoaJuridica(Pessoa pessoa) {
        try {
            String json = gson.toJson(pessoa);
            return (Message)this.executeRequestPostJson(this.urlCadastro + URL_CRIAR_PESSOA_JURIDICA, json, Message.class);
        } catch (OAuthClientCadastroUnificadoException var3) {
            return null;
        }
    }

    public Message cadastrarPessoaFisica(Pessoa pessoa) {
        try {
            String json = gson.toJson(pessoa);
            return (Message)this.executeRequestPostJson(this.urlCadastro + URL_CRIAR_PESSOA_FISICA, json, Message.class);
        } catch (OAuthClientCadastroUnificadoException var3) {
            return null;
        }
    }

    public Boolean temPessoaComCpfCnpj(String cpfCnpj) {
        try {
            return cpfCnpj.length() > 11 ? (Boolean)this.executeRequestGet(this.urlCadastro + URL_TEM_PESSOA_CNPJ + cpfCnpj, Boolean.class) : (Boolean)this.executeRequestGet(this.urlCadastro + URL_TEM_PESSOA_CPF + cpfCnpj, Boolean.class);
        } catch (OAuthClientCadastroUnificadoException var3) {
            return null;
        }
    }

    public Pais[] buscarPaises() {
        try {
            return (Pais[])this.executeRequestGet(this.urlCadastro + URL_PUBLIC_PAISES, Pais[].class);
        } catch (OAuthClientCadastroUnificadoException var2) {
            return null;
        }
    }

    public Estado[] buscarEstados(Integer idPais) {
        try {
            String paisEstado = URL_PUBLIC_PAIS_ESTADOS.replace("{id}", idPais.toString());
            return (Estado[])this.executeRequestGet(this.urlCadastro + paisEstado, Estado[].class);
        } catch (OAuthClientCadastroUnificadoException var3) {
            return null;
        }
    }

    public Municipio[] buscarMunicipio(Integer idEstado) {
        try {
            String paisEstado = URL_PUBLIC_ESTADO_MUNICIPIOS.replace("{id}", idEstado.toString());
            return (Municipio[])this.executeRequestGet(this.urlCadastro + paisEstado, Municipio[].class);
        } catch (OAuthClientCadastroUnificadoException var3) {
            return null;
        }
    }

    public TipoContato[] buscarTipoContato() {
        try {
            return (TipoContato[])this.executeRequestGet(this.urlCadastro + URL_PUBLIC_TIPOS_CONTATO, TipoContato[].class);
        } catch (OAuthClientCadastroUnificadoException var2) {
            return null;
        }
    }

    public Configuracao buscarConfiguracao() {
        try {
            return (Configuracao)this.executeRequestGet(this.urlCadastro + URL_PUBLIC_CONFIG, Configuracao.class);
        } catch (OAuthClientCadastroUnificadoException var2) {
            return null;
        }
    }

    public Perfil buscaPerfilPorCodigo(String codigo) {
        try {
            String perfilPorCodigo = URL_PUBLIC_PERFIL_CODIGO.replace("{codigo}", codigo);
            return (Perfil)this.executeRequestGet(this.urlCadastro + perfilPorCodigo, Perfil.class);
        } catch (OAuthClientCadastroUnificadoException var3) {
            return null;
        }
    }
}
