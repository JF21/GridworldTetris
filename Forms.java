import info.gridworld.actor.Actor;
import info.gridworld.grid.Location;
import info.gridworld.grid.Grid;
import info.gridworld.grid.*;

import java.util.ArrayList;
import java.awt.Color;

/*
*7 L - *
*      *** blue
*
*6 LR -   *
*       *** orange
*
*5 S - **
*     **  green
*
*4 Z -  **
*        **  red
*
*3 T      magenta
*
*2 Bar    cyan
*1 Square  yellow
*/

public class Forms extends Actor
{
	private static final Location[][] matLocs = {{new Location(2,0),new Location(1,0),new Location(0,0)},
												 {new Location(2,1),new Location(1,1),new Location(0,1)},
												 {new Location(2,2),new Location(1,2),new Location(0,2)}
												};
	private static final Location[] horizLocs = {new Location(2,0),new Location(2,1),new Location(2,2),new Location(2,3)};
	private static final Location[] vertLocs =  {new Location(0,2),new Location(1,2),new Location(2,2),new Location(3,2)};

	private int type;

	private boolean isHoriz = false;
	private boolean suspended = false;

	private ArrayList<Block> form = new ArrayList<Block>();
	private ArrayList<Block> furthestBlocks = new ArrayList<Block>();
	private ArrayList<Block> rightestBlocks = new ArrayList<Block>();
	private ArrayList<Block> leftestBlocks = new ArrayList<Block>();

	private Block furthestBlock = new Block(null,new Location(0,4));
	private Block leftestBlock = new Block(null,new Location(0,4));
	private Block rightestBlock = new Block(null,new Location(0,4));

	private Location keyLoc = new Location(0,4);

	static int number;
	public int num;

	public Forms(int type)
	{
		setColor(Color.BLACK);
		fillBlock(type);
		this.type = type;
		number++;
		num=number;
		//System.out.println(getGrid());
	//	act();
		//putSelfInGrid(getGrid(), new Location(0,4));
	}

	public ArrayList<Block> getForm()
	{
		return form;
	}

	public int getType()
	{
		return type;
	}

	public void fillBlock(int type)
	{
		switch(type)
		{
			case 1 : 	form.add(new Block(Color.YELLOW, new Location(0,4)));
						form.add(new Block(Color.YELLOW, new Location(0,5)));
						form.add(new Block(Color.YELLOW, new Location(-1,4)));
						form.add(new Block(Color.YELLOW, new Location(-1,5)));
						keyLoc = new Location(0,4);
					break;

			case 2 :    form.add(new Block(Color.CYAN, new Location(0,4)));
					    form.add(new Block(Color.CYAN, new Location(0,3)));
						form.add(new Block(Color.CYAN, new Location(0,5)));
						form.add(new Block(Color.CYAN, new Location(0,6)));
						keyLoc = new Location(0,4);
						isHoriz = true;
					break;

			case 3 :    form.add(new Block(Color.MAGENTA, new Location(0,4)));
						form.add(new Block(Color.MAGENTA, new Location(-1,3)));
						form.add(new Block(Color.MAGENTA, new Location(-1,4)));
						form.add(new Block(Color.MAGENTA, new Location(-1,5)));
						keyLoc = new Location(0,4);
					break;

			case 4 :    form.add(new Block(Color.RED, new Location(0,4)));
						form.add(new Block(Color.RED, new Location(0,3)));
						form.add(new Block(Color.RED, new Location(-1,4)));
						form.add(new Block(Color.RED, new Location(-1,5)));
						keyLoc = new Location(0,4);
					break;

			case 5 :    form.add(new Block(Color.GREEN, new Location(0,4)));
						form.add(new Block(Color.GREEN, new Location(0,5)));
						form.add(new Block(Color.GREEN, new Location(-1,4)));
						form.add(new Block(Color.GREEN, new Location(-1,3)));
						keyLoc = new Location(0,4);
					break;

			case 6 :    form.add(new Block(Color.ORANGE, new Location(0,6)));
						form.add(new Block(Color.ORANGE, new Location(-1,4)));
						form.add(new Block(Color.ORANGE, new Location(-1,5)));
						form.add(new Block(Color.ORANGE, new Location(-1,6)));
						keyLoc = new Location(0,5);
					break;

			case 7 :    form.add(new Block(Color.BLUE, new Location(0,4)));
						form.add(new Block(Color.BLUE, new Location(-1,5)));
						form.add(new Block(Color.BLUE, new Location(-1,6)));
						form.add(new Block(Color.BLUE, new Location(-1,4)));
						keyLoc = new Location(0,5);
					break;
		}
	}

	private void setFurthestBlocks()
	{
		furthestBlocks.clear();
		rightestBlocks.clear();
		leftestBlocks.clear();

		ArrayList<Integer> profile = new ArrayList<Integer>();
		ArrayList<Integer> vertProfile = new ArrayList<Integer>();

		for(Block b : form)
		{
			if(!profile.contains(b.getLocation().getCol()))
			{
				profile.add(b.getLocation().getCol());
			}

			if(!vertProfile.contains(b.getLocation().getRow()))
			{
				vertProfile.add(b.getLocation().getRow());
			}
		}

		for(Integer i : profile)
		{
			ArrayList<Block> check = new ArrayList<Block>();

			int max = -5;

			Block maxB = new Block();

			for(Block b : form)
			{
				if(b.getLocation().getCol() == i)
				{
					check.add(b);
				}
			}

			for(Block b : check)
			{
				if(b.getLocation().getRow() > max)
				{
					maxB = b;
					max = b.getLocation().getRow();
				}
			}
			furthestBlocks.add(maxB);
		}

		for(Integer i : vertProfile)
		{
			ArrayList<Block> check = new ArrayList<Block>();
			int max = -5;
			int min = 15;

			Block maxB = new Block();
			Block minB = new Block();

			for(Block b : form)
			{
				if(b.getLocation().getRow() == i)
				{
					check.add(b);
				}
			}

			for(Block b : check)
			{
				if(b.getLocation().getCol() > max)
				{
					maxB = b;
					max = b.getLocation().getCol();
				}

				if(b.getLocation().getCol() < min)
				{
					minB = b;
					min = b.getLocation().getCol();
				}
			}
			rightestBlocks.add(maxB);
			leftestBlocks.add(minB);
		}
	}

	public void putSelfInGrid(Grid<Actor> gr, Location loc)
	{
        super.putSelfInGrid(gr,loc);
        for(Block b : form)
        {
			if(gr.isValid(b.getLocation()))
			{
				b.putSelfInGrid(gr,b.getLocation());
			//	System.out.println(b.getGrid());
			}
		}
		setGrid(gr);
		setFurthestBlocks();
	//	System.out.println(getGrid());
    }

	public void moveSide(int dir)
	{
		if(!suspended)
		{
			ArrayList<Location> newBlock = new ArrayList<Location>();
			Color c = new Color(0,0,0);

			if(canStep(dir))
			{
				keyLoc = keyLoc.getAdjacentLocation(dir);

				for(Block b: form)
				{
					c = b.getColor();
					if(getGrid().isValid(b.getLocation()))
					{
					newBlock.add(b.getLocation().getAdjacentLocation(dir));
					b.removeSelfFromGrid();
					}
					else
					{
					newBlock.add(b.getLocation().getAdjacentLocation(dir));
					}
				}
				form.clear();

				//System.out.println(form.size());
				//System.out.println(newBlock+" "+form);

				for(Location l : newBlock)
				{
					if(getGrid().isValid(l))
					{
					Block b = new Block(c, l);
					//System.out.println(getGrid());
					b.putSelfInGrid(getGrid(), l);
					form.add(b);
					}
					else
					{
					form.add(new Block(c, l));
					}
				}
				setFurthestBlocks();
			}
		}
	}

	public int hardDrop()
	{
		if(!suspended)
		{
			Color c = new Color(0,0,0);;
			ArrayList<Location> newBlock = new ArrayList<Location>();
			int minDistance = 21;

			for(Block b : furthestBlocks)
			{
				c = b.getColor();
				int x = 0;

				Location next = b.getLocation().getAdjacentLocation(180);

				while(getGrid().isValid(next)&&(getGrid().get(next) == null))
				{
					x++;
					next = next.getAdjacentLocation(180);
				}

				if(x < minDistance)
				{
					minDistance = x;
				}
			}

			for(Block b: form)
			{
				int row = b.getLocation().getRow() + minDistance;
				int col = b.getLocation().getCol();
				if(getGrid().isValid(b.getLocation()))
				{
				newBlock.add(new Location(row, col));
				b.removeSelfFromGrid();
				}
				else
				{
				newBlock.add(new Location(row, col));
				}
			}
			form.clear();

			for(Location l : newBlock)
			{
				if(getGrid().isValid(l))
				{
				Block b = new Block(c, l);
				//System.out.println(getGrid());
				b.putSelfInGrid(getGrid(), l);
				form.add(b);
				}
				else
				{
				form.add(new Block(c, l));
				}
			}
			removeSelfFromGrid();

			return minDistance;
		}
		return 0;
	}

	public void turn()
	{
		if((!suspended)&&(type != 1))
		{
			Color c = new Color(0,0,0);

			ArrayList<Location> newBlock = new ArrayList<Location>();

			if(type != 2)
			{
				if(keyLoc.getCol()==0)
				{
					keyLoc = keyLoc.getAdjacentLocation(90);
				}

				if(keyLoc.getCol()==9)
				{
					keyLoc = keyLoc.getAdjacentLocation(270);
				}

				int dCol = keyLoc.getCol()-1;
				int dRow = keyLoc.getRow()-1;

				for(Block b : form)
				{
					c = b.getColor();

					int matCol = b.getLocation().getCol()-dCol;
					int matRow = b.getLocation().getRow()-dRow;

		//System.out.println(keyLoc+"\t call Mat coordinates:"+matRow+" "+matCol+"\t Distances:"+dRow+" "+dCol+"\t Current Loc:"+b.getLocation());
					Location l = matLocs[matRow][matCol];

					int col = l.getCol()+dCol;
					int row = l.getRow()+dRow;
		//System.out.println("Mat Loc: "+l+" new Loc: "+new Location(row,col));

				/*	if(getGrid().isValid(b.getLocation()))
					{
						b.removeSelfFromGrid();
					}*/
					newBlock.add(new Location(row,col));
				}
			}
			else
			{


				if(keyLoc.getCol()==0)
				{
					Location l = keyLoc.getAdjacentLocation(90);
					keyLoc = l.getAdjacentLocation(90);
				}

				if(keyLoc.getCol()==9)
				{
					keyLoc = keyLoc.getAdjacentLocation(270);
				}

				if(isHoriz)
				{
					int dCol = keyLoc.getCol()-1;
					int dRow = keyLoc.getRow()-2;

					Location newKey = vertLocs[1];
					keyLoc = new Location(newKey.getRow()+dRow,newKey.getCol()+dCol);

					for(int i = 0; i<form.size();i++)
					{
						c = form.get(i).getColor();

					/*	if(getGrid().isValid(form.get(i).getLocation()))
						{
							form.get(i).removeSelfFromGrid();
						}*/

					int row = vertLocs[i].getRow()+dRow;
					int col = vertLocs[i].getCol()+dCol;

					newBlock.add(new Location(row, col));
					}
					isHoriz = false;
				}
				else
				{
					int dCol = keyLoc.getCol()-2;
					int dRow = keyLoc.getRow()-1;

					Location newKey = horizLocs[1];
					keyLoc = new Location(newKey.getRow()+dRow,newKey.getCol()+dCol);

					for(int i = 0; i<form.size();i++)
					{
						c = form.get(i).getColor();
					/*	if(getGrid().isValid(form.get(i).getLocation()))
						{
							form.get(i).removeSelfFromGrid();
						}*/

					int row = horizLocs[i].getRow()+dRow;
					int col = horizLocs[i].getCol()+dCol;

					newBlock.add(new Location(row, col));
					}

					isHoriz = true;
				}

			}
	//*************************************************************************************************
			boolean shudReplace = true;
			ArrayList<Location> formLocations = new ArrayList<Location>();

			for(Block b : form)
			{
				formLocations.add(b.getLocation());
			}


			for(Location l : newBlock)
			{
				if(getGrid().isValid(l))
				{
					if((formLocations.indexOf(l) == -1) && (getGrid().get(l) != null))
					{
						shudReplace = false;
					}
				}
			}
//System.out.println(shudReplace);
			if(shudReplace)
			{

			for(Block b : form)
			{
				if(getGrid().isValid(b.getLocation()))
				b.removeSelfFromGrid();
			}

			form.clear();

			for(Location l : newBlock)
			{
				if(getGrid().isValid(l))
				{
					Block b = new Block(c, l);
					b.putSelfInGrid(getGrid(), l);
					form.add(b);
				}
				else
				{
					Block b = new Block(c, l);
					form.add(b);
				}
			}

			rearrange(true);
			setFurthestBlocks();
			}
		}
//System.out.println(form);
	}

	public void rearrange(boolean byRow)//remove this boolean parameter
	{
		for(int i = 0 ; i < form.size()-1; i++)
		{
			int imax = i;
			for(int j = i+1; j < form.size(); j++)
			{
				if(byRow)
				{
					if(form.get(j).getLocation().getRow()>form.get(imax).getLocation().getRow())
					imax=j;
				}
				else
				{
					if(form.get(j).getLocation().getCol()>form.get(imax).getLocation().getCol())
					imax=j;
				}
			}

			Block temp=form.get(i);
			form.set(i,form.get(imax));
			form.set(imax,temp);
		}
	}

	public boolean canStep(int dir)
	{
		ArrayList<Block> temp = new ArrayList<Block>();

		if(dir == 180)
		{
			temp = furthestBlocks;
		}
		else if(dir == 90)
		{
			temp = rightestBlocks;
		}
		else if(dir == 270)
		{
			temp = leftestBlocks;
		}

		for(Block b : temp)
		{
//System.out.println("Blocks in temp of canStep method:"+b);
			Location l = b.getLocation().getAdjacentLocation(dir);//null pointered

			if(!getGrid().isValid(l)||(l.getCol()>9)||(l.getCol()<0)||(l.getRow()>19)||!(getGrid().get(l) == null))
			{
				return false;
			}
		}

		return true;
	}

	public void hold()
	{
		for(Block b : form)
		{
			if(getGrid().isValid(b.getLocation()))
			{
				b.removeSelfFromGrid();
			}
		}

		form.clear();
		fillBlock(type);

		int dRow = 17-keyLoc.getRow();
		int dCol = 12-keyLoc.getCol();

		suspended = true;

		ArrayList<Block> newBlocks = new ArrayList<Block>();

		for(Block b : form)
		{
			int row = b.getLocation().getRow()+dRow;
			int col = b.getLocation().getCol()+dCol;
//System.out.println(row+" "+col+" "+dRow+" "+dCol+" "+keyLoc.getRow()+" "+keyLoc.getCol());
			Block c = new Block(b.getColor(),new Location(row,col));
			c.putSelfInGrid(getGrid(),c.getLocation());
			newBlocks.add(c);
		}

		form.clear();
		form.addAll(newBlocks);
	}

    public void act()
    {
			if(!suspended)
			{
				if(canStep(180))
				{
					moveSide(180);
				}
				else
				{
					removeSelfFromGrid();
				}
		    }
	}
}