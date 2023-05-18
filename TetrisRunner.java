import info.gridworld.actor.ActorWorld;
import info.gridworld.actor.Actor;
import info.gridworld.actor.Rock;
import info.gridworld.actor.Bug;
import info.gridworld.grid.Location;
import info.gridworld.grid.BoundedGrid;
import info.gridworld.grid.*;
import info.gridworld.world.World;
import info.gridworld.gui.WorldFrame;

import java.awt.Color;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;

import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

public class TetrisRunner
{
	public static void highScores(int score, WorldFrame frame) throws Exception
	{
		try{
		ImageIcon icon = new ImageIcon("images\\Tetris.gif","logo");
		ImageIcon MrT = new ImageIcon("images\\MrT.gif","Treat Your Mother Right");
		System.out.println("Game Over");

		ArrayList<String> hsNames = new ArrayList<String>();
		ArrayList<Integer> hs = new ArrayList<Integer>();

		Scanner hsNamestxt = new Scanner(new File("text\\highscorenames.txt"));
		Scanner hstxt = new Scanner(new File("text\\highscore.txt"));

		while(hstxt.hasNextLine())
		{
			hsNames.add(hsNamestxt.nextLine());
			hs.add(Integer.parseInt(hstxt.nextLine()));
		}

		if((hs.size()<10))
		{
			if(JOptionPane.showConfirmDialog(null,"Your score is: "+score+"\nYou've made it to the High Score table!\nWould you like to add yourself to the High Score Table?","High Score!",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE,icon)==0)
			{
				frame.dispose();
				hs.add(score);
				int g = -1;
				String str = "";
				while(g<0)
				{
					str = (String)JOptionPane.showInputDialog(null,"Enter your Name: ","",JOptionPane.PLAIN_MESSAGE,icon,null,"");

					if(str.length()>0)
					g++;
				}
				hsNames.add(str);

				for(int i = 0 ; i < hs.size()-1; i++)
				{
					int imin = i;
					for(int j = i+1; j < hs.size(); j++)
					{
						if(hs.get(j)>hs.get(imin))
						imin=j;
					}
					Integer temp=hs.get(i);
					hs.set(i,hs.get(imin));
					hs.set(imin,temp);

					String tempS = hsNames.get(i);
					hsNames.set(i,hsNames.get(imin));
					hsNames.set(imin,tempS);
				}
			}
			frame.dispose();
			showHS(hs,hsNames);
		}
		else
		{
			Integer min = hs.get(0);
			for(Integer i : hs)
			{
				if(i<min)
				 min = i;
			}
		//	System.out.println(min);
			if((score>min))
			{
				if(JOptionPane.showConfirmDialog(null,"Your score is: "+score+"\nYou've made it to the High Score table!\nWould you like to add yourself to the High Score Table?\n\nMr. T says: \"Treat your mother right!\"","High Score!",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE,MrT)==0)
				{
					SoundEffect.sound1.stop();
					SoundEffect.MrTinit.playOnce();
					SoundEffect.MrT.play();
					frame.dispose();
					int index = hs.indexOf(min);
					hs.set(index,score);
					int g = -1;
					String str = "";
					while(g<0)
					{
						str = (String)JOptionPane.showInputDialog(null,"Enter your Name: ","",JOptionPane.PLAIN_MESSAGE,icon,null,"");

						if(str.length()>0)
						g++;
					}
					hsNames.set(index,str);

					for(int i = 0 ; i < hs.size()-1; i++)
					{
						int imin = i;
						for(int j = i+1; j < hs.size(); j++)
						{
							if(hs.get(j)>hs.get(imin))
							imin=j;
						}
						Integer temp=hs.get(i);
						hs.set(i,hs.get(imin));
						hs.set(imin,temp);

						String tempS = hsNames.get(i);
						hsNames.set(i,hsNames.get(imin));
						hsNames.set(imin,tempS);
				  }
			  }
			  frame.dispose();
			  showHS(hs,hsNames);
			}
			else
			{
				ImageIcon rick = new ImageIcon("images\\Rick.gif","rickroll");
				int yes = JOptionPane.showConfirmDialog(null,"Your score is: "+score+"\n Do you want to see the Tetris Gods who have beaten you?","Your Score",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE,rick);
				frame.dispose();
				if(yes==0)
				{
				showHS(hs,hsNames);
				}
			}
		}

		BufferedWriter writeName = new BufferedWriter(new FileWriter("text\\highscorenames.txt"));
		BufferedWriter writeHS = new BufferedWriter(new FileWriter("text\\highscore.txt"));

		for(int i = 0; i<hs.size();i++)
		{
			writeName.write(hsNames.get(i));
			writeName.newLine();
			writeName.flush();

			writeHS.write(Integer.toString(hs.get(i)));
			writeHS.newLine();
			writeHS.flush();
		}

		} catch(Exception e){}

	}

	public static void showHS(ArrayList<Integer> hs, ArrayList<String> hsNames)
	{
		String s = "";
		for(int i = 0; i<hs.size();i++)
		{
			s = s+"\n"+hsNames.get(i)+" "+hs.get(i);
		}
		ImageIcon icon = new ImageIcon("images\\Tetris.gif","logo");
		JOptionPane.showMessageDialog(null,s,"Tetris High Score Table",JOptionPane.INFORMATION_MESSAGE,icon);
	}

	public static void main(String args[])
	{
		ImageIcon icon = new ImageIcon("images\\Tetris.gif","logo");
		Object[] options = {"Play GridWorld Tetris!","Cancel"};
		int num = JOptionPane.showOptionDialog(null,"Welcome to GridWorld Tetris! \nTo begin the game, click \"Play GridWorld Tetris!\"","GridWorld Tetris",JOptionPane.OK_OPTION,JOptionPane.QUESTION_MESSAGE,icon,options,options[0]);//,"Play GridWorld Tetris!");

		if(num==0)
		{
		TetrisHelper run = new TetrisHelper();
		run.getFrame().startGame();
		}
	}

}