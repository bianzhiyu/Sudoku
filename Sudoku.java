package sudoku;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class al_run implements ActionListener
{
	private Sudoku s;
	public al_run(Sudoku s)
	{
		this.s=s;
	}
	@Override
	public void actionPerformed(ActionEvent e) throws NumberFormatException
	{
		try
		{
			s.transformNums();
		}
		catch(NumberFormatException er)
		{
			s.showIllegalInput();
			//throw er;
			return;
		}
		
		boolean legal=s.initializeFlags();
		
		if (!legal)
		{
			s.showIllegalInput();
		}
		else
		{
			boolean suc=s.searchAns(0,0);
			if (suc)
			{
				s.paintNums();
			}
			else
			{
				s.showNoAnswer();
			}
		}
	}
}

public class Sudoku {
	private JFrame frame;
	private JPanel panels[];
	private JLabel ans[][],status;
	private JTextField ques[][];
	private JButton run,clc;
	//to make a window
	final static int windowwidth=450;
	final static int windowheight=950;
	//row=19,column=9
	final static int block[][]={{0,0,0,1,1,1,2,2,2},
								{0,0,0,1,1,1,2,2,2},
								{0,0,0,1,1,1,2,2,2},
								{3,3,3,4,4,4,5,5,5},
								{3,3,3,4,4,4,5,5,5},
								{3,3,3,4,4,4,5,5,5},
								{6,6,6,7,7,7,8,8,8},
								{6,6,6,7,7,7,8,8,8},
								{6,6,6,7,7,7,8,8,8}};
	//
	private int [][] nums;
	private boolean [][]given;
	private boolean[][] fgrow,fgcol,fgblk;
	//inner variables
	
	public Sudoku()
	{
		nums=new int[9][];
		for (int i=0;i<9;i++)
		{
			nums[i]=new int[9];
		}
		fgrow=new boolean[9][];
		fgcol=new boolean[9][];
		fgblk=new boolean[9][];
		for (int i=0;i<9;i++)
		{
			fgrow[i]=new boolean[9];
			fgcol[i]=new boolean[9];
			fgblk[i]=new boolean[9];
		}
		given=new boolean[9][0];
		for (int i=0;i<9;i++)
		{
			given[i]=new boolean[9];
		}
		
		frame=new JFrame("Sudoku");
		Container JCP=frame.getContentPane();
		JCP.setLayout(null);
		
		panels=new JPanel[3];
		
		panels[0]=new JPanel(null);
		panels[1]=new JPanel(null);
		panels[2]=new JPanel(null);
		
		panels[0].setBounds(0, 0, windowwidth, windowheight/19*9);
		panels[1].setBounds(0, windowheight/19*9, windowwidth, windowheight/19);
		panels[2].setBounds(0, windowheight/19*10, windowwidth, windowheight/19*9);
		
		panels[0].setBackground(new Color(0xFF,0xF0,0xF0));
		panels[1].setBackground(new Color(0xF0,0xF0,0xFF));
		panels[2].setBackground(new Color(0xF0,0xF0,0xF0));
		
		for (int i=0;i<3;i++)
		{
			JCP.add(panels[i]);
		}
		
		Font ft=new Font("Arial", Font.BOLD, 25);
		
		ques=new JTextField[9][];
		for (int i=0;i<9;i++)
		{
			ques[i]=new JTextField[9];	
			for (int j=0;j<9;j++)
			{
				
				ques[i][j]=new JTextField("");
				ques[i][j].setFont(ft);
				ques[i][j].setBounds(j*windowwidth/9, i*windowheight/19, windowwidth/9, windowheight/19);
				panels[0].add(ques[i][j]);
			}
		}
		
		run=new JButton("Calc");
		run.setFont(ft);
		run.setForeground(new Color(0xFF,0x00,0x00));
		run.setBounds(0, 0, windowwidth/3, windowheight/19);
		run.addActionListener(new al_run(this));
		status=new JLabel();
		status.setFont(ft);
		status.setBounds(windowwidth/3, 0, windowwidth/3, windowheight/19);
		clc=new JButton("Clear");
		clc.setFont(ft);
		clc.setBounds(2*windowwidth/3, 0, windowwidth/3, windowheight/19);
		clc.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					clearAns();
				}
			}
		);
		
		panels[1].add(run);
		panels[1].add(status);
		panels[1].add(clc);
		
		ans=new JLabel[9][];
		for (int i=0;i<9;i++)
		{
			ans[i]=new JLabel[9];
			for (int j=0;j<9;j++)
			{
				ans[i][j]=new JLabel("");
				ans[i][j].setFont(ft);
				ans[i][j].setBounds(j*windowwidth/9, i*windowheight/19, windowwidth/9, windowheight/19);
				panels[2].add(ans[i][j]);
			}
		}
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(windowwidth+3*9,windowheight+4*19);
	}
	
	public void showIllegalInput()
	{
		JLabel tmp=new JLabel("Illegal input sudoku!");
		tmp.setFont(new Font("Arial", Font.BOLD, 25));
		JOptionPane.showMessageDialog(frame,tmp);
	}
	public void showNoAnswer()
	{
		JLabel tmp=new JLabel("No answer!");
		tmp.setFont(new Font("Arial", Font.BOLD, 25));
		JOptionPane.showMessageDialog(frame,tmp);
	}
	
	public void transformNums()throws NumberFormatException
	{
		for (int i=0;i<9;i++)
		{
			for (int j=0;j<9;j++)
			{
				try
				{
					String str=ques[i][j].getText();
					if (str.compareTo("")==0)
					{
						given[i][j]=false;
					}
					else
					{
						nums[i][j]=Integer.parseInt(str);
						if ((nums[i][j]<1)||(nums[i][j]>9))
						{
							given[i][j]=false;
							throw(new NumberFormatException("Value outof 0-9 in row "+i+" column "+j));
						}
						given[i][j]=true;
					}
					
				}
				catch(NumberFormatException e)
				{
					given[i][j]=false;
					System.out.println("The intput info of row "+i+" column "+j+" is of wrong format.");
					throw(e);
				}
			}
		}
	}
	
	public boolean initializeFlags()
	{
		for (int i=0;i<9;i++)
		{
			for (int j=0;j<9;j++)
			{
				fgrow[i][j]=false;
				fgcol[i][j]=false;
				fgblk[i][j]=false;
			}
		}
		for (int i=0;i<9;i++)
		{
			for (int j=0;j<9;j++)
			{
				if (given[i][j])
				{
					if (fgrow[i][nums[i][j]-1]||fgcol[j][nums[i][j]-1]||fgblk[block[i][j]][nums[i][j]-1])
					{
						return false;
					}
					fgrow[i][nums[i][j]-1]=true;
					fgcol[j][nums[i][j]-1]=true;
					fgblk[block[i][j]][nums[i][j]-1]=true;
				}
			}
		}
		return true;
	}
	
	public boolean searchAns(int x,int y)
	{
		if (y==9)
		{
			y=0;
			x+=1;
		}
		if (x==9)
		{
			return true;
		}
		if (given[x][y])
		{
			return searchAns(x,y+1);
		}
		for (int i=0;i<9;i++)
		{
			if (!fgrow[x][i] && !fgcol[y][i] && !fgblk[block[x][y]][i])
			{
				fgrow[x][i]=true;
				fgcol[y][i]=true;
				fgblk[block[x][y]][i]=true;
				nums[x][y]=i+1;
				if (searchAns(x,y+1))
				{
					return true;
				}
				fgrow[x][i]=false;
				fgcol[y][i]=false;
				fgblk[block[x][y]][i]=false;
			}
		}
		return false;
	}
	
	public void clearAns()
	{
		Color cr=new Color(0xF0,0xF0,0xF0);
		for (int i=0;i<9;i++)
		{
			for (int j=0;j<9;j++)
			{
				ans[i][j].setText("");
				ans[i][j].setForeground(cr);
			}
		}
	}
	
	public void paintNums()
	{
		for (int i=0;i<9;i++)
		{
			for (int j=0;j<9;j++)
			{
				ans[i][j].setText(Integer.toString(nums[i][j]));
				if (given[i][j])
				{
					ans[i][j].setForeground(Color.RED);
				}
				else
				{
					ans[i][j].setForeground(Color.BLACK);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		Sudoku t=new Sudoku();
		t.go();
	}
	public void go()
	{
		frame.setVisible(true);
	}
}
