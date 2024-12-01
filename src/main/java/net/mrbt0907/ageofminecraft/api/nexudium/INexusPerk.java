package net.mrbt0907.ageofminecraft.api.nexudium;

public interface INexusPerk
{
	public void onActivation();
	public void onActiveTick();
	public void onInactiveTick();
	public String getName();
}
