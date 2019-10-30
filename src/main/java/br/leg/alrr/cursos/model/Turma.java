package br.leg.alrr.cursos.model;

import br.leg.alrr.cursos.util.BaseEntity;
import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Heliton
 */
@Audited
@AuditTable(value="turma_auditoria", schema = "escolegis_academico")
@Table(schema = "escolegis_academico")
@Entity
public class Turma implements Serializable, BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private boolean status;
    private boolean iniciada;

    @ManyToOne(fetch = FetchType.EAGER)
    private Horario horario;

    private int cargaHoraria;
    
    private String professor;
    
    @ManyToOne
    private Disciplina disciplina;

    @ManyToOne
    private Modulo modulo;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Aluno> alunos;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataInicio;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataTermino;

    private String diasDaSemana;
    
    private int quantidadeMaximaDeFaltasJustificadas;
//==============================================================================

    public Turma() {
    }

    public Turma(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(Date dataTermino) {
        this.dataTermino = dataTermino;
    }

    public String getDiasDaSemana() {
        return diasDaSemana;
    }

    public void setDiasDaSemana(String diasDaSemana) {
        this.diasDaSemana = diasDaSemana;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public String getNomeExibicao() {
        StringBuilder sb = new StringBuilder();
        sb.append(nome);
        sb.append(" | ");

        // TRANSFORMANDO A STRING EM UM ARRAY PARA PEGAR CADA DIA DA SEMANA SEPARADAMENTE
        String[] s = diasDaSemana.split(",");
        for (int i = 0; i < s.length; i++) {
            sb.append(s[i]);
            
            if ((i + 1) >= s.length) {
            } else {
                sb.append(", ");
            }
        }
        //--------------------------------------------------------------------------------
        
        sb.append(" | ");
        sb.append(horario.getDescricao());
        return sb.toString();
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public boolean isIniciada() {
        return iniciada;
    }

    public void setIniciada(boolean iniciada) {
        this.iniciada = iniciada;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }
    
    
    public boolean getAlcancouTermoFinal(){
        GregorianCalendar g1 = new GregorianCalendar();
        g1.setTime(dataTermino);
        GregorianCalendar g2 = new GregorianCalendar();
        g2.set(GregorianCalendar.HOUR, 0);
        g2.set(GregorianCalendar.MINUTE, 0);
        g2.set(GregorianCalendar.SECOND, 0);
        return g1.before(g2);
    }

    public int getQuantidadeMaximaDeFaltasJustificadas() {
        return quantidadeMaximaDeFaltasJustificadas;
    }

    public void setQuantidadeMaximaDeFaltasJustificadas(int quantidadeMaximaDeFaltasJustificadas) {
        this.quantidadeMaximaDeFaltasJustificadas = quantidadeMaximaDeFaltasJustificadas;
    }
    
}
