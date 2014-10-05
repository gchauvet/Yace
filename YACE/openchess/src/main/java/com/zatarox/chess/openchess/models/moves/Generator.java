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
import com.zatarox.chess.openchess.models.materials.Square;
import java.util.List;

public interface Generator {

    /**
     * @param board
     * @return All attacking moves for current player
     */
    List<Move> attacks(ChessBoard board);
    
    /**
     * @param board Current chessboard
     * @param square Start position
     * @return All attacking moves from this position
     */
    List<Move> attacks(ChessBoard board, Square square);
    
    /**
     * @param board Current chessboard
     * @param square Start position
     * @return All non attacking moves from this position
     */
    List<Move> fills(ChessBoard board, Square square);
    
    /**
     * @param board Current chessboard
     * @return All moves for current player
     */
    List<Move> alls(ChessBoard board);
    
    /**
     * @param board Current chessboard
     * @param square Start position
     * @return All capturing/basic moves.
     */
    List<Move> alls(ChessBoard board, Square square);

}
