package models.EntradaUnica;

import main.java.br.ufla.lemaf.beans.pessoa.Perfil;

import java.io.Serializable;
import java.util.List;

public class Usuario implements Serializable {
    public Integer id;
    public String login;
    public String nome;
    public String email;
    public List<Perfil> perfis;
    public List<Setor> setores;
    public Perfil perfilSelecionado;
    public Setor setorSelecionado;
    public String sessionKeyEntradaUnica;
    public boolean autenticadoViaToken;

    public Usuario() {
    }
}
