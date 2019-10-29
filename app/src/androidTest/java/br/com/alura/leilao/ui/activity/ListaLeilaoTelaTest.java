package br.com.alura.leilao.ui.activity;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

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
    final TesteWebClient webClient = new TesteWebClient();
    @Rule
    public ActivityTestRule<ListaLeilaoActivity> activityTestRule =
            new ActivityTestRule<>(ListaLeilaoActivity.class, true, false);

    @Test
    public void deve_AparecerUmLeilao_QuandoCarregarUmLeilaoNaApi() throws IOException {
        limpaBaseDeDadosDaApi();

        tentaSalvarLeilaoNaApi(new Leilao("Carro"));

        activityTestRule.launchActivity(new Intent());

        onView(withText("Carro")).check(matches(isDisplayed()));
    }

    @Test
    public void deve_AparecerDoisLeiloes_QuandoCarregarDoisLeiloesDaApi() throws IOException {
        limpaBaseDeDadosDaApi();

        tentaSalvarLeilaoNaApi(new Leilao("Carro"), new Leilao("Computador"));

        activityTestRule.launchActivity(new Intent());

        onView(withText("Carro")).check(matches(isDisplayed()));
        onView(withText("Computador")).check(matches(isDisplayed()));
    }

    private void limpaBaseDeDadosDaApi() throws IOException {
        final Boolean bancoDeDadosNaoFoiLimpo = !webClient.limpaBancoDeDados();
        if (bancoDeDadosNaoFoiLimpo) {
            fail("Banco de dados não foi limpo");
        }
    }

    private void tentaSalvarLeilaoNaApi(Leilao... leiloes) throws IOException {
        for (Leilao leilao : leiloes
        ) {
            final Leilao leilaoSalvo = webClient.salva(leilao);
            if (leilaoSalvo == null) {
                fail("Leilão não foi salvo: " + leilao.getDescricao());
            }
        }
    }
}
