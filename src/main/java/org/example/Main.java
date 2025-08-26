package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("\n\u001B[35m-- PROCESAMIENTO DEL PRINCIPITO --");

        String texto = "\"Cuando yo tenía seis años vi en un libro una magnífica lámina. \n" +
                "Representaba una serpiente boa que se tragaba a una fiera. \n" +
                "En el libro se decía: \"Las boas tragan a sus presas enteras, sin masticarlas. \n" +
                "Después ya no pueden moverse y duermen durante los seis meses de su digestión\". \n" +
                "Reflexioné mucho entonces sobre las aventuras de la selva y, a mi vez, logré \n" +
                "trazar con un lápiz de colores mi primer dibujo. \n" +
                "Era una obra maestra que representaba una serpiente boa digiriendo un elefante.\n" +
                "Mostré mi obra a las personas mayores y les pregunté si mi dibujo les asustaba.\n" +
                "Me respondieron: \"¿Por qué habría de asustar un sombrero?\".\n" +
                "Mi dibujo no representaba un sombrero. \n" +
                "Representaba una serpiente boa que digiere un elefante.\n" +
                "Es necesario explicar a los adultos muchas cosas, porque nunca comprenden nada por sí mismos." +
                "\"\n";

        System.out.println("\nTexto: \n" + texto);

        //Validar si el texto esta vacio
        try {
            if (texto.trim().isEmpty()) {
                throw new IllegalArgumentException("\u001B[31m✖️ No puedes tener el texto vacio ✖️\u001B[m");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        //Elimina caracteres especialas deja solo valores alfa numericos y espacios
        String nuevoTexto = texto.replaceAll("[^[a-zA-Z0-9 ]+$]", "");
        System.out.println("\u001B[34m" + nuevoTexto + "\u001B[0m");

        //Separa cada palabra cuando hay espacio coma ¿?
        String[] partes = nuevoTexto.split("[ ¿,\\.?]");

        /*
        //Prueba si la lista es nula
        ArrayList<String> textoSeparado = null;
        */

        ArrayList<String> textoSeparado = new ArrayList<>(Arrays.asList(partes));
        System.out.println("\n\u001B[93mTexto separado:" + textoSeparado);

        System.out.println("Tamaño:" + textoSeparado.size() + "\u001B[0m");

        //Excepcion para validar si la lista es vacia
        try {
            if (textoSeparado == null) {
                throw new NullPointerException("\u001B[31m✖️ No puedes tener la lista vaia ✖️\u001B[m");
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

        //Crea HashMap y llama el metodo para contar las palabras
        HashMap<String, Long> PalabraPorCantidad = letrasPorCantidadMetodo(textoSeparado);
        System.out.println("\nVeces que aparecen las palabras: " + PalabraPorCantidad);

        //Ranking de palabras por frecuencia metodo void solo muestra
        int topNumeros = 0;
        boolean numeroEsValido;

        do {
            numeroEsValido = true;

            try {
                System.out.print("\n\u001B[95mCual es el top de palabras que quieres que se muestren por su frecuencia (entre 1 y "
                        + textoSeparado.size() + "): ");
                topNumeros = scanner.nextInt();

                // Validar rango o lanza excepcion
                if (topNumeros <= 0 || topNumeros > textoSeparado.size()) {
                    numeroEsValido = false;
                    throw new InputMismatchException("✖️ Número inválido. Debe estar entre 1 y " + textoSeparado.size() + ".");
                }

            } catch (InputMismatchException e) {
                System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
            }
        } while (!numeroEsValido);

        //Top palabras mas repetidas
        System.out.println("top " + topNumeros + " palabras más frecuentes:");
        mostrarTop(PalabraPorCantidad, topNumeros);

        //Longitud de las palabras
        /*
        HashMap<String, Integer> longitudPalabras = textoSeparado.stream().map(n->n,m-> n.length()).collect(Collectors.toCollection(HashMap::new));
        */
        HashMap<String, Integer> longitudPalabras = textoSeparado.stream().collect(Collectors.toMap(n -> n, m -> m.length(), (a, b) -> a, HashMap::new));

        System.out.println("\n\u001B[32mPromedio de la longitud de las palabras: " + calcularPromedio(longitudPalabras));

        System.out.println("\n\u001B[38;5;214mCONSULTAS: \n");


        String[] palabrasDistintas = textoSeparado.stream().distinct().toArray(String[]::new);
        System.out.println("1. Mostrar cuántas palabras distintas hay en el texto: " + Arrays.toString(palabrasDistintas));
        System.out.print("2. Consultar una palabra en especifico: ");
        scanner.nextLine();

        String palabraBuscada = scanner.nextLine();

        long cantidad = textoSeparado.stream().filter(p -> p.equalsIgnoreCase(palabraBuscada)).count();
        System.out.println("La palabra \"" + palabraBuscada + "\" aparece " + cantidad + " veces.");
        System.out.print("3. Calcular el porcentaje de aparición de la palabra más frecuente frente al total: ");

        //Palabra que mas se repite en porcentaje a las demas
        long frecuenciaMaxima = Collections.max(PalabraPorCantidad.values());
        double promedioAparicionMaximo = ((double) frecuenciaMaxima / textoSeparado.size()) * 100;
        String palabraMasFrecuente = PalabraPorCantidad.entrySet().stream().filter(entry -> entry.getValue() == frecuenciaMaxima).map(Map.Entry::getKey)
                .findFirst().orElse("No encontrada");

        System.out.println("La palabra más frecuente es '" + palabraMasFrecuente + "' y representa " + promedioAparicionMaximo + "% del total de palabras.");

    }

    //Metodo que cuenta cuantas veces aparece la palabra
    public static HashMap<String, Long> letrasPorCantidadMetodo(ArrayList<String> listaLetras) {
        HashMap<String, Long> letraPorCantidad = listaLetras.stream().collect(Collectors.groupingBy(n -> n, HashMap::new, Collectors.counting()));
        return letraPorCantidad;
    }

    //Metodo mostrar top palabras mas frecuentes
    public static void mostrarTop(HashMap<String, Long> mapa, int n) {
        //.entrySet() Obtiene todos los pares clave=valor del HashMap
        mapa.entrySet().stream().sorted((a, b) -> Long.compare(b.getValue(), a.getValue())).limit(n)
                .forEach(e -> System.out.println(e.getKey() + " = " + e.getValue()));
    }

    public static double calcularPromedio(HashMap<String, Integer> hashPromedio) {
        int sumaLetras = hashPromedio.values().stream().reduce(0, (a, b) -> a + b);

        int cantidad = hashPromedio.size();

        double promedioLetras = (double) sumaLetras / cantidad;
        return promedioLetras;
    }
}