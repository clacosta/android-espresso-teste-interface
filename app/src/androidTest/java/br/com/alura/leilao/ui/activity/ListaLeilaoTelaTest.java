package br.com.alura.leilao.ui.activity;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import br.com.alura.leilao.api.retrofit.client.TesteWebClient;
import br.com.alura.leilao.model.Leilao;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.fail;

public class ListaLeilaoTelaTest {
    private static final String ERRO_FALHA_LIMPEZA_DE_DADOS_DA_API = "Banco de dados não foi limpo";
    private static final String LEILAO_NAO_FOI_SALVO = "Leilão não foi salvo: ";
    private final TesteWebClient webClient = new TesteWebClient();
    @Rule
    public ActivityTestRule<ListaLeilaoActivity> activityTestRule =
            new ActivityTestRule<>(ListaLeilaoActivity.class, true, false);

    @Before
    public void setup() throws IOException {
        limpaBancoDeDadosDaApi();
    }

    @Test
    public void deve_AparecerUmLeilao_QuandoCarregarUmLeilaoNaApi() throws IOException {
        tentaSalvarLeilaoNaApi(new Leilao("Carro"));

        activityTestRule.launchActivity(new Intent());

        onView(withText("Carro")).check(matches(isDisplayed()));
    }

    @Test
    public void deve_AparecerDoisLeiloes_QuandoCarregarDoisLeiloesDaApi() throws IOException {
        tentaSalvarLeilaoNaApi(new Leilao("Carro"), new Leilao("Computador"));

        activityTestRule.launchActivity(new Intent());

        onView(withText("Carro")).check(matches(isDisplayed()));
        onView(withText("Computador")).check(matches(isDisplayed()));
    }

    private void tentaSalvarLeilaoNaApi(Leilao... leiloes) throws IOException {
        for (Leilao leilao : leiloes
        ) {
            final Leilao leilaoSalvo = webClient.salva(leilao);
            if (leilaoSalvo == null) {
                fail(LEILAO_NAO_FOI_SALVO + leilao.getDescricao());
            }
        }
    }

    private void limpaBancoDeDadosDaApi() throws IOException {
        final Boolean bancoDeDadosNaoFoiLimpo = !webClient.limpaBancoDeDados();
        if (bancoDeDadosNaoFoiLimpo) {
            fail(ERRO_FALHA_LIMPEZA_DE_DADOS_DA_API);
        }
    }

    @After
    public void tearDown() throws IOException {
        limpaBancoDeDadosDaApi();
    }
}
