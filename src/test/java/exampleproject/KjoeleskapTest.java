package exampleproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import myproject.KjoeleskapBehandler;
import myproject.Matvare;

public class KjoeleskapTest {

    private KjoeleskapBehandler behandler;

    @BeforeEach
    public void setUp() {
        behandler = new KjoeleskapBehandler();
    }
    
    @Test
    public void testValidConstructor() {
        new Matvare ("ost", "Pålegg", 1);
    }

    @Test
    public void testUgyldigAntall() {
        assertThrows(IllegalArgumentException.class, () -> new Matvare("ost", "Pålegg", 0));
    }

    @Test
    public void testUgyldigKategori() {
        assertThrows(IllegalArgumentException.class, () -> new Matvare("ost", null, 2));
    }

    @Test
    public void testUgyldigMatvareNavn() {
        assertThrows(IllegalArgumentException.class, () -> new Matvare("2", "Pålegg", 2));
    }

    @Test
    public void leggTilNyMatvare() {
        Matvare matvare = new Matvare("Ost", "Pålegg", 1);
        behandler.leggTilMatvare(matvare);

        assertTrue(behandler.getMatvarer().contains(matvare));
    }

    @Test
    public void leggTilEksisterendeMatvare() {
        Matvare matvare1 = new Matvare("Ost", "Pålegg", 1);
        Matvare matvare2 = new Matvare("Ost", "Pålegg", 2);

        behandler.leggTilMatvare(matvare1);
        behandler.leggTilMatvare(matvare2);

        assertEquals(3, behandler.getMatvarer().get(0).getAntall());
    }

    @Test
    public void fjerneMatvareGyldig() {
        Matvare matvare = new Matvare("Ost", "Pålegg", 2);
        behandler.leggTilMatvare(matvare);

        assertTrue(behandler.fjerneMatvare(matvare, 1));
        assertEquals(1, behandler.getMatvarer().get(0).getAntall());
    }

    @Test
    public void fjerneMatvareUgyldig() {
        Matvare matvare = new Matvare("Ost", "Pålegg", 1);
        behandler.leggTilMatvare(matvare);

        assertFalse(behandler.fjerneMatvare(matvare, 2));
        assertEquals(1, behandler.getMatvarer().get(0).getAntall());
    }

    @Test
    public void lagreOgAapneKjoeleskap() throws IOException{
        Matvare matvare = new Matvare("Ost", "Pålegg", 1);
        behandler.leggTilMatvare(matvare);

        behandler.lagreKjoeleskap();
        behandler.openKjoeleskap();

        assertFalse(behandler.getMatvarer().contains(matvare));
    }

    @Test
    public void lagreOgAapneKjoeleskapEmpty() throws IOException{
        behandler.lagreKjoeleskap();
        behandler.openKjoeleskap();

        assertTrue(behandler.getMatvarer().isEmpty());
    }

    @Test
    public void getMatvarerEmpty() {
        assertTrue(behandler.getMatvarer().isEmpty());
    }

    @Test
    public void testGetAntallMatvarer() {
        Matvare matvare1 = new Matvare("Ost", "Pålegg", 1);
        Matvare matvare2 = new Matvare("Melk", "Drikke", 2);

        behandler.leggTilMatvare(matvare1);
        behandler.leggTilMatvare(matvare2);

        assertEquals(3, behandler.getAntallMatvarer());
    }

    @Test
    public void testGetAntallIKategori() {
        Matvare matvare1 = new Matvare("Ost", "Pålegg", 1);
        Matvare matvare2 = new Matvare("Melk", "Drikke", 2);
        Matvare matvare3 = new Matvare("Juice", "Drikke", 1);

        behandler.leggTilMatvare(matvare1);
        behandler.leggTilMatvare(matvare2);
        behandler.leggTilMatvare(matvare3);

        assertEquals(1, behandler.getAntallIKategori("Pålegg"));
        assertEquals(3, behandler.getAntallIKategori("Drikke"));
    }

}
