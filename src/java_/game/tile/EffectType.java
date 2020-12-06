package java_.game.tile;

/**
 * The action tile effects available.
 *
 * @author nylecm
 */
public enum EffectType {
    FIRE {
        @Override
        public String toString() {
            return "fire";
        }
    }, ICE {
        @Override
        public String toString() {
            return "ice";
        }
    }, DOUBLE_MOVE {
        @Override
        public String toString() {
            return "double_move";
        }
    }, BACKTRACK {
        @Override
        public String toString() {
            return "backtrack";
        }
    }
}
