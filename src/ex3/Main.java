package ex3;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import ex3.Client;

public class Main {
    public static void main(String[] args) {
        List<Client> clienti = List.of(
                new Client("Andrei", 22, 3400, Optional.of("VIP")),
                new Client("Maria", 30, 4200, Optional.of("Standard")),
                new Client("Ioana", 19, 1200, Optional.of("Nou")),
                new Client("George", 41, 8000, Optional.of("VIP")),
                new Client("Elena", 28, 5000, Optional.of("Standard")),
                new Client("Vlad", 24, 6000, Optional.empty()),
                new Client("Carmen", 35, 9100, Optional.of("VIP")),
                new Client("Tudor", 45, 3000, Optional.of("Standard")),
                new Client("Bianca", 20, 1500, Optional.of("Nou")),
                new Client("Darius", 21, 400, Optional.of("Nou")),
                new Client("Lavinia", 27, 7800, Optional.of("VIP")),
                new Client("Alex", 18, 500, Optional.empty())
        );

        // supplier pt medie sume
        Supplier<Double> mediaSume = () -> clienti.stream()
                .mapToDouble(Client::getSumaCont)
                .average()
                .orElse(0.0);

        double media = mediaSume.get();
        System.out.println("Media sumelor: " + media + "\n");

        //predicate pt clienti vip cu suma > media
        Predicate<Client> esteVipSiPesteMedia = c ->
                c.getTipClient().orElse("Necunoscut").equals("VIP") && c.getSumaCont() > media;

        List<Client> clientiVipPesteMedia = clienti.stream()
                .filter(esteVipSiPesteMedia).toList();

        System.out.println("Clienti VIP cu suma peste medie:");
        clientiVipPesteMedia.forEach(System.out::println);

        //mapare format nume - varsta ani
        Function<Client, String> formatNumeVarsta = c -> c.getNume() + " - " + c.getVarsta() + " ani";
        List<String> formatati = clienti.stream()
                .map(formatNumeVarsta)
                .toList();

        System.out.println("\nMapare clienti:");
        formatati.forEach(System.out::println);

        // suma totala a sumelor din cont
        BiFunction<Double, Client, Double> adunare = (total, c) -> total + c.getSumaCont();
        double totalSume = clienti.stream()
                .reduce(0.0, adunare, Double::sum);

        System.out.println("\nSuma totala din conturi: " + totalSume);

        // grupare dupq tip client
        Map<String, Long> grupare = clienti.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getTipClient().orElse("Necunoscut"),
                        Collectors.counting()
                ));

        System.out.println("\nNumar clienti pe tip:");
        grupare.forEach((tip, count) -> System.out.println(tip + ": " + count));

        // nume clienți < 25 ani într-un singur String
        String tineri = clienti.stream()
                .filter(c -> c.getVarsta() < 25)
                .map(Client::getNume)
                .collect(Collectors.joining(", "));

        System.out.println("\nClienti sub 25 ani: " + tineri);
    }
}
