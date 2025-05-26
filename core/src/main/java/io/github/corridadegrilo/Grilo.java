package io.github.corridadegrilo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.Random;

public class Grilo extends Thread {
    private String id; //mantem o id para logica do jogo
    private Texture textura;
    private float x;
    private float y;
    private float velocidade;
    private Random aleatorio;
    private boolean terminado;
    private long tempoFinalMillis;
    private long tempoInicioMillis;

    public static final float LINHA_CHEGADA_X = 700f;
    private static final float VELOCIDADE_MINIMA = 10f;
    private static final float VARIACAO_MAXIMA_VELOCIDADE = 1000f;

    public Grilo(String id, Texture textura, float inicioY) {
        super(id + "_thread"); //chama o construtor da classe thread para nomear a thread
        this.id = id;
        this.textura = textura;
        this.x = 0;
        this.y = inicioY;
        this.aleatorio = new Random();
        this.velocidade = 50f * aleatorio.nextFloat();
        this.terminado = false;
        this.tempoFinalMillis = 0;
    }

    public String obterId() {
        return id;
    }

    public float obterX() {
        return x;
    }

    public float obterY() {
        return y;
    }

    public boolean estaTerminado() {
        return terminado;
    }

    public long obterDuracaoCorridaMillis() {
        if (terminado && tempoInicioMillis > 0) {
            return tempoFinalMillis - tempoInicioMillis;
        }
        return Long.MAX_VALUE;
    }
    
    public long obterTempoFinalMillisBruto() {
        return tempoFinalMillis;
    }

    public Texture obterTextura() {
        return textura;
    }
    
    public void definirTempoInicio(long tempoInicioMillis) {
        this.tempoInicioMillis = tempoInicioMillis;
    }

    @Override
    public void run() {
        //loop principal do grilo
        while (x < LINHA_CHEGADA_X && !Thread.currentThread().isInterrupted()) { 
            float deltaTime = 0.016f;
            x += velocidade * deltaTime;
            try {
            	this.velocidade = VELOCIDADE_MINIMA + aleatorio.nextFloat() *  (VARIACAO_MAXIMA_VELOCIDADE - VELOCIDADE_MINIMA);
                Thread.sleep(160);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Gdx.app.log(this.getName(), "foi interrompido durante o sono"); //usando this.getname() para o nome da thread
                return;
            }
        }

        if (x >= LINHA_CHEGADA_X && !this.isInterrupted()) { //verifica se nao foi interrompido antes de finalizar
            terminado = true;
            tempoFinalMillis = System.currentTimeMillis();
            Gdx.app.log(this.getName(), "terminou em " + obterDuracaoCorridaMillis() + "ms");
        } else if (this.isInterrupted()) {
            Gdx.app.log(this.getName(), "terminou por interrupcao antes da linha de chegada");
        }
    }

    public void desenhar(SpriteBatch batch) {
        batch.draw(textura, x, y, 100, 70);
    }

    public void descartar() {
        //a textura e compartilhada, nao descarta aqui
    }
}