package myproject;

import java.util.List;
import java.util.Objects;


/**
 * En Matvare
 */

public class Matvare {

    private final String matvareNavn;
    private final String kategori; 
    private int antall;

    public static final List<String> lovligeKategorier = List.of("Pålegg", "Drikke", "Grønnsaker", "Saus", "Annet"); 

    /**
     * @param matvareNavn Navn på matvaren
     * @param kategori Kategorien på matvaren. Må være en av {@link #lovligeKategorier}
     * @param antall Antall av den nye matvaren. Kan ikke væree mindre enn 1
     */

    public Matvare(String matvareNavn, String kategori, int antall) {
        if (antall < 1) {
            throw new IllegalArgumentException("Antall må være større enn 0 og mindre enn 50");
        }

        if (kategori == null) {
            throw new IllegalArgumentException("Velg en kategori");
        }

        if (matvareNavn == null || !matvareNavn.matches("[a-zA-ZæøåÆØÅ\\-]+") || matvareNavn.length() <= 2) {
            throw new IllegalArgumentException("Skriv inn en gyldig matvare på minst 3 bokstaver");
        }

        this.matvareNavn = Objects.requireNonNull(matvareNavn);
        this.kategori = Objects.requireNonNull(kategori);
        this.antall = antall;
    }

    /**
     * @return Navnet på denne matvaren
     */
    public String getMatvareNavn() {
        return matvareNavn;
    }

    /**
     * @return Kategorien til denne matvaren
     */
    public String getKategori() {
        return kategori;
    }

    /**
     * @return Antall av denne matvaren
     */
    public int getAntall() {
        return antall;
    }

    public void setAntall(int newAntall) {
        this.antall = newAntall; 
    }

    @Override
    public String toString() {
        return "[" + matvareNavn + " (" + kategori + "), antall: " + antall + "]"; 
    }

}
