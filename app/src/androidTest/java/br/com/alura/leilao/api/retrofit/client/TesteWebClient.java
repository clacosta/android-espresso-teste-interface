package br.com.alura.leilao.api.retrofit.client;

import java.io.IOException;

import br.com.alura.leilao.api.retrofit.TesteRetrofitInicializador;
import br.com.alura.leilao.api.retrofit.service.TesteService;
import br.com.alura.leilao.model.Leilao;
import retrofit2.Call;
import retrofit2.Response;

public class TesteWebClient extends WebClient {
    final private TesteService service;

    public TesteWebClient() {
        this.service = new TesteRetrofitInicializador().getTesteService();
    }

    public Leilao salva(Leilao leilao) throws IOException {
        final Call<Leilao> call = service.salva(leilao);
        final Response<Leilao> response = call.execute();
        if (temDados(response)) {
            return response.body();
        }
        return null;
    }

    public Boolean limpaBancoDeDados() throws IOException {
        final Call<Void> call = service.limpaBancoDeDados();
        final Response<Void> response = call.execute();
        return response.isSuccessful();
    }
}
