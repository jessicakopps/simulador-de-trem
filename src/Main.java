import controller.SimuladorController;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random rand = new Random();
        int numEstacoes = rand.nextInt(21) + 10; // Gera entre 10 e 30 estações
        
        System.out.println("Iniciando simulação com " + numEstacoes + " estações...");
        new SimuladorController(numEstacoes).iniciar();
    }
}