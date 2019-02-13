package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ning.http.util.Base64;
import main.java.br.ufla.lemaf.OAuthCadastroUnificadoToken;
import main.java.br.ufla.lemaf.OAuthClientCadastroUnificadoException;
import main.java.br.ufla.lemaf.deserializers.DateDeserializer;
import main.java.br.ufla.lemaf.httpClient.MGAHttpClient;
import main.java.br.ufla.lemaf.httpClient.WSHttpClientRequest;
import main.java.br.ufla.lemaf.httpClient.WSHttpResponse;
import main.java.br.ufla.lemaf.serializers.DateSerializer;
import main.java.br.ufla.lemaf.utils.Time;
import models.EntradaUnica.Usuario;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class OAuthClientCadastroUnificadoLicenciamento {
    private String clientId;
    private String clientSecret;
    private String authorizationRequest;
    private String urlPortal;
    private OAuthCadastroUnificadoToken token;
    private static final String URL_SERVICE_GET_TOKEN = "/external/token";
    private static final String URL_SERVICE_GET_LOGIN = "/external/login";
    private static final String URL_SERVICE_GET_IS_LOGGED = "/external/session/estaLogado";
    private static final String URL_USUARIO_POSSUI_LOGIN = "/public/temUsuarioComLogin/";
    private static final String URL_SEARCH_USER_BY_SESSION_KEY = "/external/usuario/buscaPorSessionKey";
    private static final String URL_ALLOWED_ACCESS_SERVICE = "/external/modulo/allowed/access";
    private static final String CONTENT_TYPE_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String GRANT_TYPE = "client_credentials";
    public static final Gson gson = (new GsonBuilder()).serializeSpecialFloatingPointValues().registerTypeAdapter(Date.class, new DateSerializer()).registerTypeAdapter(Date.class, new DateDeserializer()).create();

    public OAuthClientCadastroUnificadoLicenciamento(String clientId, String clientSecret, String urlPortal) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.urlPortal = urlPortal;
        this.generateToken();
    }

    private void generateToken() {
        long nowInMs = (new Date()).getTime();
        if (this.token == null || nowInMs > this.token.timeExpireInMs) {
            this.token = this.requestToken();
            this.token.tick = (new Date()).getTime();
            this.token.timeExpireInMs = this.token.tick + (long)(Time.parseDuration(this.token.expires_in) * 1000);
            this.authorizationRequest = this.token.token_type + " " + this.token.access_token;
        }

    }

    public <T> T executeRequestGet(String urlService, Class<T> classResult) {
        if (this.token == null) {
            throw new OAuthClientCadastroUnificadoException(OAuthClientCadastroUnificadoException.OAuthClientExceptionCode.ERROR_REQUEST_TOKEN_NOT_EXIST, -1, "token inexistente");
        } else {
            this.generateToken();
            WSHttpClientRequest request = new WSHttpClientRequest(MGAHttpClient.TPRequest.GET, urlService, 10000);
            request.addHeader("authorization", this.authorizationRequest);
            request.addHeader("Content-Type", "application/x-www-form-urlencoded");
            return this.getResponseFromRequest(request, classResult, OAuthClientCadastroUnificadoException.OAuthClientExceptionCode.ERROR_REQUEST_GET, "");
        }
    }

    public <T> T executeRequestPost(String urlService, Map<String, String> parameters, Class<T> classResult) {
        if (this.token == null) {
            throw new OAuthClientCadastroUnificadoException(OAuthClientCadastroUnificadoException.OAuthClientExceptionCode.ERROR_REQUEST_TOKEN_NOT_EXIST, -1, "token inexistente");
        } else {
            this.generateToken();
            WSHttpClientRequest request = new WSHttpClientRequest(MGAHttpClient.TPRequest.POST, urlService, 10000);
            request.addHeader("authorization", this.authorizationRequest);
            request.addHeader("Content-Type", "application/x-www-form-urlencoded");
            if (parameters != null) {
                Iterator var5 = parameters.keySet().iterator();

                while(var5.hasNext()) {
                    String key = (String)var5.next();
                    request.addParam(key, (String)parameters.get(key));
                }
            }

            return this.getResponseFromRequest(request, classResult, OAuthClientCadastroUnificadoException.OAuthClientExceptionCode.ERROR_REQUEST_POST, "");
        }
    }

    public <T> T executeRequestPostJson(String urlService, String json, Class<T> classResult) {
        if (this.token == null) {
            throw new OAuthClientCadastroUnificadoException(OAuthClientCadastroUnificadoException.OAuthClientExceptionCode.ERROR_REQUEST_TOKEN_NOT_EXIST, -1, "token inexistente");
        } else {
            this.generateToken();
            WSHttpClientRequest request = new WSHttpClientRequest(MGAHttpClient.TPRequest.POST, urlService, 10000);
            request.addHeader("authorization", this.authorizationRequest);
            request.addHeader("Content-Type", "application/json");
            request.addJsonStr(json);
            return this.getResponseFromRequest(request, classResult, OAuthClientCadastroUnificadoException.OAuthClientExceptionCode.ERROR_REQUEST_POST, "");
        }
    }

    public <T> T executeRequestPutJson(String urlService, String json, Class<T> classResult) {
        if (this.token == null) {
            throw new OAuthClientCadastroUnificadoException(OAuthClientCadastroUnificadoException.OAuthClientExceptionCode.ERROR_REQUEST_TOKEN_NOT_EXIST, -1, "token inexistente");
        } else {
            this.generateToken();
            WSHttpClientRequest request = new WSHttpClientRequest(MGAHttpClient.TPRequest.PUT, urlService, 10000);
            request.addHeader("authorization", this.authorizationRequest);
            request.addHeader("Content-Type", "application/json");
            request.addJsonStr(json);
            return this.getResponseFromRequest(request, classResult, OAuthClientCadastroUnificadoException.OAuthClientExceptionCode.ERROR_REQUEST_PUT, "");
        }
    }

    public OAuthCadastroUnificadoToken getToken() {
        return this.token;
    }

    private OAuthCadastroUnificadoToken requestToken() {
        String url = this.urlPortal + "/external/token";
        WSHttpClientRequest request = new WSHttpClientRequest(MGAHttpClient.TPRequest.POST, url, 10000);
        String strEncode = this.clientId + ":" + this.clientSecret;
        String authorizationGetToken = "Basic " + new String(Base64.encode(strEncode.getBytes()));
        request.addHeader("authorization", authorizationGetToken);
        request.addHeader("Content-Type", "application/x-www-form-urlencoded");
        request.addParam("grant_type", "client_credentials");
        return (OAuthCadastroUnificadoToken)this.getResponseFromRequest(request, OAuthCadastroUnificadoToken.class, OAuthClientCadastroUnificadoException.OAuthClientExceptionCode.ERROR_GET_TOKEN, "Erro ao tentar obter o token");
    }

    private <T> T getResponseFromRequest(WSHttpClientRequest request, Class<T> classResult, OAuthClientCadastroUnificadoException.OAuthClientExceptionCode errorCode, String messageError) {
        WSHttpResponse response = null;

        try {
            response = request.execute();
            if (response.getStatus() == 200) {
                return gson.fromJson(response.getBody(), classResult);
            }

            System.err.println("Error -> Status: " + response.getStatus() + " Code: " + errorCode + " Message: " + response.getBody() + " " + messageError);
            throw new OAuthClientCadastroUnificadoException(errorCode, response.getStatus(), response.getBody() + " " + messageError);
        } catch (InterruptedException var7) {
            var7.printStackTrace();
        } catch (ExecutionException var8) {
            var8.printStackTrace();
        } catch (IOException var9) {
            var9.printStackTrace();
        }

        return null;
    }

    public boolean isLogged(String userSessionKey) {
        Map<String, String> parametros = new HashMap();
        parametros.put("sessionKey", userSessionKey);
        return (Boolean)this.executeRequestPost(this.urlPortal + "/external/session/estaLogado", parametros, Boolean.class);
    }

    public Usuario login(String usuario, String senha) {
        Map<String, String> parametros = new HashMap();
        parametros.put("username", usuario);
        parametros.put("password", senha);
        return (Usuario)this.executeRequestPost(this.urlPortal + "/external/login", parametros, Usuario.class);
    }

    public Boolean isUser(String cpfCnpj) {
        return (Boolean)this.executeRequestGet(this.urlPortal + "/public/temUsuarioComLogin/" + cpfCnpj, Boolean.class);
    }

    public Usuario searchBySessionKey(String sessionKey) {
        Map<String, String> parametros = new HashMap();
        parametros.put("sessionKeyEntradaUnica", sessionKey);
        return (Usuario)this.executeRequestPost(this.urlPortal + "/external/usuario/buscaPorSessionKey", parametros, Usuario.class);
    }

    protected void finalize() throws Throwable {
        System.out.print("OAuthClientCadastroUnificado::finalize()");
        super.finalize();
    }

    public boolean isAllowedAccess(String userSession, String service, String address) {
        Map<String, String> params = new HashMap();
        params.put("userSession", userSession);
        params.put("service", service);
        params.put("address", address);
        return (Boolean)this.executeRequestPost(this.urlPortal + "/external/modulo/allowed/access", params, Boolean.class);
    }
}
