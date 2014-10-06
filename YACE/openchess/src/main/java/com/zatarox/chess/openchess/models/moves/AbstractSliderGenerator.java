/*
 * Copyright 2014 Guillaume.
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

import com.zatarox.chess.openchess.models.materials.Piece;

abstract class AbstractSliderGenerator extends AbstractGenerator {

    public AbstractSliderGenerator(Piece type) {
        super(type);
    }
    
    @Override
    final protected long squareAttacked(long square, int shift, long border) {
        long ret = 0;
        while ((square & border) == 0) {
            if (shift > 0) {
                square <<= shift;
            } else {
                square >>>= -shift;
            }
            ret |= square;
        }
        return ret;
    }

    final protected long squareAttackedMask(long square, int shift, long border) {
        long ret = 0;
        while ((square & border) == 0) {
            if (shift > 0) {
                square <<= shift;
            } else {
                square >>>= -shift;
            }
            if ((square & border) == 0) {
                ret |= square;
            }
        }
        return ret;
    }
    
    /**
     * Attacks for sliding pieces
     */
    final protected long checkSquareAttacked(long square, long all, int shift, long border) {
        long ret = 0;
        while ((square & border) == 0) {
            if (shift > 0) {
                square <<= shift;
            } else {
                square >>>= -shift;
            }
            ret |= square;
            // If we collide with other piece
            if ((square & all) != 0) {
                break;
            }
        }
        return ret;
    }
    
}
