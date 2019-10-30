package br.leg.alrr.cursos.model;

import br.leg.alrr.cursos.business.Sexo;
import br.leg.alrr.cursos.util.StringUtils;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Heliton
 */
@Audited
@AuditTable(value="aluno_auditoria", schema = "escolegis_academico")
@Table(schema = "escolegis_academico")
@Entity
public class Aluno implements Serializable, Comparable<Aluno> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    
    @Column(unique = true)
    private String cpf;
    
    private String rg;

    private String celular;

    private String fixo;
    
    private String email;
    
    @Enumerated(EnumType.STRING)
    private Sexo sexo;
    
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;
    
    private String nomeDoResponsavel;

    private String grauParenteso;
    
    private String telefoneDoResponsavel;
    
    private boolean possuiFilho;

    private String informacoesComplementares;
    
    @ManyToOne
    private Pais paisDeOrigem;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Endereco endereco;

    @ManyToOne
    private Unidade unidade;
    
    @Transient
    private Curso curso;
    
    @Transient
    private Turma turma;
    
    @Transient
    private Diretor diretor;
    
    @Transient
    private Long quantidadeDeAulas;
    
    @Transient
    private Long quantidadeDePresencas;
    //=========================================================================//

    public Aluno() {
    }

    public Aluno(Long id) {
        this.id = id;
    }
    
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNacimento) {
        this.dataNascimento = dataNacimento;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFixo() {
        return fixo;
    }

    public void setFixo(String fixo) {
        this.fixo = fixo;
    }

    public String getPrimeiroNome() {
        return StringUtils.retornarPrimeiroNome(nome);
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNomeDoResponsavel() {
        return nomeDoResponsavel;
    }

    public void setNomeDoResponsavel(String nomeDoResponsavel) {
        this.nomeDoResponsavel = nomeDoResponsavel;
    }

    public String getGrauParenteso() {
        return grauParenteso;
    }

    public void setGrauParenteso(String grauParenteso) {
        this.grauParenteso = grauParenteso;
    }

    public String getTelefoneDoResponsavel() {
        return telefoneDoResponsavel;
    }

    public void setTelefoneDoResponsavel(String telefoneDoResponsavel) {
        this.telefoneDoResponsavel = telefoneDoResponsavel;
    }

    public String getInformacoesComplementares() {
        return informacoesComplementares;
    }

    public void setInformacoesComplementares(String informacoesComplementares) {
        this.informacoesComplementares = informacoesComplementares;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public Long getQuantidadeDeAulas() {
        return quantidadeDeAulas;
    }

    public void setQuantidadeDeAulas(Long quantidadeDeAulas) {
        this.quantidadeDeAulas = quantidadeDeAulas;
    }

    public Long getQuantidadeDePresencas() {
        return quantidadeDePresencas;
    }

    public void setQuantidadeDePresencas(Long quantidadeDePresencas) {
        this.quantidadeDePresencas = quantidadeDePresencas;
    }

    public boolean isPossuiFilho() {
        return possuiFilho;
    }

    public void setPossuiFilho(boolean possuiFilho) {
        this.possuiFilho = possuiFilho;
    }

    public Diretor getDiretor() {
        return diretor;
    }

    public void setDiretor(Diretor diretor) {
        this.diretor = diretor;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Pais getPaisDeOrigem() {
        return paisDeOrigem;
    }

    public void setPaisDeOrigem(Pais paisDeOrigem) {
        this.paisDeOrigem = paisDeOrigem;
    }

    @Override
    public int compareTo(Aluno o) {
        if (this.nome.compareTo(o.getNome()) > 0) {
            return 1;
        } else  {
            return -1;
        }
    }
    
}
