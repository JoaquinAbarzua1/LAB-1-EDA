import java.util.Random;
import java.util.Scanner;
// Se importa las clases random y scaner de biblioteca estandar //
public class BigVigenere {
    private int[] key;
    private char[][] alphabet;
    private static final String Alphabet_Caracters = "abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ0123456789";
    private static final int Alphabet_Size = Alphabet_Caracters.length();
//se crea clase principal con key arreglo de neteros alphabet matriz aphabet caracteres que seran las letras size el tamaño //

    public BigVigenere(String numericKey) {
        this.key = convertKey(numericKey);
        generateAlphabet();
    }
    // El constructor toma clave numerica y la convierte en arreglos de enteros y luego genera el alfabeto//

    private int[] convertKey(String keyString) {
        int[] resultado = new int[keyString.length()];
        for (int i = 0; i < keyString.length(); i++) {
            resultado[i] = Character.getNumericValue(keyString.charAt(i)) % Alphabet_Size;
        }
        return resultado;
    }
// Convierte clave numerica en tipo string en un arreglo de enteros cada digito de la clave se convierte a su valor numerico y se toma el valor del modulo alfabetosize//

    private void generateAlphabet() {
        alphabet = new char[Alphabet_Size][Alphabet_Size];
        for (int i = 0; i < Alphabet_Size; i++) {
            for (int j = 0; j < Alphabet_Size; j++) {
                alphabet[i][j] = Alphabet_Caracters.charAt((i + j) % Alphabet_Size);
            }
        }
    }
//crea la matris bidimensional  //

    public String encrypt(String message) {
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            int posMensaje = Alphabet_Caracters.indexOf(c);
            if (posMensaje == -1) {
                resultado.append(c);
                continue;
            }
            int posClave = key[i % key.length];
            resultado.append(alphabet[posMensaje][posClave]);
        }
        return resultado.toString();
    }
// cifra un mensaje utilizando el cigrado de vigenere //

    public String decrypt(String encryptedMessage) {
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < encryptedMessage.length(); i++) {
            char c = encryptedMessage.charAt(i);
            int posClave = key[i % key.length];
            int originalIndex = -1;
            for (int j = 0; j < Alphabet_Size; j++) {
                if (alphabet[j][posClave] == c) {
                    originalIndex = j;
                    break;
                }
            }
            if (originalIndex == -1) {
                resultado.append(c);
            } else {
                resultado.append(Alphabet_Caracters.charAt(originalIndex));
            }
        }
        return resultado.toString();
    }
//Este metodo desifra un mensaje //




    // Busqueda optimizada //
    public static class Benchmark {
        private static String generarMensaje(int largo) {
            String chars = Alphabet_Caracters;
            StringBuilder mensaje = new StringBuilder();
            Random rand = new Random();
            for (int i = 0; i < largo; i++) {
                mensaje.append(chars.charAt(rand.nextInt(chars.length())));
            }
            return mensaje.toString();
        }
        // Clase encargada de generar puebas automaticas//
        private static String generarClave(int largo) {
            StringBuilder clave = new StringBuilder();
            Random rand = new Random();
            for (int i = 0; i < largo; i++) {
                clave.append(rand.nextInt(10));
            }
            return clave.toString();
        }

        public static void ejecutarPruebasAutomaticas() {
            int[] tamaños = {10, 50, 100, 500, 1000, 5000, 10000, 50000, 100000, 500000};
            String mensaje = generarMensaje(10000);
            System.out.println("TamañoClave | TiempoEncrypt(ms) | TiempoDecrypt(ms)");

            for (int tamaño : tamaños) {
                String clave = generarClave(tamaño);
                BigVigenere vigenere = new BigVigenere(clave);

                long startEncrypt = System.nanoTime();
                String cifrado = vigenere.encrypt(mensaje);
                long endEncrypt = System.nanoTime();

                long startDecrypt = System.nanoTime();
                String descifrado = vigenere.decrypt(cifrado);
                long endDecrypt = System.nanoTime();

                long tiempoEncrypt = (endEncrypt - startEncrypt) / 1_000_000;
                long tiempoDecrypt = (endDecrypt - startDecrypt) / 1_000_000;

                System.out.printf("%11d | %16d | %17d%n", tamaño, tiempoEncrypt, tiempoDecrypt);
            }
        }
        //un modo manual para comprobar si encripta bien //
        public static void ejecutarModoManual() {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Ingresa una clave numérica: ");
            String clave = scanner.nextLine();
            BigVigenere vigenere = new BigVigenere(clave);

            System.out.print("Ingresa el mensaje a cifrar: ");
            String mensaje = scanner.nextLine();

            long startEncrypt = System.nanoTime();
            String cifrado = vigenere.encrypt(mensaje);
            long endEncrypt = System.nanoTime();

            long startDecrypt = System.nanoTime();
            String descifrado = vigenere.decrypt(cifrado);
            long endDecrypt = System.nanoTime();

            System.out.println("Mensaje cifrado: " + cifrado);
            System.out.println("Mensaje descifrado: " + descifrado);

            System.out.println("Tiempo de cifrado (ms): " + ((endEncrypt - startEncrypt) / 1_000_000));
            System.out.println("Tiempo de descifrado (ms): " + ((endDecrypt - startDecrypt) / 1_000_000));
        }
    }
    // menu para elegir opciones de manual o automatico //
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Menú ===");
        System.out.println("1. Ejecutar pruebas automáticas");
        System.out.println("2. Ingresar clave y mensaje manualmente");
        System.out.print("Opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine(); // limpiar buffer

        if (opcion == 1) {
            Benchmark.ejecutarPruebasAutomaticas();
        } else if (opcion == 2) {
            Benchmark.ejecutarModoManual();
        } else {
            System.out.println("Opción inválida.");
        }

        scanner.close();
    }

}