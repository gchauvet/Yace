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
package com.zatarox.chess.openchess.controllers.engines;

import com.zatarox.chess.openchess.models.materials.ChessBoard;
import java.io.Serializable;

public abstract class AbstractEngine implements Parameters, Serializable {

    private ChessBoard board = new ChessBoard();
    private RatingStrategy rating;
    private SolverStrategy solver;
    private short depth;
    private int engineTime;
    private int engineIncrement;
    private int moveTime;

    public final void setSolver(SolverStrategy solver) {
        this.solver = solver;
    }

    public final SolveResult solve() throws EngineException {
        return solver.compute(this);
    }

    public final void setRating(RatingStrategy rating) {
        this.rating = rating;
    }

    public final float rate() {
        return rating.rate(board);
    }

    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public final short getDepth() {
        return depth;
    }

    @Override
    public final int getEngineIncrement() {
        return engineIncrement;
    }

    @Override
    public final int getEngineTime() {
        return engineTime;
    }

    @Override
    public final int getMoveTime() {
        return moveTime;
    }

    @Override
    public final void setDepth(short depth) {
        this.depth = depth;
    }

    @Override
    public final void setEngineIncrement(int engineIncrement) {
        this.engineIncrement = engineIncrement;
    }

    @Override
    public final void setEngineTime(int engineTime) {
        this.engineTime = engineTime;
    }

    @Override
    public final void setMoveTime(int moveTime) {
        this.moveTime = moveTime;
    }

}
