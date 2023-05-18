import info.gridworld.actor.Actor;
import info.gridworld.grid.Location;

import java.awt.Color;

public class Block extends Actor
{
	public Block(Color c, Location loc)
	{
		this.setColor(c);
		setLocation(loc);
	}

	public Block()
	{

	}

    public void act()
    {
    }
}