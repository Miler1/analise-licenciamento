package deserializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import br.ufla.lemaf.beans.pessoa.Perfil;
import models.EntradaUnica.Setor;
import models.EntradaUnica.Usuario;
import models.UsuarioAnalise;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDeserializer implements JsonDeserializer<List<UsuarioAnalise>> {

	@Override
	public List<UsuarioAnalise> deserialize(JsonElement json, Type type, JsonDeserializationContext context) {

		List<UsuarioAnalise> usuarios = new ArrayList<>();

		JsonArray usuariosJson = json.getAsJsonArray();

		for (int i = 0; i < usuariosJson.size(); i++) {

			UsuarioAnalise usuario = new UsuarioAnalise();
			usuario.usuarioEntradaUnica = new Usuario();

			JsonObject jsonObject = usuariosJson.get(i).getAsJsonObject();

			usuario.login = jsonObject.get("login") == null ? null : jsonObject.get("login").getAsString();

			usuario.usuarioEntradaUnica.nome = jsonObject.get("nome") == null ? null : jsonObject.get("nome").getAsString();
			usuario.usuarioEntradaUnica.login = jsonObject.get("login") == null ? null : jsonObject.get("login").getAsString();
			usuario.usuarioEntradaUnica.perfis = getPerfis(jsonObject.get("perfis") == null ? null : jsonObject.get("perfis").getAsJsonArray());
			usuario.usuarioEntradaUnica.setores = getSetores(jsonObject.get("setores") == null ? null : jsonObject.get("setores").getAsJsonArray());
			usuario.usuarioEntradaUnica.email = jsonObject.get("email") == null ? null : jsonObject.get("email").getAsString();

			usuarios.add(usuario);
		}

		return usuarios;
	}

	private List<Perfil> getPerfis(JsonArray perfisJson){

		if(perfisJson == null){
			return null;
		}

		List<Perfil> perfis = null;

		for (int perfilIndex = 0; perfilIndex < perfisJson.size(); perfilIndex++) {

			Perfil perfil = new Perfil();

			JsonObject jsonObject = perfisJson.get(perfilIndex).getAsJsonObject();

			perfil.id = jsonObject.get("id") == null ? null : jsonObject.get("id").getAsInt();
			perfil.nome = jsonObject.get("nome") == null ? null : jsonObject.get("nome").getAsString();
			perfil.codigo = jsonObject.get("codigo") == null ? null : jsonObject.get("codigo").getAsString();
			perfis.add(perfil);
		}

		return perfis;
	}

	private List<br.ufla.lemaf.beans.pessoa.Setor> getSetores(JsonArray setoresJson){

		if(setoresJson == null){
			return null;
		}

		List<br.ufla.lemaf.beans.pessoa.Setor> setores = null;

		for (int perfilIndex = 0; perfilIndex < setoresJson.size(); perfilIndex++) {

			br.ufla.lemaf.beans.pessoa.Setor setor = new br.ufla.lemaf.beans.pessoa.Setor();

			JsonObject jsonObject = setoresJson.get(perfilIndex).getAsJsonObject();

			setor.nome = jsonObject.get("nome") == null ? null : jsonObject.get("nome").getAsString();
			setor.sigla = jsonObject.get("sigla") == null ? null : jsonObject.get("sigla").getAsString();;
			setores.add(setor);
		}

		return setores;
	}

}