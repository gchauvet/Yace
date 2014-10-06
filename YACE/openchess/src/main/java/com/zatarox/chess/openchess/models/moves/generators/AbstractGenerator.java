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
package com.zatarox.chess.openchess.models.moves.generators;

import com.zatarox.chess.openchess.models.materials.*;
import com.zatarox.chess.openchess.models.moves.Move;
import com.zatarox.chess.openchess.models.moves.MovesFactorySingleton;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public abstract class AbstractGenerator implements Generator {

    // Board borders
    protected static final long b_d = 0x00000000000000ffL; // down
    protected static final long b_u = 0xff00000000000000L; // up
    protected static final long b_r = 0x0101010101010101L; // right
    protected static final long b_l = 0x8080808080808080L; // left

    protected static final long b3_d = 0x0000000000ffffffL; // down
    protected static final long b3_u = 0xffffff0000000000L; // up

    private final Piece type;

    protected AbstractGenerator(Piece type) {
        this.type = type;
    }

    abstract protected long squareAttacked(long square, int shift, long border);

    final protected int magicTransform(long b, long magic, byte bits) {
        return (int) ((b * magic) >>> (64 - bits));
    }

    /**
     * Fills pieces from a mask. Neccesary for magic generation variable bits is
     * the mask bytes number index goes from 0 to 2^bits
     */
    final protected long generatePieces(int index, int bits, long mask) {
        int i;
        long lsb;
        long result = 0L;
        for (i = 0; i < bits; i++) {
            lsb = mask & (-mask);
            mask ^= lsb; // Deactivates lsb bit of the mask to get next bit next time
            if ((index & (1 << i)) != 0) {
                result |= lsb; // if bit is set to 1
            }
        }
        return result;
    }

    @Override
    public Queue<Move> attacks(ChessBoard board, Square square) {
        // Can't be final, for pawns generation...
        final BitBoard all = board.getSnapshot(BoardSide.WHITE);
        all.merge(board.getSnapshot(BoardSide.BLACK));
        final BitBoard attacks = new BitBoard(coverage(square, all, board.getTurn()) & board.getSnapshot(board.getStone(square).getSide().flip()).unwrap());
        final Queue<Move> result = new PriorityQueue<>();
        for (Square to : attacks) {
            result.add(MovesFactorySingleton.getInstance().createCapture(square, to, board.getStone(to).getPiece()));
        }
        return result;
    }

    @Override
    public Queue<Move> fills(ChessBoard board, Square square) {
        // Can't be final, for pawns generation...
        final BitBoard all = board.getSnapshot(BoardSide.WHITE);
        all.merge(board.getSnapshot(BoardSide.BLACK));
        final BitBoard attacks = new BitBoard(coverage(square, all, board.getTurn()) & ~all.unwrap());
        final Queue<Move> result = new PriorityQueue<>();
        for (Square to : attacks) {
            result.add(MovesFactorySingleton.getInstance().createNormal(square, to));
        }
        return result;
    }

    @Override
    public final Queue<Move> attacks(ChessBoard board) {
        final Queue<Move> result = new LinkedList<>();
        for (Square index : board.getSide(board.getTurn()).get(type)) {
            result.addAll(attacks(board, index));
        }
        return result;
    }

    @Override
    public final Queue<Move> alls(ChessBoard board) {
        final Queue<Move> result = new PriorityQueue<>();
        for (Square index : board.getSide(board.getTurn()).get(type)) {
            result.addAll(fills(board, index));
        }
        return result;
    }

    @Override
    public final Queue<Move> alls(ChessBoard board, Square square) {
        Queue<Move> result = attacks(board, square);
        result.addAll(fills(board, square));
        return result;
    }

    /**
     * Magic! coverage, very fast method
     *
     * @param index
     * @param all
     * @param color
     * @return
     */
    abstract protected long coverage(Square index, BitBoard all, BoardSide color);
    
    /**
     * Call it from final constructor for magic bitboard initialization.
     */
    protected void populate() {
        long square = 1;
        short i = 0;
        while (square != 0) {
            populate(i, square);
            square <<= 1;
            i++;
        }
    }
    
    abstract protected void populate(short index, long square);

}