package net.wicstech.genericsearch.entidadestest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * The persistent class for the reg_regiao database table.
 * 
 */
@Entity
@Table(name = Tabelas.TABELA_REGIAO)
@Access(AccessType.PROPERTY)
public class Regiao implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String descricao;
	private String cidade;
	private List<Profissional> profissionais;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "REG_ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "REG_NM", nullable = false, length = 40)
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String nome) {
		this.descricao = nome;
	}

	@Column(name = "REG_NM_CIDADE", length = 40)
	public String getCidade() {
		return this.cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	/**
	 * Adicionar um profissional a esta região.
	 * 
	 * @param profissional
	 */
	void addProfissional(Profissional profissional) {
		if (getProfissionais() == null) {
			setProfissionais(new ArrayList<Profissional>());
		}
		getProfissionais().add(profissional);
	}

	/**
	 * @return the profissionais
	 */
	@ManyToMany(mappedBy = "regioes")
	public List<Profissional> getProfissionais() {
		return profissionais;
	}

	/**
	 * @param profissionais
	 *            the profissionais to set
	 */
	public void setProfissionais(List<Profissional> profissionais) {
		this.profissionais = profissionais;
	}

}