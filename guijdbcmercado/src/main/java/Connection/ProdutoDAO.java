package Connection;

import Model.Produto;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    private Connection connection;

    // Construtor que inicializa a conexão e cria a tabela de produtos se não
    // existir
    public ProdutoDAO() {
        this.connection = ConnectionFactory.getConnection();
        criarTabelaProdutos();
    }

    // Cria a tabela de produtos no banco de dados
    private void criarTabelaProdutos() {
        String sql = "CREATE TABLE IF NOT EXISTS produtos (codigo_barra VARCHAR(255), nome VARCHAR(255), quantidade INT, preco NUMERIC)";
        try (Statement stmt = this.connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabela de produtos criada com sucesso.");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar a tabela de produtos: " + e.getMessage(), e);
        }
    }

    public void adicionarProduto(Produto produto) {
        String sql = "INSERT INTO produtos (codigo_barra, nome, quantidade, preco) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, produto.getCodigoBarra());
            preparedStatement.setString(2, produto.getNome());
            preparedStatement.setInt(3, produto.getQuantidade());
            preparedStatement.setBigDecimal(4, BigDecimal.valueOf(produto.getPreco()));
            preparedStatement.executeUpdate();
            System.out.println("Produto adicionado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar produto: " + e.getMessage());
            rollbackTransacao();
        }
    }

    public List<Produto> listarTodos() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Produto> produtos = new ArrayList<>();

        try {
            obterConexao();
            stmt = connection.prepareStatement("SELECT * FROM produtos");
            rs = stmt.executeQuery();
            while (rs.next()) {
                Produto produto = new Produto(
                        rs.getString("codigo_barra"),
                        rs.getString("nome"),
                        rs.getInt("quantidade"),
                        rs.getDouble("preco"));
                produtos.add(produto);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            ConnectionFactory.closeConnection(connection, stmt, rs);
        }
        return produtos;
    }

    private boolean registroExiste(String codigoBarra) throws SQLException {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT 1 FROM produtos WHERE codigo_barra = ?")) {
            preparedStatement.setString(1, codigoBarra);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public void atualizarTabelaBancoDados(List<Produto> produtos) {
        try {
            obterConexao();
            connection.setAutoCommit(false); // Desativa o autoCommit

            for (Produto produto : produtos) {
                if (registroExiste(produto.getCodigoBarra())) {
                    atualizarProduto(produto);
                } else {
                    adicionarProduto(produto);
                }
            }

            connection.commit(); // Realiza o commit se não houve erros
        } catch (SQLException e) {
            rollbackTransacao();
            System.err.println("Erro ao atualizar tabela no banco de dados.");
            e.printStackTrace();
        } finally {
            fecharConexao();
        }
    }

    private void atualizarProduto(Produto produto) throws SQLException {
        String sql = "UPDATE produtos SET nome=?, quantidade=?, preco=? WHERE codigo_barra=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, produto.getNome());
            preparedStatement.setInt(2, produto.getQuantidade());
            preparedStatement.setDouble(3, produto.getPreco());
            preparedStatement.setString(4, produto.getCodigoBarra());
            preparedStatement.executeUpdate();
            System.out.println("Produto atualizado com sucesso!");
        }
    }

    private void obterConexao() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = ConnectionFactory.getConnection();
        }
    }

    private void rollbackTransacao() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.err.println("Erro no rollback: " + e.getMessage());
        }
    }

    public void fecharConexao() {
        ConnectionFactory.closeConnection(this.connection);
    }
}
