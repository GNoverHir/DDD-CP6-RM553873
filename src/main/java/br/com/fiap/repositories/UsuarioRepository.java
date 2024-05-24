package br.com.fiap.repositories;

import br.com.fiap.models.Usuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import static java.util.Map.entry;

/**
 * Classe que manipula operações de banco de dados relacionadas aos usuários.
 */
public class UsuarioRepository {

    private static final Logger LOGGER = LogManager.getLogger(UsuarioRepository.class);
    private static String URL_ORACLE;
    private static String USER;
    private static String PASSWORD;

    // Nome da tabela e colunas do banco de dados
    private static final String TABLE_NAME = "T_SF_USUARIOS";
    private static final Map<String, String> TABLE_COLUMNS = Map.ofEntries(
            entry("NOME", "nm_usuario"),
            entry("EMAIL", "email_usuario"),
            entry("TELEFONE", "nr_telefone"),
            entry("TAMEMPRESA", "nr_tamanho_empresa"),
            entry("PAIS", "nm_pais"),
            entry("IDIOMA", "nm_idioma"),
            entry("SENHA", "senha")
    );

    // Consultas SQL como constantes
    private static final String SELECT_USER_QUERY = "SELECT * FROM %s WHERE email_usuario = ? AND senha = ?".formatted(TABLE_NAME);
    private static final String INSERT_USER_QUERY = "INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?, ?)"
            .formatted(TABLE_NAME,
                    TABLE_COLUMNS.get("NOME"),
                    TABLE_COLUMNS.get("EMAIL"),
                    TABLE_COLUMNS.get("TELEFONE"),
                    TABLE_COLUMNS.get("TAMEMPRESA"),
                    TABLE_COLUMNS.get("PAIS"),
                    TABLE_COLUMNS.get("IDIOMA"),
                    TABLE_COLUMNS.get("SENHA"));

    // Construtor que carrega as propriedades do banco de dados
    public UsuarioRepository() {
        loadDatabaseProperties();
    }

    /**
     * Carrega as propriedades do banco de dados do arquivo properties.
     */
    private void loadDatabaseProperties() {
        try (var inputStream = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            var properties = new Properties();
            properties.load(inputStream);
            URL_ORACLE = properties.getProperty("jdbc.url");
            USER = properties.getProperty("jdbc.username");
            PASSWORD = properties.getProperty("jdbc.password");
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar propriedades do banco de dados", e);
        }
    }

    /**
     * Verifica o login de um usuário com base no email e senha.
     * @param email o email do usuário
     * @param senha a senha do usuário
     * @return um Optional contendo o usuário se encontrado, caso contrário, um Optional vazio
     */
    public Optional<Usuario> verificarLogin(String email, String senha) {
        try (var connection = getConnection();
             var preparedStatement = createPreparedStatement(connection, SELECT_USER_QUERY, email, senha);
             var resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return Optional.of(mapResultSetToUsuario(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.error("Erro ao verificar login do usuário!", e);
        }
        LOGGER.info("Verificação de login realizada com sucesso!");
        return Optional.empty();
    }

    /**
     * Cadastra um novo usuário no banco de dados.
     * @param usuario o usuário a ser cadastrado
     */
    public void cadastrarUsuario(Usuario usuario) {
        try (var connection = getConnection();
             var preparedStatement = connection.prepareStatement(INSERT_USER_QUERY)) {
            mapUsuarioToPreparedStatement(usuario, preparedStatement);
            preparedStatement.executeUpdate();
            LOGGER.info("Usuário cadastrado com sucesso!");
        } catch (SQLException e) {
            LOGGER.error("Erro ao cadastrar usuário!", e);
        }
    }

    /**
     * Obtém uma conexão com o banco de dados.
     * @return a conexão com o banco de dados
     * @throws SQLException se ocorrer um erro ao obter a conexão
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL_ORACLE, USER, PASSWORD);
    }

    /**
     * Cria um PreparedStatement para a consulta de seleção de usuário.
     * @param connection a conexão com o banco de dados
     * @param query a consulta SQL
     * @param parameters os parâmetros da consulta
     * @return o PreparedStatement criado
     * @throws SQLException se ocorrer um erro ao criar o PreparedStatement
     */
    private PreparedStatement createPreparedStatement(Connection connection, String query, String... parameters) throws SQLException {
        var preparedStatement = connection.prepareStatement(query);
        for (int i = 0; i < parameters.length; i++) {
            preparedStatement.setString(i + 1, parameters[i]);
        }
        return preparedStatement;
    }

    /**
     * Mapeia um ResultSet para um objeto Usuario.
     * @param resultSet o ResultSet a ser mapeado
     * @return o objeto Usuario mapeado
     * @throws SQLException se ocorrer um erro ao mapear o ResultSet
     */
    private Usuario mapResultSetToUsuario(ResultSet resultSet) throws SQLException {
        return new Usuario(
                resultSet.getString(TABLE_COLUMNS.get("IDIOMA")),
                resultSet.getString(TABLE_COLUMNS.get("PAIS")),
                resultSet.getInt(TABLE_COLUMNS.get("TAMEMPRESA")),
                resultSet.getString(TABLE_COLUMNS.get("TELEFONE")),
                resultSet.getString(TABLE_COLUMNS.get("SENHA")),
                resultSet.getString(TABLE_COLUMNS.get("EMAIL")),
                resultSet.getString(TABLE_COLUMNS.get("NOME"))
        );
    }

    /**
     * Mapeia um objeto Usuario para um PreparedStatement.
     * @param usuario o objeto Usuario a ser mapeado
     * @param preparedStatement o PreparedStatement a ser preenchido
     * @throws SQLException se ocorrer um erro ao preencher o PreparedStatement
     */
    private void mapUsuarioToPreparedStatement(Usuario usuario, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, usuario.getNomeCompleto());
        preparedStatement.setString(2, usuario.getEmail());
        preparedStatement.setString(3, usuario.getTelefone());
        preparedStatement.setInt(4, usuario.getTamanhoEmpresa());
        preparedStatement.setString(5, usuario.getPais());
        preparedStatement.setString(6, usuario.getIdioma());
        preparedStatement.setString(7, usuario.getSenha());
    }
}
