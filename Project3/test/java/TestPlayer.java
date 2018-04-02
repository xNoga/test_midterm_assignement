import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import java.util.concurrent.ThreadLocalRandom;

@RunWith(MockitoJUnitRunner.class)
public class TestPlayer {

    private Player player;
    @Mock IBoard board;
    @Mock IDie die;
    Piece piece;

    @Before
    public void setup(){
        // technically incorrect as the die is rolled twice in the takeTurn() method
        // which means it will return a random number, but that same number is returned twice.
        // Anyway, for the sake of showing how to use mocking we just go with it ;)
        when(die.roll()).thenReturn(ThreadLocalRandom.current().nextInt(1, 6+1));

        List<Piece> boardPieces = Arrays.asList(piece);
        BDDMockito.when(board.getPieces()).thenReturn(boardPieces);

        // using a stub to verify that the piece was moved
        piece = new Piece();

        player = new Player("Player 1", piece, die);
    }

    @Test
    public void testTakeTurn(){
        assertEquals(1, board.getPieces().size());
        assertEquals(0, piece.getLocation());

        player.takeTurn();
        assertEquals(true, player.getPiece().getLocation() > 0);
        System.out.println("The new location of the piece is: " + player.getPiece().getLocation());

        // just generating a new random int (that will be return twice) just so we don't get the same number 4 times.
        when(die.roll()).thenReturn(ThreadLocalRandom.current().nextInt(1, 6+1));
        int oldPos = player.getPiece().getLocation();
        player.takeTurn();
        assertEquals(true, oldPos < player.getPiece().getLocation());
        System.out.println("The new location of the piece is: " + player.getPiece().getLocation());

        // if we kept going we should start to look for whether the player was over 30 and therefore might be down to 1 or right around there.
    }

}
