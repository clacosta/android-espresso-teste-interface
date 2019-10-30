package br.com.alura.leilao.matchers;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import br.com.alura.leilao.R;
import br.com.alura.leilao.formatter.FormatadorDeMoeda;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

public class ViewMatcher {

    public static Matcher<? super View> apareceLeilaoNaPosicao(final int position,
                                                               final String descricao,
                                                               final double maiorLanceEsperado) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {

            private Matcher<View> displayed = isDisplayed();
            final String maiorLanceEsperadoFormatado = new
                    FormatadorDeMoeda().formata(maiorLanceEsperado);

            @Override
            public void describeTo(Description description) {
                description.appendText("View com descrição ")
                        .appendText(descricao)
                        .appendText(", maior lance ")
                        .appendValue(maiorLanceEsperadoFormatado)
                        .appendText(" na posição ")
                        .appendValue(position)
                        .appendText(" ");
                description.appendDescriptionOf(displayed);
            }

            @Override
            protected boolean matchesSafely(RecyclerView item) {
                final RecyclerView.ViewHolder viewHolder =
                        item.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    throw new IndexOutOfBoundsException("View do Viewholder posição " +
                            position +
                            " não foi encontrada");
                }
                final View itemView = viewHolder.itemView;
                final boolean temDescricaoEsperada = verificaDescricaoEsperada(itemView);
                final boolean temMaiorLanceEsperado = verificaMaiorLanceEsperado(itemView);
                displayed = isDisplayed();
                return temDescricaoEsperada && temMaiorLanceEsperado && displayed.matches(itemView);
            }

            private boolean verificaDescricaoEsperada(View itemView) {
                final TextView textViewDescricao =
                        itemView.findViewById(R.id.item_leilao_descricao);
                return textViewDescricao.getText().toString().equals(descricao);
            }

            private boolean verificaMaiorLanceEsperado(View itemView) {
                final TextView textViewMaiorLance =
                        itemView.findViewById(R.id.item_leilao_maior_lance);
                return textViewMaiorLance.getText().toString().equals(maiorLanceEsperadoFormatado);
            }
        };
    }

}
