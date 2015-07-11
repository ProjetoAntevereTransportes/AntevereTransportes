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
    
    public boolean pagarDebito(contratos.Pagamento2 pg) {
        try {
            abrir();
            String sqlParcela = "insert into parcela_pagamento(vencimento, valor, "
                    + "descontos, juros, status_pagamento_id, descricao, debito_automatico_id, numero"
                    + ", data_pagamento, fornecedor_id, conta_bancaria_id) "
                    + "values(?,?,?,?,?,?,?,?,?,?,?)";
            
            PreparedStatement ps = con.prepareCall(sqlParcela);
            ps.setDate(1, new java.sql.Date(pg.getVencimento().getTime()));
            ps.setDouble(2, pg.getValor());
            ps.setDouble(3, pg.getDescontos());
            ps.setDouble(4, pg.getJuros());
            ps.setInt(5, 2);
            ps.setString(6, pg.getDescricao());
            ps.setInt(7, pg.getID());
            ps.setInt(8, pg.getNumero());
            ps.setDate(9, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
            ps.setInt(10, pg.getFornecedorID());
            ps.setInt(11, pg.getContaBancariaID());
            
            int i = ps.executeUpdate();
            
            if (i == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }
    
    public List<contratos.PagamentoFornecedor> pagamentoFornecedorMensal(int fornecedorID) {
        try {
            abrir();
            Statement st = con.createStatement();
            String sql = "select sum(parcela_pagamento.valor) + sum(parcela_pagamento.juros) - sum(parcela_pagamento.descontos) as total,\n"
                    + "fornecedor.nome,\n"
                    + "parcela_pagamento.data_pagamento\n"
                    + " from parcela_pagamento\n"
                    + " inner join conta_pagar on conta_pagar.id = parcela_pagamento.id\n"
                    + " inner join fornecedor on fornecedor.id = conta_pagar.fornecedor_id\n"
                    + " where parcela_pagamento.status_pagamento_id = 2\n"
                    + " and data_pagamento between DATE(CONCAT(YEAR(NOW()) - 1, '-', MONTH(NOW()), '-', '01')) and data_pagamento"
                    + " and conta_pagar.fornecedor_id = ?\n"
                    + "group by  YEAR(data_pagamento), MONTH(data_pagamento) ASC"
                    + " order by data_pagamento;";
            
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setInt(1, fornecedorID);
            
            ResultSet rs = ps.executeQuery();
            
            List<contratos.PagamentoFornecedor> pf = new ArrayList<>();
            
            while (rs.next()) {
                contratos.PagamentoFornecedor p = new PagamentoFornecedor();
                p.setMes(rs.getDate("data_pagamento"));
                p.setNomeFornecedor(rs.getString("nome"));
                p.setValor(rs.getDouble("total"));
                
                pf.add(p);
            }
            
            return pf;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            Conexao.fecharConexao(con);
        }
    }
    
    public boolean pagarUnico(contratos.Pagamento2 pg) {
        
        try {
            abrir();
            Statement st = con.createStatement();
            String sql = "update parcela_pagamento set descontos = ?, juros = ?, status_pagamento_id = 2, data_pagamento = NOW(), comprovante_id = ?, conta_bancaria_id = ? where id = ?;";
            
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setDouble(1, pg.getDescontos());
            ps.setDouble(2, pg.getJuros());
            ps.setInt(3, pg.getComprovanteID());
            ps.setInt(4, pg.getContaBancariaID());
            ps.setInt(5, pg.getID());
            
            int status = ps.executeUpdate();
            
            if (status == 1) {
                return true;
            } else {
                return false;
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            Conexao.fecharConexao(con);
        }
    }
    
    public boolean salvarDebito(contratos.DebitoAutomatico debito) {
        try {
            abrir();
            con.setAutoCommit(false);
            Statement st = con.createStatement();
            String sql = "INSERT INTO debito_automatico (conta_bancaria_id, data_inicio, data_fim, dia_mensal, "
                    + "nome, descricao, fornecedor_id, valor) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setInt(1, debito.getContaBancariaID());
            
            if (debito.getDataInicio() != null && debito.getDataFim() != null) {
                java.sql.Date sqlDataInicio = new java.sql.Date(debito.getDataInicio().getTime());
                java.sql.Date sqlDataFim = new java.sql.Date(debito.getDataFim().getTime());
                
                ps.setDate(2, sqlDataInicio);
                ps.setDate(3, sqlDataFim);
                ps.setInt(4, sqlDataFim.getDate());
            } else {
                ps.setNull(2, Types.NULL);
                ps.setNull(3, Types.NULL);
                
                if (debito.getDia() == 0) {
                    ps.setNull(4, Types.NULL);
                } else {
                    ps.setInt(4, debito.getDia());
                }
            }
            
            ps.setString(5, debito.getNome());
            ps.setString(6, debito.getDescricao());
            ps.setInt(7, debito.getFornecedorID());
            ps.setDouble(8, debito.getValor());
            
            int sucesso = ps.executeUpdate();
            
            if (sucesso != 1) {
                con.rollback();
                return false;
            } else {
                con.commit();
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                con.rollback();
                
            } catch (SQLException ex1) {
                Logger.getLogger(Pagamento.class
                        .getName()).log(Level.SEVERE, null, ex1);
            }
            return false;
        } finally {
            Conexao.fecharConexao(con);
        }
    }
    
    public List<contratos.DebitoAutomatico> listarDebitos() {
        try {
            Statement st = con.createStatement();
            String sql = "select * from debito_automatico;";
            PreparedStatement ps = con.prepareStatement(sql);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs != null) {
                List<contratos.DebitoAutomatico> lista = new ArrayList<>();
                while (rs.next()) {
                    contratos.DebitoAutomatico d = new DebitoAutomatico();
                    d.setContaBancariaID(rs.getInt("conta_bancaria_id"));
                    d.setDataFim(rs.getDate("data_fim"));
                    d.setDataInicio(rs.getDate("data_inicio"));
                    d.setDescricao(rs.getString("descricao"));
                    d.setDia(rs.getInt("dia_mensal"));
                    d.setFornecedorID(rs.getInt("fornecedor_id"));
                    d.setID(rs.getInt("id"));
                    d.setNome(rs.getString("nome"));
                    d.setValor(rs.getDouble("valor"));
                    //d.seteValido(rs.getBoolean("e_valido"));
                }
                
                return lista;
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            Conexao.fecharConexao(con);
        }
    }
    
    public boolean salvarVarios(contratos.Pagamento pagamento) {
        try {
            abrir();
            con.setAutoCommit(false);
            Statement st = con.createStatement();
            String sql = "insert into conta_pagar(nome, fornecedor_id, quantidade) values(?, ?, ?);";
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            
            ps.setString(1, pagamento.getNome());
            ps.setInt(2, pagamento.getFornecedorID());
            ps.setInt(3, pagamento.getTotalParcelas());
            
            int sucesso = ps.executeUpdate();
            
            if (sucesso != 1) {
                con.rollback();
                return false;
            }
            
            ResultSet keys = ps.getGeneratedKeys();
            keys.next();
            int contaPagarID = keys.getInt(1);
            
            String sqlParcela = "insert into parcela_pagamento(vencimento, valor, "
                    + "descontos, juros, status_pagamento_id, descricao, conta_pagar_id, numero,"
                    + "boleto_id) "
                    + "values(?,?,?,?,?,?,?,?,?)";
            
            int count = 1;
            
            pagamento.getParcelas().sort((contratos.ParcelaPagamento pg1,
                    contratos.ParcelaPagamento pg2) -> pg1.getVencimento().compareTo(pg2.getVencimento()));
            
            for (contratos.ParcelaPagamento p : pagamento.getParcelas()) {
                PreparedStatement par = con.prepareStatement(sqlParcela);
                
                par.setString(1, new java.text.SimpleDateFormat("yyyy-MM-dd").format(p.getVencimento()));
                par.setDouble(2, p.getValor());
                par.setDouble(3, 0);
                par.setDouble(4, 0);
                par.setInt(5, 1);
                par.setString(6, p.getDescricao());
                par.setInt(7, contaPagarID);
                par.setInt(8, count);
                
                if (p.getBoletoID() == 0) {
                    par.setNull(9, Types.INTEGER);
                } else {
                    par.setInt(9, p.getBoletoID());
                }
                
                int status = par.executeUpdate();
                if (status != 1) {
                    con.rollback();
                    return false;
                }
                
                count++;
            }
            con.commit();
            
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                con.rollback();
                
            } catch (SQLException ex1) {
                Logger.getLogger(Pagamento.class
                        .getName()).log(Level.SEVERE, null, ex1);
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
    
    public List<contratos.Pagamento2> listarTodosPagamentos(java.util.Date date) {
        
        //String e = new library.orc().getText(null);
        try {            
            abrir();
            Statement st = con.createStatement();
            
            Calendar c = Calendar.getInstance();
            c.set(date.getYear(), date.getMonth(), date.getDate());
            
            String sql = "select parcela_pagamento.id,\n"
                    + "	conta_pagar.nome,\n"
                    + "       parcela_pagamento.vencimento,\n"
                    + "       parcela_pagamento.conta_bancaria_id,\n"
                    + "       parcela_pagamento.debito_automatico_id,\n"
                    + "	   parcela_pagamento.status_pagamento_id,\n"
                    + "	   parcela_pagamento.valor,\n"
                    + "       parcela_pagamento.descontos,\n"
                    + "       parcela_pagamento.juros,\n"
                    + "       parcela_pagamento.data_pagamento,\n"
                    + "       parcela_pagamento.comprovante_id,\n"
                    + "       parcela_pagamento.boleto_id,\n"
                    + "       parcela_pagamento.descricao,\n"
                    + "       status_pagamento.nome as 'status_nome',\n"
                    + "       conta_bancaria.nome as 'conta_nome',\n"
                    + "       conta_pagar.fornecedor_id,\n"
                    + "       fornecedor.nome as 'fornecedor_nome',"
                    + "conta_pagar.quantidade as 'quantidade',\n"
                    + "                           parcela_pagamento.numero as 'numero',\n"
                    + "       false as 'debito_limitado',\n"
                    + "                           false as 'debito_ilimitado',\n"
                    + "                           if((SELECT count(*) from parcela_pagamento where conta_pagar_id = conta_pagar.id) > 1, true, false) as 'carne',\n"
                    + "                           if((SELECT count(*) from parcela_pagamento where conta_pagar_id = conta_pagar.id) = 1, true, false) as 'unico'"
                    + "	   from parcela_pagamento \n"
                    + "       left join conta_bancaria on conta_bancaria.id = parcela_pagamento.conta_bancaria_id\n"
                    + "       left join status_pagamento on status_pagamento.id = parcela_pagamento.status_pagamento_id \n"
                    + "       left join conta_pagar on conta_pagar.id = parcela_pagamento.conta_pagar_id\n"
                    + "       left join fornecedor on fornecedor.id = conta_pagar.fornecedor_id\n"
                    + "       where MONTH(parcela_pagamento.vencimento) = ? &&\n"
                    + "       YEAR(parcela_pagamento.vencimento) = ?\n"
                    + "        union\n"
                    + "select debito_automatico.id as 'id',\n"
                    + "       debito_automatico.nome as 'nome',\n"
                    + "       DATE(CONCAT(?, '-', ?, '-', debito_automatico.dia_mensal)) as 'vencimento',\n"
                    + "       debito_automatico.conta_bancaria_id as 'conta_bancaria_id',\n"
                    + "       debito_automatico.id as 'debito_automatico_id',\n"
                    + "	   0 as 'status_pagamento_id',\n"
                    + "	   debito_automatico.valor as 'valor',\n"
                    + "       0 as 'descontos',\n"
                    + "       0 as 'juros',\n"
                    + "       null as 'data_pagamento',\n"
                    + "       null as 'comprovante_id',\n"
                    + "       null as 'boleto_id',\n"
                    + "       debito_automatico.descricao as 'descricao',\n"
                    + "       conta_bancaria.nome as 'conta_nome',\n"
                    + "       debito_automatico.fornecedor_id as 'fornecedor_id',\n"
                    + "       null as 'status_nome',\n"
                    + "       fornecedor.nome as 'fornecedor_nome',"
                    + "IF(debito_automatico.data_inicio is null, \n"
                    + "(SELECT count(*) from parcela_pagamento where debito_automatico_id = debito_automatico.id),\n"
                    + "TIMESTAMPDIFF(MONTH, debito_automatico.data_inicio, debito_automatico.data_fim) + 1) as 'quantidade',\n"
                    + "TIMESTAMPDIFF(MONTH, debito_automatico.data_inicio, DATE(CONCAT(?,'-',?,'-', debito_automatico.dia_mensal))) + 1\n"
                    + "as 'numero',\n"
                    + " IF(debito_automatico.data_inicio is null, false, true) as 'debito_limitado',\n"
                    + "                            IF(debito_automatico.data_inicio is null, true, false) as 'debito_ilimitado',\n"
                    + "                           false as 'carne',\n"
                    + "                           false as 'unico'"
                    + "       from debito_automatico \n"
                    + "       inner join fornecedor on fornecedor.id = debito_automatico.fornecedor_id\n"
                    + "       inner join conta_bancaria on conta_bancaria.id = debito_automatico.conta_bancaria_id\n"
                    + " left join parcela_pagamento on  (parcela_pagamento.debito_automatico_id = debito_automatico.id)"
                    //                    + "       where (dia_mensal between ? and ?) or\n"
                    + " where ((DATE(CONCAT(?,'-',?,'-',debito_automatico.dia_mensal))"
                    + "between DATE(debito_automatico.data_inicio) and DATE(debito_automatico.data_fim)))\n"
                    + "                     or ((debito_automatico.data_fim = '0000-00-00' or debito_automatico.data_fim is null or debito_automatico.data_fim = '') and\n"
                    + "					  (debito_automatico.data_inicio = '0000-00-00' or debito_automatico.data_inicio is null or debito_automatico.data_inicio = ''))"
                    + " order by vencimento;";
            
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setString(1, new SimpleDateFormat("MM").format(c.getTime()));
            ps.setString(2, String.valueOf(c.get(Calendar.YEAR) + 1900));
            ps.setString(3, String.valueOf(c.get(Calendar.YEAR) + 1900));
            ps.setString(4, new SimpleDateFormat("MM").format(c.getTime()));
            
            c.set(Calendar.DATE, c.getMaximum(Calendar.DATE));
            java.util.Date ultimaData = c.getTime();
            
            c.set(Calendar.DATE, c.getMinimum(Calendar.DATE));
            java.util.Date primeiraData = c.getTime();

            //ps.setInt(5, primeiraData.getDate());
            //ps.setInt(6, ultimaData.getDate());
            ps.setString(5, String.valueOf(date.getYear() + 1900));
            ps.setString(6, new SimpleDateFormat("MM").format(date));
            
            ps.setString(7, String.valueOf(date.getYear() + 1900));
            ps.setString(8, new SimpleDateFormat("MM").format(date));
            
            System.out.print(ps);
            
            ResultSet rs = ps.executeQuery();
            
            List<contratos.Pagamento2> list = new ArrayList<>();
            while (rs.next()) {
                contratos.Pagamento2 pg = new Pagamento2();
                pg.setID(rs.getInt("id"));
                pg.setNome(rs.getString("nome"));
                pg.setValor(rs.getDouble("valor"));
                pg.setDescricao(rs.getString("descricao"));
                pg.setVencimento(rs.getDate("vencimento"));
                pg.setComprovanteID(rs.getInt("comprovante_id"));
                pg.setBoletoID(rs.getInt("boleto_id"));
                pg.setContaBancariaID(rs.getInt("conta_bancaria_id"));
                pg.setContaNome(rs.getString("conta_nome"));
                pg.setDataPagamento(rs.getDate("data_pagamento"));
                pg.setDebitoLimitado(rs.getBoolean("debito_limitado"));
                pg.setDebitoIlimitado(rs.getBoolean("debito_ilimitado"));
                pg.setUnico(rs.getBoolean("unico"));
                pg.setCarne(rs.getBoolean("carne"));
                pg.setDescontos(rs.getDouble("descontos"));
                pg.setFornecedorID(rs.getInt("fornecedor_id"));
                pg.setFornecedorNome(rs.getString("fornecedor_nome"));
                pg.setJuros(rs.getDouble("juros"));
                pg.setStatusNome(rs.getString("status_nome"));
                pg.setStatusPagamentoID(rs.getInt("status_pagamento_id"));
                pg.setQuantidade(rs.getInt("quantidade"));
                pg.setNumero(rs.getInt("numero"));
                
                pg.setPago(pg.getStatusPagamentoID() == 2);
                
                pg.setVencido(pg.getVencimento().compareTo(new java.util.Date()) < 0);
                
                list.add(pg);
            }
            
            return list;
            
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
    
    public List<contratos.PagamentoView> listarPagamentosView() {
        List<contratos.PagamentoView> lista = new ArrayList<PagamentoView>();
        
        List<contratos.Pagamento> boletos = listar();
        List<contratos.DebitoAutomatico> debitos = listarDebitos();
        
        for (contratos.DebitoAutomatico d : debitos) {
            if (d.geteValido() && d.getDataFim() == null && d.getDataInicio() == null) {
                contratos.Pagamento p = new contratos.Pagamento();
                p.setNome(d.getNome());
                p.setFornecedorID(d.getFornecedorID());
                //p.setFornecedor(null);

                contratos.ParcelaPagamento par = new ParcelaPagamento();
                par.setContaBancariaID(d.getContaBancariaID());
                //par.set

                p.setParcelas(null);
            }
        }
        
        return lista;
    }
    
    public List<Semana> GetMonthPayments(java.util.Date data) {

//        Calendar calStart = Calendar.getInstance();
//        calStart.set(2015, 03, 01);
//        java.util.Date start = calStart.getTime();
//
//        Calendar calEnd = Calendar.getInstance();
//        calStart.set(2015, 05, 01);
//        java.util.Date end = calEnd.getTime();
        con = Conexao.abrirConexao();
        List<contratos.Pagamento2> pagamentos = listarTodosPagamentos(data);
        
        List<Semana> semanas = new ArrayList<>();
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DAY_OF_MONTH));
//        cal.set(Calendar.YEAR, start.getYear());
//        cal.set(Calendar.MONTH, start.getMonth());
//        cal.set(Calendar.DATE, start.getDate());

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
                //System.out.print("\n" + cal.get(Calendar.DATE));
                for (contratos.Pagamento2 p : pagamentos) {
                    if (getZeroTimeDate(p.getVencimento()).compareTo(getZeroTimeDate(cal.getTime())) == 0) {
                        //System.out.print("\n" + p.getVencimento().getDate());
                        dia.pagamentos.add(p);
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
