package net.wicstech.genericsearch.entidadestest;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the pro_profissional database table.
 * 
 */
@Entity
@Table(name = Tabelas.TABELA_PROFISSIONAL)
@Access(AccessType.PROPERTY)
public class Profissional implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String celular;
	private String telefone;
	private String observacoes;
	private Date dataNascimento;
	private String nome;
	private String celularOperadora;
	private String formacaoAcademica;
	private String registro;
	private String telefoneOperadora;
	private BigDecimal valorHoraAula;
	private Boolean efetivo;
	private String email;
	private String senha;
	private List<Disciplina> disciplinas;
	private List<SerieAnoCurso> cursos;
	private List<Regiao> regioes;
	private Date dataHoraUltimaAtualizacao;
	private Date dataHoraCadastro;
	private String nomeArquivoCurriculo;
	private String nomeArquivoFoto;
	private List<Disponibilidade> disponibilidades;

	/**
	 * @param id
	 * @param nome
	 */
	public Profissional(Integer id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}

	/**
	 * Construtor.
	 */
	public Profissional() {
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PRO_ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "PRO_CD_CELULAR", length = 11)
	public String getCelular() {
		return this.celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	@Column(name = "PRO_CD_TELEFONE", length = 11)
	public String getTelefone() {
		return this.telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	@Column(name = "PRO_DS_OBSERVACOES")
	public String getObservacoes() {
		return this.observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PRO_DT_NASCIMENTO", nullable = false)
	public Date getDataNascimento() {
		return this.dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	@Column(name = "PRO_NM", nullable = false, length = 80)
	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "PRO_NM_CELULAR_OPER", length = 10)
	public String getCelularOperadora() {
		return this.celularOperadora;
	}

	public void setCelularOperadora(String celularOperadora) {
		this.celularOperadora = celularOperadora;
	}

	@Column(name = "PRO_NM_FORMACAO_ACADEMICA", length = 70)
	public String getFormacaoAcademica() {
		return this.formacaoAcademica;
	}

	public void setFormacaoAcademica(String formacaoAcademica) {
		this.formacaoAcademica = formacaoAcademica;
	}

	@Column(name = "PRO_NM_REGISTRO", length = 40)
	public String getRegistro() {
		return this.registro;
	}

	public void setRegistro(String registro) {
		this.registro = registro;
	}

	@Column(name = "PRO_NM_TELEFONE_OPER", length = 10)
	public String getTelefoneOperadora() {
		return this.telefoneOperadora;
	}

	public void setTelefoneOperadora(String telefoneOperadora) {
		this.telefoneOperadora = telefoneOperadora;
	}

	@Column(name = "PRO_VL_HORA_AULA")
	public BigDecimal getValorHoraAula() {
		return this.valorHoraAula;
	}

	public void setValorHoraAula(BigDecimal valorHoraAula) {
		this.valorHoraAula = valorHoraAula;
	}

	/**
	 * @return the disciplinas
	 */
	@ManyToMany
	@JoinTable(name = "PIS_PRO_DIS", joinColumns = @JoinColumn(name = "PRO_ID", nullable = false), inverseJoinColumns = @JoinColumn(name = "DIS_ID", nullable = false))
	public List<Disciplina> getDisciplinas() {
		return disciplinas;
	}

	/**
	 * @param disciplinas
	 *            the disciplinas to set
	 */
	public void setDisciplinas(List<Disciplina> disciplinas) {
		this.disciplinas = disciplinas;
	}

	/**
	 * @return the efetivo
	 */
	@Column(name = "PRO_IB_EFETIVO", nullable = false)
	public Boolean getEfetivo() {
		return efetivo;
	}

	/**
	 * @param efetivo
	 *            the efetivo to set
	 */
	public void setEfetivo(Boolean efetivo) {
		this.efetivo = efetivo;
	}

	/**
	 * @return the email
	 */
	@Column(name = "PRO_NM_EMAIL", length = 80)
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the cursos
	 */
	// CHECKSTYLE:OFF
	@ManyToMany
	@JoinTable(joinColumns = @JoinColumn(name = "PRO_ID", nullable = false), name = "PEA_PRO_SEA", inverseJoinColumns = @JoinColumn(
			name = "SEA_ID",
			referencedColumnName = "SEA_ID",
			nullable = false))
	public List<SerieAnoCurso> getCursos() {
		// CHECKSTYLE:ON
		return cursos;
	}

	/**
	 * @param cursos
	 *            the cursos to set
	 */
	public void setCursos(List<SerieAnoCurso> cursos) {
		this.cursos = cursos;
	}

	/**
	 * @return the dataHoraUltimaAtualizacao
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, name = "PRO_DH_ATUALIZACAO")
	public Date getDataHoraUltimaAtualizacao() {
		return dataHoraUltimaAtualizacao;
	}

	/**
	 * @param dataHoraUltimaAtualizacao
	 *            the dataHoraUltimaAtualizacao to set
	 */
	public void setDataHoraUltimaAtualizacao(Date dataHoraUltimaAtualizacao) {
		this.dataHoraUltimaAtualizacao = dataHoraUltimaAtualizacao;
	}

	/**
	 * @return the dataHoraCadastro
	 */
	@Column(name = "PRO_DH_CADASTRO", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDataHoraCadastro() {
		return dataHoraCadastro;
	}

	/**
	 * @param dataHoraCadastro
	 *            the dataHoraCadastro to set
	 */
	public void setDataHoraCadastro(Date dataHoraCadastro) {
		this.dataHoraCadastro = dataHoraCadastro;
	}

	/**
	 * Limpar a coleção de cursos.
	 */
	public void removeAllCursos() {
		if (getCursos() != null) {
			getCursos().clear();
		}
	}

	/**
	 * Limpar a coleção de disciplinas.
	 */
	public void removeAllDisciplinas() {
		if (getDisciplinas() != null) {
			getDisciplinas().clear();
		}
	}

	/**
	 * Remover todas as associações.
	 */
	public void removeAllRegioes() {
		if (getRegioes() != null) {
			getRegioes().clear();
		}

	}

	/**
	 * Adicionar uma disciplina.
	 * 
	 * @param disciplina
	 */
	public void addDisciplina(Disciplina disciplina) {
		if (disciplinas == null) {
			disciplinas = new ArrayList<Disciplina>();
		}
		disciplinas.add(disciplina);
		disciplina.addProfissional(this);
	}

	/**
	 * Adicionar um curso ministrado a este profissional.
	 * 
	 * @param curso
	 */
	public void addCurso(SerieAnoCurso curso) {
		if (getCursos() == null) {
			setCursos(new ArrayList<SerieAnoCurso>());
		}
		getCursos().add(curso);
		curso.addProfissional(this);
	}

	/**
	 * Adicionar uma região de atuação a este profissional.
	 * 
	 * @param regiao
	 */
	public void addRegiao(Regiao regiao) {
		if (getRegioes() == null) {
			setRegioes(new ArrayList<Regiao>());
		}
		getRegioes().add(regiao);
		regiao.addProfissional(this);
	}

	/**
	 * @return the nomeArquivoCurriculo
	 */
	@Column(name = "PRO_NM_ARQUIVO_CURRICULO", length = 30)
	public String getNomeArquivoCurriculo() {
		return nomeArquivoCurriculo;
	}

	/**
	 * @param nomeArquivoCurriculo
	 *            the nomeArquivoCurriculo to set
	 */
	public void setNomeArquivoCurriculo(String nomeArquivoCurriculo) {
		this.nomeArquivoCurriculo = nomeArquivoCurriculo;
	}

	/**
	 * @return the nomeArquivoFoto
	 */
	@Column(name = "PRO_NM_ARQUIVO_FOTO", length = 30)
	public String getNomeArquivoFoto() {
		return nomeArquivoFoto;
	}

	/**
	 * @param nomeArquivoFoto
	 *            the nomeArquivoFoto to set
	 */
	public void setNomeArquivoFoto(String nomeArquivoFoto) {
		this.nomeArquivoFoto = nomeArquivoFoto;
	}

	/**
	 * Remover a associação completa.
	 */
	public void removeAssociacaoAllCursos() {
		for (SerieAnoCurso curso : getCursos()) {
			curso.getProfissionais().remove(this);
		}
		removeAllCursos();
	}

	/**
	 * Remover as associações quando excluído.
	 */
	public void removeAssociacaoAllRegioes() {
		for (Regiao regiao : getRegioes()) {
			regiao.getProfissionais().remove(this);
		}
		removeAllRegioes();
	}

	/**
	 * Remover a associação completa.
	 */
	public void removeAssociacaoAllDisciplinas() {
		for (Disciplina disciplina : getDisciplinas()) {
			disciplina.getProfissionais().remove(this);
		}
		removeAllDisciplinas();
	}

	/**
	 * @return the regioes
	 */
	@ManyToMany
	@JoinTable(name = "PRG_PRO_REG", joinColumns = @JoinColumn(name = "PRO_ID", nullable = false), inverseJoinColumns = @JoinColumn(name = "REG_ID", nullable = false))
	public List<Regiao> getRegioes() {
		return regioes;
	}

	/**
	 * @param regioes
	 *            the regioes to set
	 */
	public void setRegioes(List<Regiao> regioes) {
		this.regioes = regioes;
	}

	// bi-directional many-to-one association to Disponibilidade
	@OneToMany(mappedBy = "id.profissional")
	public List<Disponibilidade> getDisponibilidades() {
		return this.disponibilidades;
	}

	public void setDisponibilidades(List<Disponibilidade> disponibilidades) {
		this.disponibilidades = disponibilidades;
	}

	/**
	 * @return the senha
	 */
	@Column(name = "PRO_SS", length = 8)
	public String getSenha() {
		return senha;
	}

	/**
	 * @param senha
	 *            the senha to set
	 */
	public void setSenha(String senha) {
		this.senha = senha;
	}
}