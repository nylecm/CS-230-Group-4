package java_;

/**
 * Represents all possible tile types.
 * @author nylecm
 */
public enum TileType {
    STRAIGHT {
        @Override
        public String toString() {
            return "straight";
        }
    }, CORNER {
        @Override
        public String toString() {
            return "corner";
        }
    }, T_SHAPED {
        @Override
        public String toString() {
            return "t_shaped";
        }
    }, GOAL {
        @Override
        public String toString() {
            return "goal";
        }
    }, FIRE {
        @Override
        public String toString() {
            return "fire";
        }
    }, ICE {
        @Override
        public String toString() {
            return "ice";
        }
    }, BACKTRACK {
        @Override
        public String toString() {
            return "backtrack";
        }
    }, DOUBLE_MOVE {
        @Override
        public String toString() {
            return "double_move";
        }
    }
}
