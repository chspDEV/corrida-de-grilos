# Corrida de Grilos com Threads em Java

![Linguagem](https://img.shields.io/badge/Java-100%25-orange)
![Framework](https://img.shields.io/badge/Engine-LWJGL%203-blue)
![Status](https://img.shields.io/badge/status-conclu%C3%ADdo-green)

> Um projeto gráfico que utiliza threads em Java para visualizar uma corrida de grilos.

## 🎯 Objetivo

Este projeto serve como uma demonstração prática de multithreading em Java. Cada grilo na corrida é controlado por sua própria `Thread`, permitindo que todos se movam de forma concorrente e independente. O resultado é uma simulação visual de processos paralelos competindo para alcançar um objetivo.

## 🛠️ Tecnologias Utilizadas

-   **Linguagem:** Java
-   **Biblioteca Gráfica:** LWJGL 3 (Lightweight Java Game Library)
-   **Automação de Build:** Gradle

## ⚙️ Como Executar

O projeto utiliza o Gradle Wrapper, então não é necessário ter o Gradle instalado na máquina.

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/chspDEV/corrida-de-grilos.git](https://github.com/chspDEV/corrida-de-grilos.git)
    ```
2.  **Acesse a pasta do projeto:**
    ```bash
    cd corrida-de-grilos
    ```
3.  **Execute o projeto:**
    * No Windows:
        ```bash
        gradlew.bat run
        ```
    * No Linux ou macOS:
        ```bash
        ./gradlew run
        ```
