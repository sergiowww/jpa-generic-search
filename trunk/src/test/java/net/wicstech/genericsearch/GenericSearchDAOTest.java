package net.wicstech.genericsearch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.wicstech.genericsearch.dto.EditalDTO;
import net.wicstech.genericsearch.dto.PesquisaProfissionalDTO;
import net.wicstech.genericsearch.entidadestest.BolsaCorretoraCorretor;
import net.wicstech.genericsearch.entidadestest.Corretor;
import net.wicstech.genericsearch.entidadestest.Corretora;
import net.wicstech.genericsearch.entidadestest.Disciplina;
import net.wicstech.genericsearch.entidadestest.Disponibilidade;
import net.wicstech.genericsearch.entidadestest.Edital;
import net.wicstech.genericsearch.entidadestest.Profissional;
import net.wicstech.genericsearch.entidadestest.Regiao;
import net.wicstech.genericsearch.entidadestest.SerieAnoCurso;
import net.wicstech.genericsearch.entidadestest.SituacaoCorretorCorretora;
import net.wicstech.genericsearch.entidadestest.Tabelas;
import net.wicstech.genericsearch.entidadestest.TipoDocumento;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration("/GenericSearchDAOTest.xml")
public class GenericSearchDAOTest extends AbstractTransactionalJUnit4SpringContextTests {
	private static final String SITUACAO_INATIVO = "Descrição inativo";
	private static final String SITUACAO_ATIVO = "Descrição Ativo";
	private static final String CPF_TESTE2 = "4548756";
	private static final String CPF_TESTE1 = "123456";
	private static final String FORMATO_DD_MM_YYYY = "dd/MM/yyyy";
	private static final String CPF_TESTE3 = "528155";
	private static final String FORMATO_HH_MM = "HH:mm";

	@Autowired
	private GenericSearchDAO genericSearchDAO;

	@Autowired
	private TestDbDAO dao;

	@Test
	public void testBetween() throws Exception {
		limparTabelas();
		Edital edital1 = new Edital();
		edital1.setAno(2010);
		edital1.setDataLeilao(getDate("05/10/2005"));
		edital1.setNumero(2);
		edital1.setNumeroReserva(20l);

		Edital edital2 = new Edital();
		edital2.setAno(2011);
		edital2.setDataLeilao(getDate("07/10/2005"));
		edital2.setNumero(4);
		edital2.setNumeroReserva(40l);
		dao.salvar(edital1, edital2);

		EditalDTO dto = new EditalDTO();

		dto.setDataInicio(getDate("06/10/2005"));
		dto.setDataFim(getDate("08/10/2005"));

		PesquisaPaginadaDTO paginacao = new PesquisaPaginadaDTO(0, 0);
		List<Edital> resultado = genericSearchDAO.list(Edital.class, dto, paginacao);
		assertEquals(1, genericSearchDAO.size(Edital.class, dto));
		assertEquals(1, resultado.size());
		Edital editalConferir = resultado.get(0);
		assertEquals(getDate("07/10/2005"), editalConferir.getDataLeilao());
		assertEquals(Integer.valueOf(4), editalConferir.getNumero());

		dto.setDataInicio(getDate("04/10/2005"));
		resultado = genericSearchDAO.list(Edital.class, dto, paginacao);
		assertEquals(2, genericSearchDAO.size(Edital.class, dto));
		assertEquals(2, resultado.size());

		dto.setDataInicio(getDate("01/10/2005"));
		dto.setDataFim(getDate("03/10/2005"));
		resultado = genericSearchDAO.list(Edital.class, dto, paginacao);
		assertEquals(0, genericSearchDAO.size(Edital.class, dto));
		assertEquals(0, resultado.size());
	}

	private void limparTabelas() throws Exception {
		Field[] fields = Tabelas.class.getDeclaredFields();

		for (Field field : fields) {
			Object campo = field.get(null);

			deleteFromTables(ObjectUtils.toString(campo));
		}
	}

	@Test
	public void testOrdenacao() throws Exception {
		limparTabelas();
		SituacaoCorretorCorretora situacao1 = new SituacaoCorretorCorretora();
		situacao1.setDescricao("c");
		situacao1.setTipoSituacao("c");

		SituacaoCorretorCorretora situacao2 = new SituacaoCorretorCorretora();
		situacao2.setDescricao("b");
		situacao2.setTipoSituacao("b");

		SituacaoCorretorCorretora situacao3 = new SituacaoCorretorCorretora();
		situacao3.setDescricao("d");
		situacao3.setTipoSituacao("d");

		dao.salvar(situacao1, situacao2, situacao3);

		SituacaoCorretorCorretora parametros = new SituacaoCorretorCorretora();
		PesquisaPaginadaDTO paginacao = new PesquisaPaginadaDTO(0, 0);
		paginacao.setAscending(true);
		paginacao.setSortProperty("tipoSituacao");
		List<SituacaoCorretorCorretora> resultado = genericSearchDAO.list(SituacaoCorretorCorretora.class, parametros, paginacao);
		assertEquals(3, resultado.size());

		SituacaoCorretorCorretora situacaoConferir0 = resultado.get(0);
		assertEquals("b", situacaoConferir0.getDescricao());
		SituacaoCorretorCorretora situacaoConferir1 = resultado.get(1);
		assertEquals("c", situacaoConferir1.getDescricao());
		SituacaoCorretorCorretora situacaoConferir2 = resultado.get(2);
		assertEquals("d", situacaoConferir2.getDescricao());

		paginacao.setAscending(false);
		paginacao.setSortProperty("descricao");

		resultado = genericSearchDAO.list(SituacaoCorretorCorretora.class, parametros, paginacao);
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
		SituacaoCorretorCorretora situacao = new SituacaoCorretorCorretora();
		situacao.setDescricao(SITUACAO_ATIVO);
		String assertTipo = "Não quero que este valor venha no resultado";
		situacao.setTipoSituacao(assertTipo);
		dao.salvar(situacao);
		SituacaoCorretorCorretora situacaoGravada = dao.getSituacaoByDescricao(SITUACAO_ATIVO);
		assertNotNull(situacaoGravada);
		assertEquals(assertTipo, situacaoGravada.getTipoSituacao());

		List<SituacaoCorretorCorretora> resultado = genericSearchDAO.list(SituacaoCorretorCorretora.class, new SituacaoCorretorCorretora(), new PesquisaPaginadaDTO(0, 5));
		assertEquals(1, resultado.size());
		SituacaoCorretorCorretora situacaoComSelecaoDeCampos = resultado.get(0);
		assertNotNull(situacaoComSelecaoDeCampos.getId());
		assertNotNull(situacaoComSelecaoDeCampos.getDescricao());
		assertNull(situacaoComSelecaoDeCampos.getTipoSituacao());
	}

	/**
	 * Realizar uma consulta utilizando a mesma entidade como fonte de parâmetros.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLikeEntidadeComoParametros() throws Exception {
		limparTabelas();
		TipoDocumento tipoDocumento1 = new TipoDocumento();
		tipoDocumento1.setNome("Sergio Eduardo");
		tipoDocumento1.setDescricao("Esta é uma descrição");
		tipoDocumento1.setPossuiValidade(false);

		TipoDocumento tipoDocumento2 = new TipoDocumento();
		tipoDocumento2.setNome("Vanessa");
		tipoDocumento2.setDescricao("Esta é outra descrição");
		tipoDocumento2.setPossuiValidade(true);
		dao.salvar(tipoDocumento1, tipoDocumento2);

		TipoDocumento tipoDocumentoPesquisa = new TipoDocumento();
		tipoDocumentoPesquisa.setNome("eduardo");
		PesquisaPaginadaDTO paginacao = new PesquisaPaginadaDTO(0, 0);

		List<TipoDocumento> resultado = genericSearchDAO.list(TipoDocumento.class, tipoDocumentoPesquisa, paginacao);
		assertEquals(1, genericSearchDAO.size(TipoDocumento.class, tipoDocumentoPesquisa));
		assertEquals(1, resultado.size());
		TipoDocumento tipoDocumentoConferir = resultado.get(0);
		assertEquals("Sergio Eduardo", tipoDocumentoConferir.getNome());
		assertEquals("Esta é uma descrição", tipoDocumentoConferir.getDescricao());

		TipoDocumento nenhumParametroPreenchido = new TipoDocumento();
		resultado = genericSearchDAO.list(TipoDocumento.class, nenhumParametroPreenchido, paginacao);
		assertEquals(2, resultado.size());
		assertEquals(2, genericSearchDAO.size(TipoDocumento.class, nenhumParametroPreenchido));

	}

	@Test
	public void testLikeNestedProperties() throws Exception {
		limparTabelas();
		prepararCarga("4569878", CPF_TESTE1, "Dono da corretora", SITUACAO_ATIVO);
		prepararCarga("3456565", CPF_TESTE2, "Outro dono de corretora", SITUACAO_INATIVO);
		prepararCarga("4548488", CPF_TESTE3, "Mais outro dono de corretora", SITUACAO_ATIVO);

		BolsaCorretoraCorretor pesquisa = new BolsaCorretoraCorretor();
		Corretor corretorPesquisa = new Corretor();
		corretorPesquisa.setCpf(CPF_TESTE1);
		corretorPesquisa.setSituacaoCorretorCorretora(dao.getSituacaoByDescricao(SITUACAO_ATIVO));
		pesquisa.setCorretor(corretorPesquisa);
		List<BolsaCorretoraCorretor> resultado = genericSearchDAO.list(BolsaCorretoraCorretor.class, pesquisa, new PesquisaPaginadaDTO(0, 0));
		assertEquals(1, resultado.size());
		BolsaCorretoraCorretor bolsaConferir = resultado.get(0);
		assertNotNull(bolsaConferir.getCorretor());
		assertEquals(CPF_TESTE1, bolsaConferir.getCorretor().getCpf());

		corretorPesquisa.setSituacaoCorretorCorretora(dao.getSituacaoByDescricao(SITUACAO_INATIVO));
		resultado = genericSearchDAO.list(BolsaCorretoraCorretor.class, pesquisa, new PesquisaPaginadaDTO(0, 5));
		assertEquals(0, resultado.size());
		assertEquals(0, genericSearchDAO.size(BolsaCorretoraCorretor.class, pesquisa));

	}

	@Test
	public void testCollections() throws Exception {
		limparTabelas();
		SerieAnoCurso curso1 = new SerieAnoCurso();
		curso1.setDescricao("Curso 1");

		SerieAnoCurso curso2 = new SerieAnoCurso();
		curso2.setDescricao("Curso 2");

		Regiao regiao1 = new Regiao();
		regiao1.setCidade("Brasília");
		regiao1.setDescricao("DF");

		Regiao regiao2 = new Regiao();
		regiao2.setCidade("Taguatinga");
		regiao2.setDescricao("DF");

		Disciplina disciplina1 = new Disciplina();
		disciplina1.setDescricao("Disciplina 1");

		Disciplina disciplina2 = new Disciplina();
		disciplina2.setDescricao("Disciplina 2");

		dao.salvar(curso1, curso2, regiao1, regiao2, disciplina1, disciplina2);

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
		List<Profissional> resultado = genericSearchDAO.list(Profissional.class, dto, new PesquisaPaginadaDTO(0, 0));
		assertTrue(resultado.isEmpty());
		assertEquals(0, genericSearchDAO.size(Profissional.class, dto));

		dto = new PesquisaProfissionalDTO();
		dto.setInicio(getTime("19:00"));
		dto.setFim(getTime("20:00"));
		resultado = genericSearchDAO.list(Profissional.class, dto, new PesquisaPaginadaDTO(0, 0));
		assertEquals(1, resultado.size());
		assertEquals(1, genericSearchDAO.size(Profissional.class, dto));
		assertEquals("Sergio", resultado.get(0).getNome());

		dto = new PesquisaProfissionalDTO();
		resultado = genericSearchDAO.list(Profissional.class, dto, new PesquisaPaginadaDTO(0, 0));
		assertEquals(2, resultado.size());

		dto = new PesquisaProfissionalDTO();
		dto.setRegiao(regiao2);
		resultado = genericSearchDAO.list(Profissional.class, dto, new PesquisaPaginadaDTO(0, 0));
		assertEquals(1, resultado.size());
		assertEquals("João", resultado.get(0).getNome());

		dto = new PesquisaProfissionalDTO();
		dto.setCurso(curso1);
		resultado = genericSearchDAO.list(Profissional.class, dto, new PesquisaPaginadaDTO(0, 0));
		assertEquals(1, resultado.size());
		assertEquals("Sergio", resultado.get(0).getNome());

		dto = new PesquisaProfissionalDTO();
		dto.setDisciplina(disciplina2);
		resultado = genericSearchDAO.list(Profissional.class, dto, new PesquisaPaginadaDTO(0, 0));
		assertEquals(1, resultado.size());
		assertEquals("João", resultado.get(0).getNome());

		dto = new PesquisaProfissionalDTO();
		dto.setDisciplina(disciplina1);
		resultado = genericSearchDAO.list(Profissional.class, dto, new PesquisaPaginadaDTO(0, 0));
		assertEquals(1, resultado.size());
		assertEquals("Sergio", resultado.get(0).getNome());

		dto = new PesquisaProfissionalDTO();
		dto.setDiaSemana(Calendar.SATURDAY);
		resultado = genericSearchDAO.list(Profissional.class, dto, new PesquisaPaginadaDTO(0, 0));
		assertEquals(1, resultado.size());
		assertEquals("João", resultado.get(0).getNome());

		dto = new PesquisaProfissionalDTO();
		dto.setTelefones("334634522");
		resultado = genericSearchDAO.list(Profissional.class, dto, new PesquisaPaginadaDTO(0, 0));
		assertEquals(1, resultado.size());
		assertEquals("Sergio", resultado.get(0).getNome());
	}

	private void gravarProfissional(SerieAnoCurso curso1, Regiao regiao1, Disciplina disciplina, String celular, String telefone, String email, boolean efetivo, String nome,
			String horaInicio,
			String horaFim, Integer day) throws ParseException {
		Profissional profissional = new Profissional();
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

		dao.salvar(profissional);
		Disponibilidade disponibilidade = new Disponibilidade(getTime(horaInicio), getTime(horaFim), day);
		disponibilidade.setProfissional(profissional);
		profissional.setDisponibilidades(new ArrayList<Disponibilidade>());
		profissional.getDisponibilidades().add(disponibilidade);
		dao.salvar(disponibilidade);
	}

	private void prepararCarga(String cnpj, String cpf, String nomeResponsavel, String situacaoDescricao) {

		SituacaoCorretorCorretora situacaoCorretorCorretora = dao.getSituacaoByDescricao(situacaoDescricao);
		if (situacaoCorretorCorretora == null) {
			situacaoCorretorCorretora = new SituacaoCorretorCorretora();
			situacaoCorretorCorretora.setDescricao(situacaoDescricao);
			dao.salvar(situacaoCorretorCorretora);
		}

		Corretor corretor = new Corretor();
		corretor.setCpf(cpf);
		corretor.setSituacaoCorretorCorretora(situacaoCorretorCorretora);
		corretor.setDataTerminoSuspensao(new Date());

		Corretora corretora = new Corretora();
		corretora.setCnpj(cnpj);
		corretora.setNomeResponsavel(nomeResponsavel);
		corretora.setDataTerminoSuspensao(new Date());
		corretora.setSituacaoCorretorCorretora(situacaoCorretorCorretora);

		BolsaCorretoraCorretor bolsaCorretoraCorretor = new BolsaCorretoraCorretor();
		bolsaCorretoraCorretor.setCorretor(corretor);
		bolsaCorretoraCorretor.setCorretora(corretora);

		dao.salvar(corretor, corretora, bolsaCorretoraCorretor);
	}

	private Date getTime(String dateString) {
		return parseDate(dateString, FORMATO_HH_MM);
	}

	private Date getDate(String dateString) {
		return parseDate(dateString, FORMATO_DD_MM_YYYY);
	}

	private Date parseDate(String dateString, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		try {
			return sdf.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}

}
