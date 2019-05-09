package security;


import java.io.Serializable;


public class ManejoSessao implements Serializable {

	public String token;
	public String expires;

	public ManejoSessao() {

	}

	public ManejoSessao(String token, String expires) {

		this.token = token;
		this.expires = expires;

	}


}