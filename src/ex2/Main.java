package ex2;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        File file = new File("comenzi.dat");

        // scrierea comenzilor in fisier
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            IntStream.rangeClosed(1, 15)
                    .mapToObj(i -> new Comanda(i, "Client" + (i % 5), 1000 * i, false))
                    .forEach(c -> {
                        try {
                            out.writeObject(c);
                        }
                        catch (IOException e) {
                            System.err.println("Eroare la scriere: " + e.getMessage());
                        }
                    });
        }
        catch (IOException e) {
            System.err.println("Eroare la deschidere: " + e.getMessage());
        }


        // actualizarea statusului cu RandomAccessFile
        List<Comanda> actualizate = new ArrayList<>();

        try (RandomAccessFile raf = new RandomAccessFile("comenzi.dat", "rw")) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("comenzi.dat"))) {
                while (true) {
                    Comanda c = (Comanda) ois.readObject();
                    if (c.getValoare() > 5000) {
                        c.setFinalizata(true);
                    }
                    actualizate.add(c);
                }
            }
            catch (EOFException e) {
                // final de file, ignor
            }
            catch (Exception e) {
                System.err.println("Eroare la citire: " + e.getMessage());
            }

            // suprascriere
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("comenzi.dat"))) {
                for (Comanda c : actualizate) {
                    oos.writeObject(c);
                }
            }

        }
        catch (IOException | SecurityException e) {
            System.err.println("Eroare la RandomAccessFile: " + e.getMessage());
        }


        // recitire comenzi
        List<Comanda> comenzi = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                try {
                    Comanda c = (Comanda) in.readObject();
                    comenzi.add(c);
                }
                catch (EOFException e) {
                    break;
                }
                catch (ClassNotFoundException e) {
                    System.err.println("Clasa necunoscuta: " + e.getMessage());
                }
            }
        }
        catch (IOException e) {
            System.err.println("Eroare la recitire: " + e.getMessage());
        }

        // filtrare finalizate, total finalizate, grupate per client
        List<Comanda> finalizate = comenzi.stream()
                .filter(Comanda::isFinalizata)
                .toList();

        double total = finalizate.stream()
                .mapToDouble(Comanda::getValoare)
                .sum();

        System.out.println("Comenzi finalizate:");
        finalizate.forEach(System.out::println);

        System.out.println("\nTotal comenzi finalizate: " + total);

        System.out.println("\nComenzi grupate pe client:");
        finalizate.stream()
                .collect(Collectors.groupingBy(Comanda::getNumeClient))
                .forEach((client, lista) -> {
                    System.out.println(client + ":");
                    lista.forEach(System.out::println);
                });
    }
}
