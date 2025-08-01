/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Adm
 */
import java.sql.PreparedStatement;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ProdutosDAO {

    Connection conn;
    PreparedStatement prep;
    ResultSet resultset;
    ArrayList<ProdutosDTO> listagem = new ArrayList<>();

    public void cadastrarProduto(ProdutosDTO produto) {
        String sql = "INSERT INTO produtos (nome, valor, status) VALUES (?, ?, ?)";

        try {
            conn = new conectaDAO().connectDB();
            prep = conn.prepareStatement(sql);
            prep.setString(1, produto.getNome());
            prep.setInt(2, produto.getValor());
            prep.setString(3, produto.getStatus());

            prep.executeUpdate();
            JOptionPane.showMessageDialog(null, "Produto cadastrado com sucesso!");

        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar produto: " + erro.getMessage());
        }
    }

    public ArrayList<ProdutosDTO> listarProdutos() {
        ArrayList<ProdutosDTO> lista = new ArrayList<>();

        String sql = "SELECT * FROM produtos";

        try {
            conn = new conectaDAO().connectDB();
            prep = conn.prepareStatement(sql);
            resultset = prep.executeQuery();

            while (resultset.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(resultset.getInt("id"));
                produto.setNome(resultset.getString("nome"));
                produto.setValor(resultset.getInt("valor"));
                produto.setStatus(resultset.getString("status"));

                lista.add(produto);
            }

        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, "Erro ao listar produtos: " + erro.getMessage());
        }

        return lista;
    }

    public void venderProduto(int id) {
        String sqlBusca = "SELECT nome, status FROM produtos WHERE id = ?";
        String sqlVenda = "UPDATE produtos SET status = ? WHERE id = ?";

        try {
            conn = new conectaDAO().connectDB();

            // Busca do status atual
            prep = conn.prepareStatement(sqlBusca);
            prep.setInt(1, id);
            resultset = prep.executeQuery();

            if (resultset.next()) {
                String nomeProduto = resultset.getString("nome");
                String statusAtual = resultset.getString("status");

                if (!statusAtual.equalsIgnoreCase("A Venda")) {
                    JOptionPane.showMessageDialog(null, "O produto \"" + nomeProduto + "\" já foi vendido.");
                    return;
                }

                // Atualização do status para 'Vendido'
                prep = conn.prepareStatement(sqlVenda);
                prep.setString(1, "Vendido");
                prep.setInt(2, id);
                prep.executeUpdate();

                JOptionPane.showMessageDialog(null, "Produto \"" + nomeProduto + "\" vendido com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Produto com ID " + id + " não encontrado.");
            }

        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, "Erro ao vender produto: " + erro.getMessage());
        }
    }

}
