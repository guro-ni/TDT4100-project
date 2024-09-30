package myproject;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import myproject.Kjoeleskap.CSVKjoeleskap;
import myproject.Kjoeleskap.Kjoeleskap;

/**
 * Klasse som behandler matvarer i kjøleskapet
 */
public class KjoeleskapBehandler { //Behold
    
    private final List<Matvare> matvarer;

    /**
     * Oppretter en ny kjøleskap behandler uten noen matvarer lagt til
     */
    public KjoeleskapBehandler() {
        this.matvarer = new ArrayList<>();
    }

    public void lagreKjoeleskap() throws IOException {
        Kjoeleskap storage = new CSVKjoeleskap(Path.of("kjoeleskap.csv"));
        storage.lagreKjoeleskap(this.matvarer);
    }

    public void openKjoeleskap() throws IOException {
        Kjoeleskap storage = new CSVKjoeleskap(Path.of("kjoeleskap.csv"));
        this.matvarer.clear();
        this.matvarer.addAll(storage.openKjoeleskap());
    }

    public List<Matvare> getMatvarer() {
        return Collections.unmodifiableList(this.matvarer);
    }

    /**
     * Legger til en matvare i dette kjøleskapet
     * @param matvare Matvaren
     */
    public void leggTilMatvare(Matvare matvare) {
        Objects.requireNonNull(matvare);

        String matvareNavn = matvare.getMatvareNavn();
        String kategori = matvare.getKategori();
        int antall = matvare.getAntall();

        for (Matvare existingMatvare : matvarer) {
            if (existingMatvare.getMatvareNavn().equals(matvareNavn) 
                && existingMatvare.getKategori().equals(kategori)) {
                existingMatvare.setAntall(existingMatvare.getAntall() + antall);
                return;
            }
        }

        matvarer.add(matvare);
    }

    /**
     * Fjerner en matvare fra dette kjøleskapet
     * @param matvare Matvaren
     * @return {@code true} dersom matvaren er fjernet, {@code false} dersom matvaren ikke er fjernet
     */
    public boolean fjerneMatvare(Matvare matvare, int antall) {
        if (antall >= 1 && antall <= matvare.getAntall()) {
            matvare.setAntall(matvare.getAntall() - antall);
            System.out.println("Fjernet " + antall + " stk av " + matvare.getMatvareNavn());

            if (matvare.getAntall() == 0) {
                this.matvarer.remove(matvare);
            }
            return true;
        }

        else {
            return false;
        }
    }

    /**
     * @return Antall matvarer i kjøleskapet
     */
    public int getAntallMatvarer() {
        return this.matvarer.stream().mapToInt(Matvare::getAntall).sum();
    }

    /**
     * @param kategori Kategorien
     * @return Antall matvarer innenfor den gitte kategorien
     */
    public int getAntallIKategori(String kategori) {
        return this.matvarer.stream().filter(e -> e.getKategori().equals(kategori)).mapToInt(Matvare::getAntall).sum(); 
    }

} 

