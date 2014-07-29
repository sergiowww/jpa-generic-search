package net.wicstech.genericsearch.dto;

import java.io.Serializable;
import java.util.Date;

import net.wicstech.genericsearch.FilterParameter;
import net.wicstech.genericsearch.FilterType;
import net.wicstech.genericsearch.SelectFields;
import net.wicstech.genericsearch.entidadestest.Disciplina;
import net.wicstech.genericsearch.entidadestest.Regiao;
import net.wicstech.genericsearch.entidadestest.SerieAnoCurso;

/**
 * Objeto com valores de pesquisa do profissional.
 * 
 * @author Sergio
 * 
 */
@SelectFields({"id", "email", "nome", "telefone", "celular", "dataHoraCadastro", "efetivo"})
public class PesquisaProfissionalDTO implements Serializable {
	private static final long serialVersionUID = 7132919565327356041L;

	/**
	 * E-mail.
	 */
	@FilterParameter(value = FilterType.ILIKE)
	private String email;

	/**
	 * Telefones cadastrados.
	 */
	@FilterParameter(entityProperty = {"telefone", "celular"}, value = FilterType.ILIKE)
	private String telefones;

	/**
	 * Filtrar por regiões.
	 */
	@FilterParameter(entityProperty = "regioes", value = FilterType.EQUALS)
	private Regiao regiao;

	@FilterParameter(entityProperty = "disciplinas", value = FilterType.EQUALS)
	private Disciplina disciplina;

	@FilterParameter(entityProperty = "cursos", value = FilterType.EQUALS)
	private SerieAnoCurso curso;

	@FilterParameter(FilterType.EQUALS)
	private Boolean efetivo;

	/**
	 * Filtrar pelo dia da semana disponível.
	 */
	@FilterParameter(entityProperty = "disponibilidades.id.diaSemana", value = FilterType.EQUALS)
	private Integer diaSemana;

	/**
	 * Filtrar para o início a partir do que foi informado.
	 */
	@FilterParameter(entityProperty = "disponibilidades.id.inicio", value = FilterType.LESS_THAN_OR_EQUAL, distinct = true)
	private Date inicio;

	/**
	 * Filtrar pelo término da disponibilidade.
	 */
	@FilterParameter(entityProperty = "disponibilidades.fim", value = FilterType.GREATER_THAN_OR_EQUAL, distinct = true)
	private Date fim;

	@FilterParameter(value = FilterType.ILIKE)
	private String nome;

	/**
	 * @return the email
	 */
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
	 * @return the telefones
	 */
	public String getTelefones() {
		return telefones;
	}

	/**
	 * @param telefones
	 *            the telefones to set
	 */
	public void setTelefones(String telefones) {
		this.telefones = telefones;
	}

	/**
	 * @return the regiao
	 */
	public Regiao getRegiao() {
		return regiao;
	}

	/**
	 * @param regiao
	 *            the regiao to set
	 */
	public void setRegiao(Regiao regiao) {
		this.regiao = regiao;
	}

	/**
	 * @return the disciplina
	 */
	public Disciplina getDisciplina() {
		return disciplina;
	}

	/**
	 * @param disciplina
	 *            the disciplina to set
	 */
	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	/**
	 * @return the curso
	 */
	public SerieAnoCurso getCurso() {
		return curso;
	}

	/**
	 * @param curso
	 *            the curso to set
	 */
	public void setCurso(SerieAnoCurso curso) {
		this.curso = curso;
	}

	/**
	 * @return the diaSemana
	 */
	public Integer getDiaSemana() {
		return diaSemana;
	}

	/**
	 * @param diaSemana
	 *            the diaSemana to set
	 */
	public void setDiaSemana(Integer diaSemana) {
		this.diaSemana = diaSemana;
	}

	/**
	 * @return the inicio
	 */
	public Date getInicio() {
		return inicio;
	}

	/**
	 * @param inicio
	 *            the inicio to set
	 */
	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	/**
	 * @return the fim
	 */
	public Date getFim() {
		return fim;
	}

	/**
	 * @param fim
	 *            the fim to set
	 */
	public void setFim(Date fim) {
		this.fim = fim;
	}

	/**
	 * @return the efetivo
	 */
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
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome
	 *            the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
}
