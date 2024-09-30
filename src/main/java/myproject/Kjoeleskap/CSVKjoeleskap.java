package myproject.Kjoeleskap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;

import myproject.Matvare;

public class CSVKjoeleskap implements Kjoeleskap { //Behold
    
    private final Path path;

    public CSVKjoeleskap(Path path) {
        this.path = Objects.requireNonNull(path);
    }

    @Override
    public void lagreKjoeleskap(List<Matvare> matvarer) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(this.path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Matvare matvare : matvarer) {
                String line = matvare.getMatvareNavn() + "," + matvare.getKategori() + "," + matvare.getAntall() + "\n";
                writer.write(line);
            }
        }
        
    }

    @Override
    public List<Matvare> openKjoeleskap() throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(this.path)) {
            return reader.lines().map(line -> {
                String[] parts = line.split(",");
                String matvareNavn = parts[0];
                String kategori = parts[1];
                int antall = Integer.parseInt(parts[2]);

                return new Matvare(matvareNavn, kategori, antall);
            }).toList();
        }
    }

}
