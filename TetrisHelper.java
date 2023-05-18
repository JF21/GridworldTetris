import info.gridworld.actor.ActorWorld;
import info.gridworld.actor.Actor;
import info.gridworld.grid.*;
import info.gridworld.world.World;
import info.gridworld.gui.WorldFrame;

import java.util.ArrayList;
import java.lang.Thread;

import javax.swing.Timer;
import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.io.InputStream;
import java.util.Scanner;

import java.awt.Color;

public class TetrisHelper extends ActorWorld
{
		public final int NUM = 4;
		private Forms currentForm;
		private Forms holdForm;
		private int score = 0;

		private ArrayList<Integer> nextBlocks = new ArrayList<Integer>();

		private boolean justHeld = false;
		private boolean paused = false;
		/* Default constructor creates 20x10  board
		*/
		public TetrisHelper()
		{
				setGrid(new BoundedGrid<Actor>(20,20));
				Forms newForm = new Forms((int)(Math.random()*7)+1);
				add(new Location(0,16),newForm);
				currentForm = newForm;
				insertForm();

				SoundEffect.init();
      			SoundEffect.volume = SoundEffect.Volume.LOW;
				SoundEffect.sound1.play();
				show();
				getFrame().setSize(800,900);
				updateScore(0);
		}

		public void insertForm()
		{
			if(nextBlocks.size()==0)
			{
				for(int i = 0; i<3; i++)
				{
					nextBlocks.add((int)(Math.random()*7)+1);
				}
			}
			else
			{
				if(lastLineFilled())
				{
					gameDone();
				}
				else
				{
					Forms newForm = new Forms(nextBlocks.remove(0));
					add(new Location(0,16),newForm);
					currentForm = newForm;

					nextBlocks.add((int)(Math.random()*7)+1);
				}
			}

			for(int i = 0; i<14; i++)
			{
				for(int j = 11; j<15; j++)
				{
					if(getGrid().get(new Location(i,j)) != null)
					{
						getGrid().get(new Location(i,j)).removeSelfFromGrid();
					}
				}
			}

			for(int i = 0; i<nextBlocks.size(); i++)
			{
				drawBlock(nextBlocks.get(i),i);
			}
		}

		public void changeSpeed()
		{

			if((score/20 + 50) < 730)
				{
				int delay = score/20 + 50;
				getFrame().getControl().setTimer(800-delay);
			}
		}

		public void step()
		{
			super.step();

			if(noForms())
			{
				if(lastLineFilled())
				{
					gameDone();
				}
				else
				{
					insertForm();
					justHeld = false;
				}
				clearRows();
			}
		}

		public void updateScore(int lines)
		{
			setMessage("");
			switch(lines)
			{
				case 0 : setMessage("Current Score: "+score);
						break;

				case 1 : score+=100;
						setMessage("Current Score: "+score);
						changeSpeed();
						break;

				case 2 : score+=300;
						setMessage("Current Score: "+score+" \nDouble!");
						changeSpeed();
						SoundEffect.sound2.playOnce();
						break;
				case 3 : score+=500;
						setMessage("Current Score: "+score+" \nTriple!");
						changeSpeed();
						SoundEffect.sound3.playOnce();
						break;
				case 4 : score+=800;
						setMessage("Current Score: "+score+" \nTetris!");
						changeSpeed();
						SoundEffect.sound4.playOnce();
						break;
			}
		}

		public void clearRows()
		{
			ArrayList<Location> blockLocs = getGrid().getOccupiedLocations();
			ArrayList<Block> blocks = new ArrayList<Block>();

			blockLocs.remove(currentForm.getLocation());

			for(Block b : currentForm.getForm())
			{
				blockLocs.remove(b.getLocation());
			}

			for(int i = blockLocs.size()-1; i>=0; i--)
			{
				Location l = blockLocs.get(i);

				if(getGrid().get(l) instanceof Block)
				{
					blocks.add((Block)(getGrid().get(l)));
				}
			}

			ArrayList<Integer> rtd = new ArrayList<Integer>();

			for(int i = 0; i<20; i++)
			{
				boolean full = true;
				for(int j = 0; j<10; j++)
				{
					if(getGrid().get(new Location(i,j)) == null)
					{
						full = false;
					}
				}

				if(full)
				{
					rtd.add(i);
				}
			}
//System.out.println(rtd);
			for(Integer i : rtd)
			{
				for(int j = 0; j<10; j++)
				{
					Block b = (Block) getGrid().get(new Location(i,j));
					b.removeSelfFromGrid();
					blocks.remove(b);
				}
//System.out.println("Occupied Locs of clear Method: "+blocks);
				for(Block b : blocks)
				{
					if((b.getLocation().getRow()<i) && (b.getLocation().getCol()<10))//null pointer error
					{
						Location newLocation = b.getLocation().getAdjacentLocation(180);
						b.moveTo(newLocation);
					}
				}
			}

			updateScore(rtd.size());
		}

		public boolean noForms()
		{
			ArrayList<Location> list = getGrid().getOccupiedLocations();

			for(Location l : list)
			{
				if(getGrid().get(l) instanceof Forms)
				{
					if(!getGrid().get(l).getLocation().equals(new Location(1,16)))
					{
						return false;
					}
				}
			}

			return true;
		}

	    public boolean keyPressed(String description, Location loc)
	    {
		   if(description.equals("P"))
		   {
			   if(paused)
			   {
				   paused = false;
				   getFrame().getControl().run();
			   }
			   else
			   {
				   paused = true;
				   getFrame().getControl().stop();
			   }
		   }

		   if(!paused)
		   {
			   if(description.equals("RIGHT")&&!(noForms()))
			   {
				   currentForm.moveSide(90);
	//System.out.println("Right");
			   }

			   if (description.equals("LEFT")&&!(noForms()))
			   {
				   currentForm.moveSide(270);
	//System.out.println("Left");
			   }

			   if (description.equals("DOWN")&&!(noForms()))
			   {
				   if(noForms())
				   {
					   if(lastLineFilled())
					   {
						    gameDone();
					   }
					   else
					   {
							insertForm();
					    	justHeld = false;
					   }
					   clearRows();
				   }
				   else
				   {
						currentForm.act();
						score++;
						changeSpeed();
						updateScore(0);
				   }
	//System.out.println("Soft Drop");
			   }

			   if(description.equals("SPACE")&&!(noForms()))
			   {
				   clearRows();
				   currentForm.act();
				   score+= currentForm.hardDrop()*2;
				   changeSpeed();
					updateScore(0);

				   if(noForms())
				   {
					   if(lastLineFilled())
					   {
							gameDone();
					   }
					   else
					   {
						   insertForm();
						   justHeld = false;
					   }
					    clearRows();
					}
	//System.out.println("Hard Drop");
			   }

			   if(description.equals("UP")&&!(noForms()))
			   {
				   //currentForm.act();
				   currentForm.turn();
	//System.out.println("Turn");
			   }

			   if(description.equals("C")&&!(noForms()))
			   {
				   if(!justHeld)
				   {
					   if(holdForm == null)
					   {
						currentForm.moveTo(new Location(1,16));
						currentForm.hold();
						holdForm = currentForm;

						insertForm();

						justHeld = true;
					   }
					   else
					   {
						currentForm.hold();

						int type = holdForm.getType();

						for(Block b : holdForm.getForm())
						{
							if(!(b.getLocation() == null)&&getGrid().isValid(b.getLocation()))
							{
								b.removeSelfFromGrid();
							}
						}

						holdForm = currentForm;

						Forms newForm = new Forms(type);
						add(new Location(0,16),newForm);
						currentForm = newForm;

						justHeld = true;
					   }
					}
	//System.out.println("Hold");
			   }
		   }
		   return true;
    	}

		public boolean lastLineFilled()
		{
			for(int i = 0; i<10; i++)
			{
				if(getGrid().get(new Location(0,i)) != null)
				{
					return true;
				}
			}

			return false;
		}

	public void drawBlock(int type, int order)
	{
			ArrayList<Block> form = new ArrayList<Block>();

			switch(type)
			{
				case 1 : 	form.add(new Block(Color.YELLOW, new Location(0,4)));
							form.add(new Block(Color.YELLOW, new Location(0,5)));
							form.add(new Block(Color.YELLOW, new Location(-1,4)));
							form.add(new Block(Color.YELLOW, new Location(-1,5)));
						break;

				case 2 :    form.add(new Block(Color.CYAN, new Location(0,4)));
							form.add(new Block(Color.CYAN, new Location(0,3)));
							form.add(new Block(Color.CYAN, new Location(0,5)));
							form.add(new Block(Color.CYAN, new Location(0,6)));
						break;

				case 3 :    form.add(new Block(Color.MAGENTA, new Location(0,4)));
							form.add(new Block(Color.MAGENTA, new Location(-1,3)));
							form.add(new Block(Color.MAGENTA, new Location(-1,4)));
							form.add(new Block(Color.MAGENTA, new Location(-1,5)));
						break;

				case 4 :    form.add(new Block(Color.RED, new Location(0,4)));
							form.add(new Block(Color.RED, new Location(0,3)));
							form.add(new Block(Color.RED, new Location(-1,4)));
							form.add(new Block(Color.RED, new Location(-1,5)));
						break;

				case 5 :    form.add(new Block(Color.GREEN, new Location(0,4)));
							form.add(new Block(Color.GREEN, new Location(0,5)));
							form.add(new Block(Color.GREEN, new Location(-1,4)));
							form.add(new Block(Color.GREEN, new Location(-1,3)));
						break;

				case 6 :    form.add(new Block(Color.ORANGE, new Location(0,6)));
							form.add(new Block(Color.ORANGE, new Location(-1,4)));
							form.add(new Block(Color.ORANGE, new Location(-1,5)));
							form.add(new Block(Color.ORANGE, new Location(-1,6)));
						break;

				case 7 :    form.add(new Block(Color.BLUE, new Location(0,4)));
							form.add(new Block(Color.BLUE, new Location(-1,5)));
							form.add(new Block(Color.BLUE, new Location(-1,6)));
							form.add(new Block(Color.BLUE, new Location(-1,4)));
						break;
			}

			int dCol=0;
			int dRow=0;

			switch(order)
			{
				case 0 : dRow = 2;
						 dCol = 8;
						break;

				case 1 : dRow = 7;
						 dCol = 8;
						break;

				case 2 : dRow = 12;
						 dCol = 8;
						break;
			}

			for(Block b : form)
			{
				int row = b.getLocation().getRow()+dRow;
				int col = b.getLocation().getCol()+dCol;

				b.setLocation(new Location(row,col));
				b.putSelfInGrid(getGrid(),b.getLocation());
			}

		}

		public void gameDone()
		{
			getFrame().getControl().stop();
			try{
			TetrisRunner.highScores(score,getFrame());}catch(Exception e){}
		}
}