package ex4;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        List<Persoana> persoane = new ArrayList<>();

        // citirea din fisier
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("date.txt"))) {
            persoane = reader.lines()
                    .map(line -> line.split(";"))
                    .filter(parts -> parts.length == 3)
                    .map(parts -> new Persoana(parts[0].trim(),
                            Integer.parseInt(parts[1].trim()),
                            parts[2].trim())).toList();
        }
        catch (IOException e) {
            System.err.println("Eroare la citire: " + e.getMessage());
            return;
        }

        // filtrare: persoane peste 30 ani din orașe care încep cu "B"
        List<Persoana> filtrate = persoane.stream()
                .filter(p -> p.getVarsta() > 30 && p.getOras().startsWith("B")).toList();

        // sortare: dupa nume si apoi dupa varsta
        List<Persoana> sortate = filtrate.stream()
                .sorted(Comparator.comparing(Persoana::getNume)
                        .thenComparing(Persoana::getVarsta)).toList();

        // grupare dupa oras
        Map<String, List<Persoana>> grupateDupaOras = persoane.stream()
                .collect(Collectors.groupingBy(Persoana::getOras));

        // agregare media de varsta per oras
        Map<String, Double> mediaVarstaPerOras = persoane.stream()
                .collect(Collectors.groupingBy(
                        Persoana::getOras,
                        Collectors.averagingInt(Persoana::getVarsta)
                ));

        // persoana cu varsta maxima
        Optional<Persoana> maxVarsta = persoane.stream()
                .max(Comparator.comparingInt(Persoana::getVarsta));


        try (PrintWriter writer = new PrintWriter("rezultat.txt")) {

            writer.println("Persoane peste 30 ani din orase care incep cu 'B' si sortate:");
            sortate.forEach(writer::println);

            writer.println("\nMedia varstei per oras:");
            mediaVarstaPerOras.forEach((oras, medie) ->
                    writer.println(oras + " - " + String.format("%.2f", medie))
            );

        }
        catch (IOException e) {
            System.err.println("Eroare la scriere: " + e.getMessage());
        }

        maxVarsta.ifPresent(p -> System.out.println("Persoana cu varsta maxima: " + p));
    }
}
