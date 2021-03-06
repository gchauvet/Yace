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
import com.zatarox.chess.openchess.models.moves.MoveVisitable;
import com.zatarox.chess.openchess.models.moves.MovesFactorySingleton;
import java.util.PriorityQueue;
import java.util.Queue;

final class PawnGenerator extends AbstractPushGenerator {

    private final long[] pawnDownwards = new long[64];
    private final long[] pawnUpwards = new long[64];

    public PawnGenerator(MovePonderingStrategy ponder) {
        super(Piece.PAWN, ponder);
        populate();
    }

    @Override
    public Queue<Move> fills(ChessBoard board, Square square) {
        final BitBoard all = board.getSide(BoardSide.WHITE).getSnapshot();
        all.merge(board.getSide(BoardSide.BLACK).getSnapshot());
        final BoardSide color = board.getStone(square).getSide();
        long mask = color == BoardSide.WHITE
                ? squareAttacked(square.toBitMask(), +8, b_u)
                : squareAttacked(square.toBitMask(), -8, b_d);
        final BitBoard attacks = new BitBoard(mask & ~all.unwrap());
        final Queue<Move> result = new PriorityQueue<>();
        if (!attacks.isEmpty()) {
            Move move = MovesFactorySingleton.getInstance().createNormal(square, attacks.iterator().next());
            getPonder().compute(board, (MoveVisitable) move);
            result.add(move);
            attacks.clear();
            if (square.getRankIndex() == Square.Rank._2 || square.getRankIndex() == Square.Rank._7) {
                mask = color == BoardSide.WHITE
                        ? squareAttacked(square.toBitMask(), +16, b_u)
                        : squareAttacked(square.toBitMask(), -16, b_d);
                attacks.merge(new BitBoard(mask));
            }
            if (!attacks.isEmpty()) {
                move = MovesFactorySingleton.getInstance().createCharge(square, attacks.iterator().next());
                getPonder().compute(board, (MoveVisitable) move);
                result.add(move);
            }
        }
        return result;
    }

    @Override
    public Queue<Move> attacks(ChessBoard board, Square square) {
        final BitBoard all = board.getSide(BoardSide.WHITE).getSnapshot();
        all.merge(board.getSide(BoardSide.BLACK).getSnapshot());
        final BitBoard attacks = new BitBoard(coverage(square, all, board.getTurn()) & board.getSide(board.getStone(square).getSide().flip()).getSnapshot().unwrap());
        final Queue<Move> result = new PriorityQueue<>();
        for (Square to : attacks) {
            if (to.getRankIndex() == Square.Rank._1 && board.getStone(square).getSide() == BoardSide.BLACK
                    || to.getRankIndex() == Square.Rank._8 && board.getStone(square).getSide() == BoardSide.WHITE) {
                for (Piece piece : new Piece[]{Piece.BISHOP, Piece.KNIGHT, Piece.QUEEN, Piece.ROOK}) {
                    final Move move = MovesFactorySingleton.getInstance().createPromotion(square, to, piece);
                    getPonder().compute(board, (MoveVisitable) move);
                    result.add(move);
                }
            } else {
                final Move move = MovesFactorySingleton.getInstance().createCapture(square, to, board.getStone(to));
                getPonder().compute(board, (MoveVisitable) move);
                result.add(move);
            }
        }
        final BoardSide attacker = board.getStone(square).getSide();
        if (board.getSide(attacker).isEnpassant()) {
            final Move move = MovesFactorySingleton.getInstance().createEnpassant(square, board.getSide(attacker).getEnpassant());
            getPonder().compute(board, (MoveVisitable) move);
            result.add(move);
        }
        return result;
    }

    /**
     * Check if a "en passant" move is present because this is a prise.
     */
    @Override
    public boolean isEnPrise(ChessBoard board, Square square) throws IllegalArgumentException {
        boolean result = super.isEnPrise(board, square);
        if(!result) {
            final BoardSide side = board.getStone(square).getSide().flip();
            if(board.getSide(side).isEnpassant()) {
                final BitBoard pawns = new BitBoard(board.getSide(side).get(Piece.PAWN));
                pawns.merge(new BitBoard(board.getSide(side).getEnpassant().toBitMask()));
                result = coverage(square, pawns, side) != 0;
            }
        }
        return result;
    }

    @Override
    protected void populate(short index, long square) {
        pawnUpwards[index] = squareAttacked(square, 7, b_u | b_r)
                | squareAttacked(square, 9, b_u | b_l);
        pawnDownwards[index] = squareAttacked(square, -7, b_d | b_l)
                | squareAttacked(square, -9, b_d | b_r);
    }

    @Override
    protected long coverage(Square index, BitBoard all, BoardSide color) {
        return color == BoardSide.BLACK ? pawnUpwards[index.ordinal()] : pawnDownwards[index.ordinal()];
    }

}
