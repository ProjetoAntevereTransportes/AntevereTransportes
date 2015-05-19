package database;

import java.sql.*;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import contratos.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Pagamento {

    public Connection con;

    private void abrir() {
        con = Conexao.abrirConexao();
    }

    public boolean salvarVarios(contratos.Pagamento pagamento) {
        try {
            abrir();
            con.setAutoCommit(false);
            Statement st = con.createStatement();
            String sql = "insert into conta_pagar(nome, fornecedor_id) values(?, ?);";
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, pagamento.getNome());
            ps.setInt(2, pagamento.getFornecedorID());

            int sucesso = ps.executeUpdate();

            if (sucesso != 1) {
                con.rollback();
                return false;
            }

            ResultSet keys = ps.getGeneratedKeys();
            keys.next();
            int contaPagarID = keys.getInt(1);

            String sqlParcela = "insert into parcela_pagamento(vencimento, valor, "
                    + "descontos, juros, status_pagamento_id, descricao, conta_pagar_id) values(?,?,?,?,?,?,?)";

            for (contratos.ParcelaPagamento p : pagamento.getParcelas()) {
                PreparedStatement par = con.prepareStatement(sqlParcela);

                par.setString(1, new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(p.getVencimento()));
                par.setDouble(2, p.getValor());
                par.setDouble(3, 0);
                par.setDouble(4, 0);
                par.setInt(5, 1);
                par.setString(6, p.getDescricao());
                par.setInt(7, contaPagarID);

                int status = par.executeUpdate();
                if (status != 1) {
                    con.rollback();
                    return false;
                }
            }
            con.commit();

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(Pagamento.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return false;
        } finally {
            Conexao.fecharConexao(con);
        }
    }

    public List<contratos.Pagamento> listar() {
        try {
            abrir();
            Statement st = con.createStatement();
            String sql = "select * from conta_pagar;";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (rs != null) {
                List<contratos.Pagamento> lista = new ArrayList<contratos.Pagamento>();

                database.Fornecedor f = new Fornecedor();

                while (rs.next()) {
                    contratos.Pagamento p = new contratos.Pagamento();
                    p.setID(rs.getInt("id"));
                    p.setNome(rs.getString("nome"));
                    int fornecedorID = rs.getInt("fornecedor_id");

                    p.setFornecedor(f.pegarPeloID(fornecedorID));

                    List<contratos.ParcelaPagamento> parcelas = new ArrayList<contratos.ParcelaPagamento>();

                    p.setParcelas(listarParcelasPeloPagamentoID(p.getID()));

                    lista.add(p);
                }
                return lista;
            }

            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            Conexao.fecharConexao(con);
        }
    }

    public List<contratos.ParcelaPagamento> listarParcelasPeloPagamentoID(int pagamentoID) {
        try {
            abrir();
            Statement st = con.createStatement();
            String sql = "select parcela_pagamento.id,\n"
                    + "parcela_pagamento.vencimento,\n"
                    + "parcela_pagamento.conta_bancaria_id,\n"
                    + "parcela_pagamento.debito_automatico_id,\n"
                    + "parcela_pagamento.status_pagamento_id,\n"
                    + "parcela_pagamento.valor,\n"
                    + "parcela_pagamento.descontos,\n"
                    + "parcela_pagamento.juros,\n"
                    + "parcela_pagamento.data_pagamento,\n"
                    + "parcela_pagamento.comprovante,\n"
                    + "parcela_pagamento.descricao,\n"
                    + " status_pagamento.nome as 'status_nome',\n"
                    + " conta_bancaria.nome as 'conta_nome'\n"
                    + " from parcela_pagamento left join conta_bancaria on conta_bancaria.id = parcela_pagamento.conta_bancaria_id\n"
                    + "inner join status_pagamento on status_pagamento.id = parcela_pagamento.status_pagamento_id\n"
                    + "where parcela_pagamento.conta_pagar_id = ?;";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, pagamentoID);

            ResultSet rs = ps.executeQuery();

            if (rs != null) {
                List<contratos.ParcelaPagamento> lista = new ArrayList<contratos.ParcelaPagamento>();

                while (rs.next()) {
                    contratos.ParcelaPagamento p = new contratos.ParcelaPagamento();
                    p.setID(rs.getInt("id"));
                    p.setComprovante(rs.getString("comprovante"));
                    p.setContaBancariaID(rs.getInt("conta_bancaria_id"));
                    p.setDebitoAutomaticoID(rs.getInt("debito_automatico_id"));
                    p.setDescontos(rs.getDouble("descontos"));
                    p.setDescricao(rs.getString("descricao"));
                    p.setID(rs.getInt("id"));
                    p.setJuros(rs.getDouble("juros"));
                    p.setStatusPagamento(rs.getInt("status_pagamento_id"));
                    p.setValor(rs.getDouble("valor"));
                    p.setVencimento(rs.getDate("vencimento"));
                    p.setStatusNome(rs.getString("status_nome"));
                    p.setContaBancariaNome(rs.getString("conta_nome"));

                    lista.add(p);
                }
                return lista;
            }

            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            Conexao.fecharConexao(con);
        }
    }

    public List<Semana> GetMonthPayments() {
//        Calendar calStart = Calendar.getInstance();
//        calStart.set(2015, 03, 01);
//        java.util.Date start = calStart.getTime();
//
//        Calendar calEnd = Calendar.getInstance();
//        calStart.set(2015, 05, 01);
//        java.util.Date end = calEnd.getTime();

        con = Conexao.abrirConexao();
        List<contratos.Pagamento> pagamentos = listar();

        List<Semana> semanas = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.YEAR, start.getYear());
//        cal.set(Calendar.MONTH, start.getMonth());
//        cal.set(Calendar.DATE, start.getDate());

        cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
//        while (cal.get(Calendar.DAY_OF_WEEK) != 1) {
//            cal.add(Calendar.DATE, -1);
//        }

        int month = cal.get(Calendar.MONTH);
        int daysNumber = 0;

//        while (cal.getTime().compareTo(end) == 0) {
//            daysNumber++;
//            cal.add(Calendar.DATE, 1);
//        }
        if (cal.get(Calendar.DAY_OF_WEEK) == 4) {
            daysNumber = 35;
        } else {
            daysNumber = 42;
        }
        while (cal.get(Calendar.DAY_OF_WEEK) != 1) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        for (int i = 0; i < daysNumber / 7; i++) {
            Semana semana = new Semana();
            semana.numero = cal.get(Calendar.WEEK_OF_MONTH);

            for (int d = 0; d < 7; d++, cal.add(Calendar.DATE, 1)) {
                Dia dia = new Dia();
                dia.data = cal.getTime();
                for (contratos.Pagamento p : pagamentos) {
                    int total = p.getTotalParcelas();
                    for (contratos.ParcelaPagamento parcela : p.getParcelas()) {

                        if (getZeroTimeDate(parcela.getVencimento()).compareTo(getZeroTimeDate(cal.getTime())) == 0) {
                            // ATECAO! Adicionar parcela ao dia?

                            contratos.PagamentoView pView = new PagamentoView();
                            pView.setNome(p.getNome());
                            pView.setDescricao(parcela.getDescricao());
                            pView.setID(parcela.getID());
                            pView.setValor(parcela.getValor());
                            pView.setVencimento(parcela.getVencimento());
                            pView.setFornecedor(p.getFornecedor());
                            int numero = p.getNumero(parcela.getID());

                            pView.setNumero(numero);
                            pView.setTotal(total);

                            dia.pagamentos.add(pView);
                        }
                    }
                }

                semana.dias.add(dia);
            }

            semanas.add(semana);
        }

        Conexao.fecharConexao(con);
        return semanas;
    }

    private static java.util.Date getZeroTimeDate(java.util.Date date) {
        java.util.Date res = date;
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        res = calendar.getTime();

        return res;
    }
}
