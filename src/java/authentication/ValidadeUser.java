/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package authentication;

import java.util.Arrays;
import javax.crypto.SecretKey;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import javax.crypto.KeyGenerator;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.*;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import com.auth0.jwt.*;
import com.google.gson.Gson;
import contratos.Usuario;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import servlets.AutenticaUsuario;

/**
 *
 * @author lucas
 */
public class ValidadeUser {

    private String audience = "35f2d162-cfaf-4e03-8f52-41ae88395d41";

    public contratos.Usuario ValidarTokenPeloRequest(HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeyException, IOException, JWTVerifyException {
        String json = request.getHeader("Authorization");
        String url = request.getRequestURI();

        if (json == null || json.equals("null")) {
            return null;
        }

        Gson gson = new Gson();
        AutenticaUsuario.Token token = gson.fromJson(json, AutenticaUsuario.Token.class);

        contratos.Usuario usuario = ValidarToken(token.token, token.userName);
        return usuario;
    }

    public contratos.Usuario ValidarToken(String token, String secret) throws NoSuchAlgorithmException, InvalidKeyException, IOException, JWTVerifyException {
        try {
            Map<String, Object> decodedPayload
                    = new JWTVerifier(secret).verify(token);

            contratos.Usuario u = new Usuario();
            u.setEmail(decodedPayload.get("email").toString());
            u.setId(Integer.parseInt(decodedPayload.get("id").toString()));
            u.setNome(decodedPayload.get("nome").toString());
            u.setStatusID(Integer.parseInt(decodedPayload.get("statusID").toString()));
            u.setTipoUsuarioID(Integer.parseInt(decodedPayload.get("tipoUsuario").toString()));

            return u;
        } catch (SignatureException signatureException) {
            System.err.println("Invalid signature!");
            return null;
        } catch (IllegalStateException illegalStateException) {
            System.err.println("Invalid Token! " + illegalStateException);
            return null;
        }
    }

    public String GerarToken(String secret, contratos.Usuario usuario) {
        Map<String, Object> map = new HashMap<>();

        map.put("email", usuario.getEmail());
        map.put("id", usuario.getId());
        map.put("nome", usuario.getNome());
        map.put("statusID", usuario.getStatusID());
        map.put("tipoUsuario", usuario.getTipoUsuarioID());

        String a = new JWTSigner(secret).sign(map);
        return a;
    }
}
