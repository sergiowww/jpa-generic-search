package net.wicstech.genericsearch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.NonUniqueResultException;

import net.wicstech.genericsearch.dto.PesquisaProfissionalDTO;
import net.wicstech.genericsearch.entidadestest.BolsaCorretoraCorretor;
import net.wicstech.genericsearch.entidadestest.Corretor;
import net.wicstech.genericsearch.entidadestest.Corretora;
import net.wicstech.genericsearch.entidadestest.Disciplina;
import net.wicstech.genericsearch.entidadestest.Disponibilidade;
import net.wicstech.genericsearch.entidadestest.EntidadeTipo;
import net.wicstech.genericsearch.entidadestest.Profissional;
import net.wicstech.genericsearch.entidadestest.Regiao;
import net.wicstech.genericsearch.entidadestest.SerieAnoCurso;
import net.wicstech.genericsearch.entidadestest.SituacaoCorretorCorretora;
import net.wicstech.genericsearch.entidadestest.TipoDocumento;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

public class GenericSearchDAOTest extends AbstractJUnitTest {
	private static final String CPF_TESTE1 = "123456";
	private static final String CPF_TESTE2 = "4548756";
	private static final String CPF_TESTE3 = "528155";
	private static final String SITUACAO_ATIVO = "Descrição Ativo";
	private static final String SITUACAO_INATIVO = "Descrição inativo";

	@Test
	public void testCollections() throws Exception {
		limparTabelas();
		final SerieAnoCurso curso1 = new SerieAnoCurso();
		curso1.setDescricao("Curso 1");

		final SerieAnoCurso curso2 = new SerieAnoCurso();
		curso2.setDescricao("Curso 2");

		final Regiao regiao1 = new Regiao();
		regiao1.setCidade("Brasília");
		regiao1.setDescricao("DF");

		final Regiao regiao2 = new Regiao();
		regiao2.setCidade("Taguatinga");
		regiao2.setDescricao("DF");

		final Disciplina disciplina1 = new Disciplina();
		disciplina1.setDescricao("Disciplina 1");

		final Disciplina disciplina2 = new Disciplina();
		disciplina2.setDescricao("Disciplina 2");

		this.dao.salvar(curso1, curso2, regiao1, regiao2, disciplina1, disciplina2);

		gravarProfissional(curso1, regiao1, disciplina1, "96013799", "334634522", "sergiowww@gmail.com", true, "Sergio", "18:00", "20:00", Calendar.MONDAY);

		gravarProfissional(curso2, regiao2, disciplina2, "99474061", "334634223", "sergiowww@email.it", true, "João", "14:00", "15:00", Calendar.SATURDAY);

		// dto.setCurso(curso);
		// dto.setDiaSemana(diaSemana);
		// dto.setDisciplina(disciplina);
		// dto.setEfetivo(efetivo);
		// dto.setEmail(email);
		// dto.setFim(fim);
		// dto.setInicio("18:00");
		// dto.setNome(nome);
		// dto.setRegiao(regiao1);
		// dto.setTelefones(telefones);

		PesquisaProfissionalDTO dto = new PesquisaProfissionalDTO();
		dto.setInicio(getTime("21:00"));
		dto.setFim(getTime("22:00"));
		List<Profissional> resultado = this.genericSearchDAO.list(Profissional.class, dto, new PagedSearchNavigation(0, 0));
		assertTrue(resultado.isEmpty());
		assertEquals(0, this.genericSearchDAO.size(Profissional.class, dto));

		dto = new PesquisaProfissionalDTO();
		dto.setInicio(getTime("19:00"));
		dto.setFim(getTime("20:00"));
		resultado = this.genericSearchDAO.list(Profissional.class, dto, new PagedSearchNavigation(0, 0));
		assertEquals(1, resultado.size());
		assertEquals(1, this.genericSearchDAO.size(Profissional.class, dto));
		assertEquals("Sergio", resultado.get(0).getNome());

		dto = new PesquisaProfissionalDTO();
		resultado = this.genericSearchDAO.list(Profissional.class, dto, new PagedSearchNavigation(0, 0));
		assertEquals(2, resultado.size());

		dto = new PesquisaProfissionalDTO();
		dto.setRegiao(regiao2);
		resultado = this.genericSearchDAO.list(Profissional.class, dto, new PagedSearchNavigation(0, 0));
		assertEquals(1, resultado.size());
		assertEquals("João", resultado.get(0).getNome());

		dto = new PesquisaProfissionalDTO();
		dto.setCurso(curso1);
		resultado = this.genericSearchDAO.list(Profissional.class, dto, new PagedSearchNavigation(0, 0));
		assertEquals(1, resultado.size());
		assertEquals("Sergio", resultado.get(0).getNome());

		dto = new PesquisaProfissionalDTO();
		dto.setDisciplina(disciplina2);
		resultado = this.genericSearchDAO.list(Profissional.class, dto, new PagedSearchNavigation(0, 0));
		assertEquals(1, resultado.size());
		assertEquals("João", resultado.get(0).getNome());

		dto = new PesquisaProfissionalDTO();
		dto.setDisciplina(disciplina1);
		resultado = this.genericSearchDAO.list(Profissional.class, dto, new PagedSearchNavigation(0, 0));
		assertEquals(1, resultado.size());
		assertEquals("Sergio", resultado.get(0).getNome());

		dto = new PesquisaProfissionalDTO();
		dto.setDiaSemana(Calendar.SATURDAY);
		resultado = this.genericSearchDAO.list(Profissional.class, dto, new PagedSearchNavigation(0, 0));
		assertEquals(1, resultado.size());
		assertEquals("João", resultado.get(0).getNome());

		dto = new PesquisaProfissionalDTO();
		dto.setTelefones("334634522");
		resultado = this.genericSearchDAO.list(Profissional.class, dto, new PagedSearchNavigation(0, 0));
		assertEquals(1, resultado.size());
		assertEquals("Sergio", resultado.get(0).getNome());
	}

	@Test
	public void testNotEquals() throws Exception {
		limparTabelas();
		EntidadeTipo ent1 = new EntidadeTipo();
		ent1.setNome("Trazer Este Registro");

		EntidadeTipo ent2 = new EntidadeTipo();
		ent2.setNome("Não trazer");
		dao.salvar(ent1, ent2);

		EntidadeTipo parametros = new EntidadeTipo();
		parametros.setNomeExclude("Não trazer");
		List<EntidadeTipo> resultado = genericSearchDAO.list(EntidadeTipo.class, parametros);
		assertEquals(1, resultado.size());
		EntidadeTipo encontrado = resultado.get(NumberUtils.INTEGER_ZERO);
		assertEquals("Trazer Este Registro", encontrado.getNome());
	}

	/**
	 * Realizar uma consulta utilizando a mesma entidade como fonte de parâmetros.
	 *
	 * @throws Exception
	 */
	@Test
	public void testLikeEntidadeComoParametros() throws Exception {
		limparTabelas();
		final TipoDocumento tipoDocumento1 = new TipoDocumento();
		tipoDocumento1.setNome("Sergio Eduardo");
		tipoDocumento1.setDescricao("Esta é uma descrição");
		tipoDocumento1.setPossuiValidade(false);

		final TipoDocumento tipoDocumento2 = new TipoDocumento();
		tipoDocumento2.setNome("Vanessa");
		tipoDocumento2.setDescricao("Esta é outra descrição");
		tipoDocumento2.setPossuiValidade(true);
		this.dao.salvar(tipoDocumento1, tipoDocumento2);

		final TipoDocumento tipoDocumentoPesquisa = new TipoDocumento();
		tipoDocumentoPesquisa.setNome("eduardo");
		final PagedSearchNavigation paginacao = new PagedSearchNavigation(0, 0);

		List<TipoDocumento> resultado = this.genericSearchDAO.list(TipoDocumento.class, tipoDocumentoPesquisa, paginacao);
		assertEquals(1, this.genericSearchDAO.size(TipoDocumento.class, tipoDocumentoPesquisa));
		assertEquals(1, resultado.size());
		final TipoDocumento tipoDocumentoConferir = resultado.get(0);
		assertEquals("Sergio Eduardo", tipoDocumentoConferir.getNome());
		assertEquals("Esta é uma descrição", tipoDocumentoConferir.getDescricao());
		assertNotNull(this.genericSearchDAO.getSingleResult(TipoDocumento.class, tipoDocumentoPesquisa));
		assertEquals(tipoDocumentoConferir, this.genericSearchDAO.getSingleResult(TipoDocumento.class, tipoDocumentoPesquisa));

		final TipoDocumento nenhumParametroPreenchido = new TipoDocumento();
		resultado = this.genericSearchDAO.list(TipoDocumento.class, nenhumParametroPreenchido, paginacao);
		boolean exception = false;
		try {
			final TipoDocumento singleResult = this.genericSearchDAO.getSingleResult(TipoDocumento.class, nenhumParametroPreenchido);
			fail("Não deveria chegar aqui" + singleResult);
		} catch (final NonUniqueResultException e) {
			exception = true;
		}
		assertTrue(exception);
		assertEquals(2, resultado.size());
		assertEquals(2, this.genericSearchDAO.size(TipoDocumento.class, nenhumParametroPreenchido));

	}

	@Test
	public void testLikeExact() throws Exception {
		limparTabelas();
		prepararCarga("4569878", CPF_TESTE1, "Dono da corretora", SITUACAO_ATIVO);
		prepararCarga("3456565", CPF_TESTE2, "Dono da corretora2", SITUACAO_ATIVO);
		prepararCarga("4548488", CPF_TESTE3, "O dono da corretora 3", SITUACAO_ATIVO);

		final Corretora corretoraPesquisa = new Corretora();
		corretoraPesquisa.setNomeResponsavel("_ono%");
		final List<Corretora> resultado = this.genericSearchDAO.list(Corretora.class, corretoraPesquisa);
		assertEquals(2, resultado.size());
	}

	@Test
	public void testLikeNestedProperties() throws Exception {
		limparTabelas();
		prepararCarga("4569878", CPF_TESTE1, "Dono da corretora", SITUACAO_ATIVO);
		prepararCarga("3456565", CPF_TESTE2, "Outro dono de corretora", SITUACAO_INATIVO);
		prepararCarga("4548488", CPF_TESTE3, "Mais outro dono de corretora", SITUACAO_ATIVO);

		final BolsaCorretoraCorretor pesquisa = new BolsaCorretoraCorretor();
		final Corretor corretorPesquisa = new Corretor();
		corretorPesquisa.setCpf(CPF_TESTE1);
		corretorPesquisa.setSituacaoCorretorCorretora(this.dao.getSituacaoByDescricao(SITUACAO_ATIVO));
		pesquisa.setCorretor(corretorPesquisa);
		List<BolsaCorretoraCorretor> resultado = this.genericSearchDAO.list(BolsaCorretoraCorretor.class, pesquisa, new PagedSearchNavigation(0, 0));
		assertEquals(1, resultado.size());
		final BolsaCorretoraCorretor bolsaConferir = resultado.get(0);
		assertNotNull(bolsaConferir.getCorretor());
		assertEquals(CPF_TESTE1, bolsaConferir.getCorretor().getCpf());

		corretorPesquisa.setSituacaoCorretorCorretora(this.dao.getSituacaoByDescricao(SITUACAO_INATIVO));
		resultado = this.genericSearchDAO.list(BolsaCorretoraCorretor.class, pesquisa, new PagedSearchNavigation(0, 5));
		assertNull(this.genericSearchDAO.getSingleResult(BolsaCorretoraCorretor.class, pesquisa));
		assertEquals(0, resultado.size());
		assertEquals(0, this.genericSearchDAO.size(BolsaCorretoraCorretor.class, pesquisa));

	}

	@Test
	public void testOrdenacao() throws Exception {
		limparTabelas();
		final SituacaoCorretorCorretora situacao1 = new SituacaoCorretorCorretora();
		situacao1.setDescricao("c");
		situacao1.setTipoSituacao("c");

		final SituacaoCorretorCorretora situacao2 = new SituacaoCorretorCorretora();
		situacao2.setDescricao("b");
		situacao2.setTipoSituacao("b");

		final SituacaoCorretorCorretora situacao3 = new SituacaoCorretorCorretora();
		situacao3.setDescricao("d");
		situacao3.setTipoSituacao("d");

		this.dao.salvar(situacao1, situacao2, situacao3);

		final SituacaoCorretorCorretora parametros = new SituacaoCorretorCorretora();
		final PagedSearchNavigation paginacao = new PagedSearchNavigation(0, 0);
		paginacao.setAscending(true);
		paginacao.setSortProperty("tipoSituacao");
		List<SituacaoCorretorCorretora> resultado = this.genericSearchDAO.list(SituacaoCorretorCorretora.class, parametros, paginacao);
		assertEquals(3, resultado.size());

		SituacaoCorretorCorretora situacaoConferir0 = resultado.get(0);
		assertEquals("b", situacaoConferir0.getDescricao());
		SituacaoCorretorCorretora situacaoConferir1 = resultado.get(1);
		assertEquals("c", situacaoConferir1.getDescricao());
		SituacaoCorretorCorretora situacaoConferir2 = resultado.get(2);
		assertEquals("d", situacaoConferir2.getDescricao());

		paginacao.setAscending(false);
		paginacao.setSortProperty("descricao");

		resultado = this.genericSearchDAO.list(SituacaoCorretorCorretora.class, parametros, paginacao);
		assertEquals(3, resultado.size());

		situacaoConferir0 = resultado.get(0);
		assertEquals("d", situacaoConferir0.getDescricao());
		situacaoConferir1 = resultado.get(1);
		assertEquals("c", situacaoConferir1.getDescricao());
		situacaoConferir2 = resultado.get(2);
		assertEquals("b", situacaoConferir2.getDescricao());

	}

	@Test
	public void testSelection() throws Exception {
		limparTabelas();
		final SituacaoCorretorCorretora situacao = new SituacaoCorretorCorretora();
		situacao.setDescricao(SITUACAO_ATIVO);
		final String assertTipo = "Não quero que este valor venha no resultado";
		situacao.setTipoSituacao(assertTipo);
		this.dao.salvar(situacao);
		final SituacaoCorretorCorretora situacaoGravada = this.dao.getSituacaoByDescricao(SITUACAO_ATIVO);
		assertNotNull(situacaoGravada);
		assertEquals(assertTipo, situacaoGravada.getTipoSituacao());

		final List<SituacaoCorretorCorretora> resultado = this.genericSearchDAO.list(SituacaoCorretorCorretora.class, new SituacaoCorretorCorretora(), new PagedSearchNavigation(0,
				5));
		assertEquals(1, resultado.size());
		final SituacaoCorretorCorretora situacaoComSelecaoDeCampos = resultado.get(0);
		assertNotNull(situacaoComSelecaoDeCampos.getId());
		assertNotNull(situacaoComSelecaoDeCampos.getDescricao());
		assertNull(situacaoComSelecaoDeCampos.getTipoSituacao());
	}

	private void gravarProfissional(final SerieAnoCurso curso1, final Regiao regiao1, final Disciplina disciplina, final String celular, final String telefone, final String email,
			final boolean efetivo, final String nome,
			final String horaInicio,
			final String horaFim, final Integer day) throws ParseException {
		final Profissional profissional = new Profissional();
		profissional.setCelular(celular);
		profissional.setCelularOperadora("vivo");
		profissional.setEfetivo(efetivo);
		profissional.setDataNascimento(new Date());
		profissional.setEmail(email);
		profissional.setValorHoraAula(BigDecimal.valueOf(45));
		profissional.setTelefone(telefone);
		profissional.setTelefoneOperadora("net");
		profissional.setFormacaoAcademica("Computer Science");
		profissional.setNome(nome);
		profissional.setObservacoes("nenhuma");
		profissional.setDataHoraCadastro(new Date());
		profissional.setDataHoraUltimaAtualizacao(new Date());
		profissional.addCurso(curso1);
		profissional.addRegiao(regiao1);
		profissional.addDisciplina(disciplina);

		this.dao.salvar(profissional);
		final Disponibilidade disponibilidade = new Disponibilidade(getTime(horaInicio), getTime(horaFim), day);
		disponibilidade.setProfissional(profissional);
		profissional.setDisponibilidades(new ArrayList<Disponibilidade>());
		profissional.getDisponibilidades().add(disponibilidade);
		this.dao.salvar(disponibilidade);
	}

	private void prepararCarga(final String cnpj, final String cpf, final String nomeResponsavel, final String situacaoDescricao) {

		SituacaoCorretorCorretora situacaoCorretorCorretora = this.dao.getSituacaoByDescricao(situacaoDescricao);
		if (situacaoCorretorCorretora == null) {
			situacaoCorretorCorretora = new SituacaoCorretorCorretora();
			situacaoCorretorCorretora.setDescricao(situacaoDescricao);
			this.dao.salvar(situacaoCorretorCorretora);
		}

		final Corretor corretor = new Corretor();
		corretor.setCpf(cpf);
		corretor.setSituacaoCorretorCorretora(situacaoCorretorCorretora);
		corretor.setDataTerminoSuspensao(new Date());

		final Corretora corretora = new Corretora();
		corretora.setCnpj(cnpj);
		corretora.setNomeResponsavel(nomeResponsavel);
		corretora.setDataTerminoSuspensao(new Date());
		corretora.setSituacaoCorretorCorretora(situacaoCorretorCorretora);

		final BolsaCorretoraCorretor bolsaCorretoraCorretor = new BolsaCorretoraCorretor();
		bolsaCorretoraCorretor.setCorretor(corretor);
		bolsaCorretoraCorretor.setCorretora(corretora);

		this.dao.salvar(corretor, corretora, bolsaCorretoraCorretor);
	}

}
