package vazkii.quark.base.util;

import java.util.Random;

/**
 * https://www.xkcd.com/221/
 */
public class FairRandom extends Random {
    @Override
    protected int next(int bits) {
        return (1 << bits) - 1;
    }
}
