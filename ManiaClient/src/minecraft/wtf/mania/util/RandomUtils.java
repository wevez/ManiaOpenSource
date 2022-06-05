package wtf.mania.util;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {
	
	public static int nextInt(final int startInclusive, final int endExclusive) {
        if (endExclusive - startInclusive <= 0)
            return startInclusive;
        return startInclusive + ThreadLocalRandom.current().nextInt(endExclusive - startInclusive);
    }

    public static double nextDouble(final double startInclusive, final double endInclusive) {
        if(startInclusive == endInclusive || endInclusive - startInclusive <= 0D)
            return startInclusive;
        return startInclusive + ((endInclusive - startInclusive) * Math.random());
    }

    public static float nextFloat(final float startInclusive, final float endInclusive) {
        if(startInclusive == endInclusive || endInclusive - startInclusive <= 0F)
            return startInclusive;
        return (float) (startInclusive + ((endInclusive - startInclusive) * Math.random()));
    }
    
    public static boolean nextBoolean(int percent) {
		return ThreadLocalRandom.current().nextInt(100) <=  percent;
	}

}
