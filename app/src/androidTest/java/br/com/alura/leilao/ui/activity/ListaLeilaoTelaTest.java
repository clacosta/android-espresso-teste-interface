package br.com.alura.leilao.ui.activity;

import android.content.Intent;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import br.com.alura.leilao.R;
import br.com.alura.leilao.api.retrofit.client.TesteWebClient;
import br.com.alura.leilao.formatter.FormatadorDeMoeda;
import br.com.alura.leilao.model.Leilao;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.fail;

public class ListaLeilaoTelaTest {
    private static final String ERRO_FALHA_LIMPEZA_DE_DADOS_DA_API = "Banco de dados não foi limpo";
    private static final String LEILAO_NAO_FOI_SALVO = "Leilão não foi salvo: ";
    private final TesteWebClient webClient = new TesteWebClient();
    private final String formatoEsperadoMoedaZerado = new FormatadorDeMoeda().formata(0.0);

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

        onView(allOf(withText("Carro"), withId(R.id.item_leilao_descricao)))
                .check(matches(isDisplayed()));

        onView(allOf(withText(formatoEsperadoMoedaZerado), withId(R.id.item_leilao_maior_lance)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void deve_AparecerDoisLeiloes_QuandoCarregarDoisLeiloesDaApi() throws IOException {
        tentaSalvarLeilaoNaApi(new Leilao("Carro"), new Leilao("Computador"));

        activityTestRule.launchActivity(new Intent());

//        onView(allOf(withText("Carro"), withId(R.id.item_leilao_descricao)))
//                .check(matches(isDisplayed()));
//        onView(allOf(withText(formatoEsperadoMoedaZerado), withId(R.id.item_leilao_maior_lance)))
//                .check(matches(isDisplayed()));
//
//        onView(allOf(withText("Computador"), withId(R.id.item_leilao_descricao)))
//                .check(matches(isDisplayed()));
//        onView(allOf(withText(formatoEsperadoMoedaZerado), withId(R.id.item_leilao_maior_lance)))
//                .check(matches(isDisplayed()));

        onView(withId(R.id.lista_leilao_recyclerview))
                .check(matches(apareceLeilao(0, "Computador", 0.00)));
    }

    private Matcher<? super View> apareceLeilao(final int position,
                                                final String description,
                                                final double maiorLanceEsperado) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {

            }

            @Override
            protected boolean matchesSafely(RecyclerView item) {
                final View viewHolder = item.findViewHolderForAdapterPosition(position).itemView;
                final TextView textViewDescricao =
                        viewHolder.findViewById(R.id.item_leilao_descricao);
                final boolean temDescricaoEsperada =
                        textViewDescricao.getText().toString().equals(description);
                final TextView textViewMaiorLance =
                        viewHolder.findViewById(R.id.item_leilao_maior_lance);
                final String maiorLanceEsperadoFormatado = new
                        FormatadorDeMoeda().formata(maiorLanceEsperado);
                final boolean temMaiorLanceEsperado =
                        textViewMaiorLance.getText().toString().equals(maiorLanceEsperadoFormatado);
                return temDescricaoEsperada && temMaiorLanceEsperado;
            }
        };
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
