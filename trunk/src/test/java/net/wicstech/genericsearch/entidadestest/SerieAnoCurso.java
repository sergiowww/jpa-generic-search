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
 * The persistent class for the sea_serie_ano_curso database table.
 * 
 */
@Entity
@Table(name = Tabelas.TABELA_SERIEANOCURSO)
@Access(AccessType.PROPERTY)
public class SerieAnoCurso implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String descricao;
	private List<Profissional> profissionais;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SEA_ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "SEA_DS", nullable = false, length = 30)
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * Adicionar um profissional a este curso.
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
	@ManyToMany(mappedBy = "cursos")
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