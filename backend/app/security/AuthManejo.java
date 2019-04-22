package security;

import exceptions.WebServiceException;
import models.manejoDigital.analise.analiseShape.ResponseToken;
import play.cache.Cache;
import play.mvc.Http;
import play.mvc.Scope;
import play.mvc.Scope.Session;
import utils.Configuracoes;
import utils.WebService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AuthManejo {

	private static final String CACHE_PREFIX = "MANEJO_USER_";

	public static String getToken(String cacheId) {

		String key = CACHE_PREFIX + cacheId;

		AuthManejo.keepLiveToken(cacheId);

		ManejoSessao manejoSessao = Cache.get(key, ManejoSessao.class);

		return manejoSessao.token;
	}

	private static void setToken(String cacheId) {

		String key = CACHE_PREFIX + cacheId;

		ResponseToken responseToken = AuthManejo.generateToken();
		ManejoSessao manejoSessao = new ManejoSessao(responseToken.token, responseToken.expires);
		Cache.set(key, manejoSessao);
	}

	private static void keepLiveToken(String cacheId) {

		if (!AuthManejo.possuiToken(cacheId)) {

			setToken(cacheId);
		}
	}

	private static boolean possuiToken(String cacheId) {

		String key = CACHE_PREFIX + cacheId;

		ManejoSessao manejoSessao = Cache.get(key, ManejoSessao.class);

		return Cache.get(key, ManejoSessao.class) != null
				&& Long.parseLong(manejoSessao.expires) >  new Date().getTime();

	}

	private static ResponseToken generateToken() {

		WebService webService = new WebService();

		Map<String, Object> params = new HashMap<>();
		params.put("f", "json");

		// TODO futuramente usar id do Semas do usuário,
		params.put("username", Configuracoes.ANALISE_SHAPE_TOKEN_USERNAME);
		params.put("password", Configuracoes.ANALISE_SHAPE_TOKEN_PASSWORD);
		params.put("referer", Configuracoes.ANALISE_SHAPE_TOKEN_REFERER);
		params.put("expiration", Configuracoes.ANALISE_SHAPE_TOKEN_EXPIRATION);

		ResponseToken response = webService.post(Configuracoes.ANALISE_SHAPE_TOKEN_URL, params, ResponseToken.class);

		if (response.error != null) {

			throw new WebServiceException("Erro ao obter token: " + response.error.message);

		} else {

			return response;
		}
	}
}
