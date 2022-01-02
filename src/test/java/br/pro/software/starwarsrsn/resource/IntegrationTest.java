package br.pro.software.starwarsrsn.resource;

import br.pro.software.starwarsrsn.model.Localizacao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class IntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void relatorioTest() throws Exception {
        conferirRelatorio("$.porcentagemTraidores", 0.0);
    }

    @Test
    public void reportarTraidorTest() throws Exception {
        for (int i = 0; i < 3; i++) {
            mvc.perform(MockMvcRequestBuilders.put("/rebelde/1/reportartraidor")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
        conferirRelatorio("$.porcentagemTraidores", 33.3333);
    }

    public void conferirRelatorio(String expressao, double valor) throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/relatorio")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath(expressao, Matchers.closeTo(valor, 0.001)));
    }

    @Test
    public void atualizarLocalizacaoTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/rebelde/2/localizacao")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(new Localizacao(0, 10.10, 11.11, "base nova", "galaxia nova"))))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mvc.perform(MockMvcRequestBuilders.get("/rebelde/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.localizacao.longitude", Matchers.is(11.11)));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void adicionarRebeldeTeste() throws Exception {
        String json =
        """
        {"id":4,"nome":"rebelde 4","genero":"F","nascimento":"2000-01-01",
        "localizacao":{"id":4,"latitude":4.0,"longitude":4.0,"base":"base 1","galaxia":"galaxia 1"},
        "inventario":{"id":4,"armas":4,"municoes":4,"aguas":4,"comidas":4},"idade":0,"traidor":false}
        """;

        mvc.perform(MockMvcRequestBuilders.post("/rebelde")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mvc.perform(MockMvcRequestBuilders.get("/rebelde/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.localizacao.longitude", Matchers.is(4.0)));
    }

    @Test
    public void negociarInventarioTeste() throws Exception {
        String json =
                """
                [
                    {"id":1,"armas":1,"municoes":2,"aguas":3,"comidas":2},
                    {"id":2,"armas":2,"municoes":1,"aguas":2,"comidas":3}
                ]
                """;
        mvc.perform(MockMvcRequestBuilders.put("/rebelde/1/negociar/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mvc.perform(MockMvcRequestBuilders.get("/rebelde/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inventario.armas", Matchers.is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inventario.municoes", Matchers.is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inventario.comidas", Matchers.is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inventario.aguas", Matchers.is(9)));
        mvc.perform(MockMvcRequestBuilders.get("/rebelde/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inventario.armas", Matchers.is(19)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inventario.municoes", Matchers.is(21)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inventario.comidas", Matchers.is(19)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inventario.aguas", Matchers.is(21)));
    }

}