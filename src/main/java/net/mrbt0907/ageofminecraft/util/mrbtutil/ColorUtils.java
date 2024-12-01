package net.mrbt0907.ageofminecraft.util.mrbtutil;

public class ColorUtils
{
	public static int toHex(int R, int G, int B)
	{
		int hex = 0;
		hex |= (R & 0xFF) << 16;
		hex |= (G & 0xFF) << 8;
		hex |= (B & 0xFF);
		return hex;
	}
	
	public static int toHex(int R, int G, int B, int A)
	{
		int hex = 0;
		hex |= (A & 0xFF) << 24;
		hex |= (R & 0xFF) << 16;
		hex |= (G & 0xFF) << 8;
		hex |= (B & 0xFF);
		return hex;
	}
	
	public static int[] toRGB(int HEX)
	{
	    int[] colors = new int[3];
	    colors[0] = (HEX >> 16) & 0xff; // red
	    colors[1] = (HEX >> 8) & 0xff; // green
	    colors[2] = HEX & 0xff; // blue
	    return colors;
	}
	
	public static int[] toRGBA(int HEX)
	{
	    int[] colors = new int[4];
	    colors[0] = (HEX >> 16) & 0xff; // red
	    colors[1] = (HEX >> 8) & 0xff; // green
	    colors[2] = HEX & 0xff; // blue
	    colors[3] = (HEX >> 24) & 0xff; // alpha
	    return colors;
	}
}
