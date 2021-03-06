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

package com.zatarox.chess.openchess.models.moves;

import com.zatarox.chess.openchess.models.materials.ChessBoard;
import com.zatarox.chess.openchess.models.moves.exceptions.*;

public interface Move {

    /**
     * @param board Chess where move will be played
     * @throws IllegalMoveException
     * @throws com.zatarox.chess.openchess.models.moves.exceptions.SelfMateMoveException
     */
    void play(ChessBoard board) throws IllegalMoveException, SelfMateMoveException;

    /**
     * @param board Chessboard unplay move
     * @throws IllegalMoveException
     */
    void unplay(ChessBoard board) throws IllegalMoveException;
    
}
