package models.EntradaUnica;

import java.io.Serializable;
import java.util.Date;

public class Setor implements Serializable {

    public Integer id;
    public Date dataCadastro;
    public String nome;
    public String sigla;
    public TipoSetor tipo;
    public Setor setorPai;

    public Setor() {
    }

}
