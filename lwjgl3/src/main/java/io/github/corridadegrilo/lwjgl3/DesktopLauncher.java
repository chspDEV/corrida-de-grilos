package io.github.corridadegrilo.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

//importando classe principal
import io.github.corridadegrilo.JogoCorridaGrilos;

public class DesktopLauncher {
  public static void main (String[] arg) {
      //configuracao da aplicacao para desktop
      Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

      //define a taxa de frames por segundo (fps) desejada
      config.setForegroundFPS(60);

      //define o titulo da janela do jogo
      config.setTitle("Corrida de Grilos");

      //define a largura e altura iniciais da janela
      config.setWindowedMode(800, 600);

      //habilita ou desabilita o vsync (pode ajudar a prevenir tearing)
      //config.usevsync(true); //descomente se quiser usar

      //cria e inicia a aplicacao desktop, passando a classe principal do jogo e a configuracao
      new Lwjgl3Application(new JogoCorridaGrilos(), config);
  }
}