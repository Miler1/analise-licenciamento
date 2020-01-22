package utils;

import main.java.br.ufla.lemaf.beans.pessoa.Contato;
import main.java.br.ufla.lemaf.beans.pessoa.Pessoa;
import main.java.br.ufla.lemaf.beans.pessoa.TipoContato;

public class PessoaEuUtils {



    public static String getTelefone(Pessoa pessoa){
        String telefone;
        Contato contato = null;

        if(pessoa.contatos != null) {

            contato = pessoa.contatos.stream().filter(c -> c.tipo.id == TipoContato.ID_TELEFONE_CELULAR).findFirst().orElse(null);

            if (contato == null) {

                contato = pessoa.contatos.stream().filter(c -> c.tipo.id == TipoContato.ID_TELEFONE_RESIDENCIAL).findFirst().orElse(null);

                if (contato == null) {

                    contato = pessoa.contatos.stream().filter(c -> c.tipo.id == TipoContato.ID_TELEFONE_COMERCIAL).findFirst().orElse(null);
                }
            }
        }

        if(contato == null) {

            telefone = "-";
        } else {

            telefone = contato.valor;
        }

        return telefone;
    }

}
