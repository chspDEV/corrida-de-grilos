package io.github.corridadegrilo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// PARTE VISUAL DO GAME
public class TelaCorrida extends ScreenAdapter {
    private SpriteBatch batch;
    private Texture texturaGrilo;
    private BitmapFont fonte;
    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderizadorFormas;

    private List<Grilo> grilos; 
    private List<Grilo> grilosFinalizados;

    private boolean corridaIniciada = false;
    private boolean corridaTerminada = false;
    private long tempoInicioCorridaMillis;

    private static final int NUMERO_DE_GRILOS = 5;
    private static final float PISTA_INICIO_Y = 100f;
    private static final float ESPACO_ENTRE_GRILOS_Y = 60f;

    private JogoCorridaGrilos jogo;

    public TelaCorrida(JogoCorridaGrilos jogo) {
        this.jogo = jogo;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        camera.update();

        batch = new SpriteBatch();
        fonte = new BitmapFont();
        fonte.setColor(Color.WHITE);
        renderizadorFormas = new ShapeRenderer();

        try {
            texturaGrilo = new Texture(Gdx.files.internal("grilo.png"));
        } catch (Exception e) {
            Gdx.app.error("carregartextura", "erro ao carregar cricket.png", e);
        }

        grilos = new ArrayList<>();
        grilosFinalizados = Collections.synchronizedList(new ArrayList<>()); //lista segura para threads

        for (int i = 0; i < NUMERO_DE_GRILOS; i++) {
            //grilo agora e uma thread, entao ele e adicionado diretamente a lista de grilos
            Grilo grilo = new Grilo("grilo_" + (i + 1), texturaGrilo, PISTA_INICIO_Y + (i * ESPACO_ENTRE_GRILOS_Y));
            grilos.add(grilo);
        }
        Gdx.app.log("telacorrida", "tela da corrida mostrada. clique para comecar.");
    }

    private void iniciarCorrida() {
        if (corridaIniciada) return;

        tempoInicioCorridaMillis = System.currentTimeMillis();
        for (Grilo grilo : grilos) {
            grilo.definirTempoInicio(tempoInicioCorridaMillis);
            grilo.start(); //inicia a thread do grilo diretamente
        }
        corridaIniciada = true;
        Gdx.app.log("telacorrida", "corrida iniciada!");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        renderizadorFormas.setProjectionMatrix(camera.combined);

        renderizadorFormas.begin(ShapeRenderer.ShapeType.Line);
        renderizadorFormas.setColor(Color.RED);
        renderizadorFormas.line(Grilo.LINHA_CHEGADA_X, 0, Grilo.LINHA_CHEGADA_X, viewport.getWorldHeight());
        renderizadorFormas.end();

        batch.begin();
        for (Grilo grilo : grilos) {
            grilo.desenhar(batch);
        }

        	
        //TEXTOS PARA STATUS DA CORRIDA
        if (!corridaIniciada) {
            fonte.draw(batch, "clique para iniciar a corrida!", viewport.getWorldWidth() / 2 - 100, viewport.getWorldHeight() - 50);
        } else if (corridaTerminada) {
            fonte.draw(batch, "corrida finalizada!", viewport.getWorldWidth() / 2 - 50, viewport.getWorldHeight() - 50);
            desenharRanking(batch);
        } else {
            fonte.draw(batch, "corrida em progresso...", viewport.getWorldWidth() / 2 - 70, viewport.getWorldHeight() - 50);
            verificarEstadoCorrida();
        }
        
        //TEXTOS  PARA ATUALIZACOES DO TEMPO
        if(corridaIniciada && !corridaTerminada){
            long tempoAtual = System.currentTimeMillis();
            float tempoDecorridoSeg = (tempoAtual - tempoInicioCorridaMillis) / 1000.0f;
            fonte.draw(batch, String.format("tempo: %.2f s", tempoDecorridoSeg), 20, viewport.getWorldHeight() - 20);
        }

        batch.end();

        if (Gdx.input.justTouched() && !corridaIniciada) {
            iniciarCorrida();
        }
    }

    private void verificarEstadoCorrida() {
        if (!corridaIniciada || corridaTerminada) return;

        boolean todosTerminaram = true;
        for (Grilo grilo : grilos) {
            if (grilo.estaTerminado() && !grilosFinalizados.contains(grilo)) {
                grilosFinalizados.add(grilo);
            }
            if (!grilo.estaTerminado()) {
                todosTerminaram = false;
            }
        }

        if (todosTerminaram) {
            corridaTerminada = true;
            Gdx.app.log("telacorrida", "todos os grilos terminaram!");
            Collections.sort(grilosFinalizados, new Comparator<Grilo>() {
                @Override
                public int compare(Grilo g1, Grilo g2) {
                    return Long.compare(g1.obterDuracaoCorridaMillis(), g2.obterDuracaoCorridaMillis());
                }
            });
            Gdx.app.log("telacorrida", "ranking finalizado.");
        }
    }

    private void desenharRanking(SpriteBatch batch) {
        float yPos = viewport.getWorldHeight() - 100;
        fonte.draw(batch, "ranking final:", 50, yPos);
        yPos -= 30;
        int rank = 1;
        for (Grilo grilo : grilosFinalizados) {
            fonte.draw(batch, rank + ". " + grilo.obterId() + " - " + String.format("%.3f", grilo.obterDuracaoCorridaMillis() / 1000.0f) + "s", 50, yPos);
            yPos -= 25;
            rank++;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        fonte.dispose();
        renderizadorFormas.dispose();
        if (texturaGrilo != null) {
            texturaGrilo.dispose();
        }

        //interrompe as threads dos grilos se ainda estiverem rodando
        if (grilos != null) {
            for (Grilo grilo : grilos) {
                if (grilo.isAlive()) { //isalive() e um metodo da classe thread
                    grilo.interrupt(); //interrupt() e um metodo da classe thread
                }
            }
        }
        Gdx.app.log("telacorrida", "tela da corrida descartada.");
    }
}