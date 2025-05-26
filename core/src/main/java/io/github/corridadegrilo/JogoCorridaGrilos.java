package io.github.corridadegrilo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx; 

public class JogoCorridaGrilos extends Game {

    @Override
    public void create() {
        //este metodo e chamado quando o jogo e iniciado
        Gdx.app.log("jogocorridagirlos", "jogo criado, definindo tela da corrida.");
        //define a tela inicial do jogo como uma nova instancia da telacorrida
        setScreen(new TelaCorrida(this));
    }

    @Override
    public void dispose() { 
        //este metodo e chamado quando o jogo e fechado
        super.dispose(); 
        Gdx.app.log("jogocorridagirlos", "jogo descartado.");
    }
}