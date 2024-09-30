package myproject.Kjoeleskap;

import java.io.IOException;
import java.util.List;
import myproject.Matvare;

/**
 * Et interface som representerer en måte å vedvare {@link Matvare}
 */
public interface Kjoeleskap { //Behold

    /**
     * @param matvarer Listen til {@link Matvare matvarer}
     * @throws IOException
     */
    void lagreKjoeleskap(List<Matvare> matvarer) throws IOException;

    /**
     * @return Laster opp/leser matvarene lagret i {@link Matvare matvarer}
     */
    List<Matvare> openKjoeleskap() throws IOException;
}
