package br.leg.alrr.cursos.model.legado;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Heliton
 */
@Entity
@Table(name = "pessoa_legada")
public class Pessoa implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String cpf;
    private String rg;
    private String celular;
    private String telefone;
   
    @Temporal(TemporalType.DATE)
    private Date data_nascimento;
    
    private String observacao;
    private String sexo;
    
    private String endereco_numero;
    private String endereco_rua;
    private String endereco_bairro;
    private String endereco_cidade;
    private String endereco_cep;
    private String endereco_complemento;

    public Pessoa() {
    }
    
    public Pessoa(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Date getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(Date data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEndereco_numero() {
        return endereco_numero;
    }

    public void setEndereco_numero(String endereco_numero) {
        this.endereco_numero = endereco_numero;
    }

    public String getEndereco_rua() {
        return endereco_rua;
    }

    public void setEndereco_rua(String endereco_rua) {
        this.endereco_rua = endereco_rua;
    }

    public String getEndereco_bairro() {
        return endereco_bairro;
    }

    public void setEndereco_bairro(String endereco_bairro) {
        this.endereco_bairro = endereco_bairro;
    }

    public String getEndereco_cidade() {
        return endereco_cidade;
    }

    public void setEndereco_cidade(String endereco_cidade) {
        this.endereco_cidade = endereco_cidade;
    }

    public String getEndereco_cep() {
        return endereco_cep;
    }

    public void setEndereco_cep(String endereco_cep) {
        this.endereco_cep = endereco_cep;
    }

    public String getEndereco_complemento() {
        return endereco_complemento;
    }

    public void setEndereco_complemento(String endereco_complemento) {
        this.endereco_complemento = endereco_complemento;
    }
    
    
    
}
