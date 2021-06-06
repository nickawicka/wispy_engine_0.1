package util;

public class Time {
	public static float time_started = System.nanoTime();
	
	public static float getTime() {
		return (float)((System.nanoTime() - time_started) * 1E-9);
	}
}
