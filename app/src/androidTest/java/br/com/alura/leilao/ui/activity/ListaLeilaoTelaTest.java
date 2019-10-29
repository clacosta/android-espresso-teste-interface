package br.com.alura.leilao.ui.activity;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import br.com.alura.leilao.api.retrofit.client.LeilaoWebClient;
import br.com.alura.leilao.model.Leilao;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class ListaLeilaoTelaTest {

    @Rule
    public ActivityTestRule<ListaLeilaoActivity> activityTestRule =
            new ActivityTestRule<>(ListaLeilaoActivity.class, true, false);

    @Test
    public void deve_AparecerUmLeilao_QuandoCarregarUmLeilaoNaApi() throws IOException {
        final LeilaoWebClient webClient = new LeilaoWebClient();
        final Boolean bancoDeDadosNaoFoiLimpo = !webClient.limpaBancoDeDados();

        if(bancoDeDadosNaoFoiLimpo){
            fail("Banco de dados não foi limpo");
        }

        final Leilao carroLeilao = webClient.salva(new Leilao("Carro"));

        if(carroLeilao == null){
            fail("Leilão não foi salvo");
        }

        activityTestRule.launchActivity(new Intent());
        //sleep(1000);

        onView(withText("Carro")).check(matches(isDisplayed()));
    }


}