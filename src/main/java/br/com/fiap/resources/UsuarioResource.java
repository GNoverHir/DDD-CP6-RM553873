package br.com.fiap.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import br.com.fiap.models.Usuario;
import br.com.fiap.repositories.UsuarioRepository;

import java.util.Optional;

/**
 * Classe que gerencia as requisições REST relacionadas aos usuários.
 */
@Path("usuario")
public class UsuarioResource {

    private final UsuarioRepository usuarioRepository;

    // Construtor que inicializa o repositório de usuários
    public UsuarioResource() {
        this.usuarioRepository = new UsuarioRepository();
    }

    /**
     * Realiza o login de um usuário.
     * @param usuario o objeto Usuario contendo email e senha
     * @return a resposta HTTP
     */
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loginUsuario(Usuario usuario) {
        Optional<Usuario> usuarioOptional = usuarioRepository.verificarLogin(usuario.getEmail(), usuario.getSenha());
        if (usuarioOptional.isPresent()) {
            return createResponse(Response.Status.OK);
        } else {
            return createResponse(Response.Status.UNAUTHORIZED);
        }
    }

    /**
     * Cadastra um novo usuário.
     * @param usuario o objeto Usuario a ser cadastrado
     * @return a resposta HTTP
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response cadastrarUsuario(Usuario usuario) {
        usuarioRepository.cadastrarUsuario(usuario);
        return createResponse(Response.Status.CREATED);
    }

    /**
     * Manipula a solicitação pré-voo para login.
     * @return a resposta HTTP
     */
    @OPTIONS
    @Path("/login")
    public Response handlePreflightLogin() {
        return createPreflightResponse();
    }

    /**
     * Manipula a solicitação pré-voo para cadastro.
     * @return a resposta HTTP
     */
    @OPTIONS
    public Response handlePreflightCadastro() {
        return createPreflightResponse();
    }

    /**
     * Cria uma resposta HTTP com cabeçalhos CORS.
     * @param status o status da resposta
     * @return a resposta HTTP
     */
    private Response createResponse(Response.Status status) {
        return Response.status(status)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .build();
    }

    /**
     * Cria uma resposta HTTP para uma solicitação pré-voo com cabeçalhos CORS.
     * @return a resposta HTTP
     */
    private Response createPreflightResponse() {
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .header("Access-Control-Max-Age", "1209600")
                .build();
    }
}
