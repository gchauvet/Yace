/*
 * Copyright 2014 Guillaume CHAUVET.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zatarox.chess.openchess.controllers.generators;

import com.zatarox.chess.openchess.models.materials.*;
import com.zatarox.chess.openchess.models.moves.Move;
import com.zatarox.chess.openchess.models.moves.MovesFactorySingleton;
import com.zatarox.chess.openchess.models.notations.ForsythEdwardsNotation;
import com.zatarox.chess.openchess.models.notations.Notation;
import java.util.Queue;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.matchers.JUnitMatchers.hasItems;

public class KnightGeneratorTest {

    private Notation notation;
    private Generator instance;

    @Before
    public void setUp() {
        notation = new ForsythEdwardsNotation("8/8/2P1k1N1/6b1/5p2/p7/P3K3/8 w - - 0 1");
        instance = GeneratorsFactorySingleton.getInstance().from(Piece.KNIGHT);
    }

    @Test
    public void attacks() {
        final ChessBoard board = notation.create();
        final Queue<Move> attacks = instance.attacks(board, Square.G6);
        assertThat(attacks.size(), is(1));
        assertThat(attacks, hasItems(MovesFactorySingleton.getInstance().createCapture(Square.G6, Square.F4, Piece.PAWN)));
    }

    @Test
    public void fills() {
        final ChessBoard board = notation.create();
        final Queue<Move> fills = instance.fills(board, Square.G6);
        assertThat(fills.size(), is(5));
        assertThat(fills, hasItems(
                MovesFactorySingleton.getInstance().createNormal(Square.G6, Square.F8),
                MovesFactorySingleton.getInstance().createNormal(Square.G6, Square.H8),
                MovesFactorySingleton.getInstance().createNormal(Square.G6, Square.E7),
                MovesFactorySingleton.getInstance().createNormal(Square.G6, Square.E5),
                MovesFactorySingleton.getInstance().createNormal(Square.G6, Square.H4)
        ));
    }

}
