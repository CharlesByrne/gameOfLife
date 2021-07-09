import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;


class ConwaysGOL_window extends JFrame{

	public static boolean[][] grid;
	public static boolean[][] gridBackup;
	
	public static final int MAX_SPEED = 70;
	public static final int MIN_SPEED = 10000;
	public static int gameSpeed = 500;
	
	static JButton[][] gridButtons;
	private static final String ID_PROPERTY_x = "ID_PROPERTY_x";
	private static final String ID_PROPERTY_y = "ID_PROPERTY_y";
	private static JLabel JLabel_1, JLabel_2, JLabel_3, JLabel_4;
	private static JLabel JLabel_1a, JLabel_2a, JLabel_3a;
    static JButton buttonPlay, buttonPaws, buttonExit, buttonClear, buttonOnce, buttonRandom, buttonReset;
    static JButton buttonLoad, buttonSave,gridXPlus, gridXMinus, gridYPlus, gridYMinus;
    
    static JTextField gameSpeedField;
    JFileChooser fileChooser;
    
public static int gridTotal = 0;
public static int generations = 0;
public static int gameChanges = 0;

public static int global_difX;
public static int global_difY;

	

// MAIN CODE: ######################################################

	public static int getTouches(int x, int y) {

		int touches = 0;

		// check top
		if (y>0) {
			if (x>0 && grid[y-1][x-1]) touches++;
			if (grid[y-1][x]) touches++;
			if (x<(grid[y-1].length-1) && grid[y-1][x+1]) touches++;
		}
		
		// check left
		if (x>0 && grid[y][x-1]) touches++;

		// check right;
		if (x<(grid[y].length-1) && grid[y][x+1]) touches++;
		
		// check bottom:
		if (y<grid.length-1) {
			if (x>0 && grid[y+1][x-1]) touches++;
			if (grid[y+1][x]) touches++;
			if (x<(grid[y+1].length-1) && grid[y+1][x+1]) touches++;
		}
		
		return touches;
	}
	
	public static void countGridTotal() {
		int total = 0;
		for(int y=0; y<grid.length; y++) {
			for (int x=0; x<grid[y].length; x++) {
				if (grid[y][x]) total++;
			}
		}
		gridTotal = total;
		System.out.println(gridTotal);
	}
	
	public static void generate() {
		boolean[][] newGrid = copyGrid(grid);
		int touches;
		int changes = 0;
		for(int y=0; y<grid.length; y++) {
			for (int x=0; x<grid[y].length; x++) {
				touches = getTouches(x, y);
				if (grid[y][x]) { // alive
					if ( touches<2 || touches > 3) {
						newGrid[y][x] = false;
						changes++;				// when 0, game over 
					}
				} else {	// not alive
					if (touches==3) {
						newGrid[y][x] = true;
						changes++;
					}
				}
			}
		}
		grid = copyGrid(newGrid);
		gameChanges = changes;
	}
	
	
	// ######################################################

	
	public static void stringToGrid( String[] strGrid ) {
		System.out.println(strGrid[0].length());
		grid = new boolean[strGrid.length][strGrid[0].length()];
		gridBackup = new boolean[strGrid.length][strGrid[0].length()];
		for(int y=0; y<grid.length; y++) {
			for (int x=0; x<grid[y].length; x++) {
				grid[y][x] = (strGrid[y].charAt(x) == '1') ? true : false;
			}
		}
	}
	
	public static String[] gridToString() {
		
		String[] strGrid = new String[grid.length]; 
		
		for(int y=0; y<grid.length; y++) {
			strGrid[y]="";
			for (int x=0; x<grid[y].length; x++) {
				strGrid[y] += (grid[y][x]) ? '1' : '0';
			}
		}
		return strGrid;
	}
	
	public static void printGrid( ) {
		String st = "";
		//System.out.println(grid.length + " x " + grid[0].length);
		for(int y=0; y<grid.length; y++) {
			st = "";
			for (int x=0; x<grid[0].length; x++) {
				st += (grid[y][x]) ? "#" : "-";
			}
			System.out.println(st);
		}
	}
	
	public static boolean[][] copyGrid(boolean[][] newGrid) {
		int total = 0;
		boolean[][] gridCopy = new boolean[newGrid.length][newGrid[0].length];
		for(int y=0; y<newGrid.length; y++) {
			for (int x=0; x<newGrid[y].length; x++) {
				gridCopy[y][x] = newGrid[y][x];
				if (newGrid[y][x]) total++;
			}
		}
		gridTotal = total;
		return gridCopy;
	}
	

	
	public static void runIT() {
		//while (true) {
		generate();
			showBoard();
//			printGrid();
			try {
				Thread.sleep(100);				
			} catch (InterruptedException ex) {
				
			}

	//	}
	}
	
	public static void setJLabels() {
	      JLabel_1.setText(""+generations);
	      JLabel_2.setText(""+gridTotal);
	      JLabel_3.setText(""+gameChanges);
	}
	
	public static void goOnce() {
		if (generations == 0 ) {
			gridBackup = copyGrid(grid);
		}
		generate();
		generations++;
		setJLabels();
		showBoard();
		if (gameChanges == 0) {
			timer.stop();
			generations--;
			JLabel_4.setText("     DONE!");
			setJLabels();
		}		
	}
	
    static Timer timer = new Timer(gameSpeed, new ActionListener() { 

        public void actionPerformed(ActionEvent e) {

        	goOnce();
        }
    });
	
    private final ActionListener gridListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {

	            JComponent source = (JComponent)e.getSource();
	            Integer x = (Integer) source.getClientProperty(ID_PROPERTY_x);
	            Integer y = (Integer) source.getClientProperty(ID_PROPERTY_y);
	        	timer.stop();
				JLabel_4.setText("     Go...");
	            generations = 0;
	            grid[y][x] = !(grid[y][x]);
	            gridTotal += (grid[y][x]) ? 1 : -1;
				setJLabels();
	            setCell(x,y);
        }
    };
    
    public static void setCell(int x, int y) {
 
    	if (grid[y][x]) {
            gridButtons[y][x].setOpaque(true);

            gridButtons[y][x].setBackground(Color.BLUE);
            //gridButtons[y][x].setForeground(Color.WHITE);
           // gridButtons[y][x].setText("X");
    	} else {
            gridButtons[y][x].setOpaque(false);
            gridButtons[y][x].setBackground(null);
            //gridButtons[y][x].setForeground(Color.BLACK);
            //gridButtons[y][x].setText("O"); 		
    	}
    	//gridButtons[y][x].setText(x+":"+y);

    } // setCell()

    private final ActionListener clearListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	timer.stop();
			JLabel_4.setText("Go...");
            for (int y=0; y<grid.length; y++) {
            	for (int x=0; x<grid[0].length; x++) {
            		grid[y][x] = false;
            		setCell(x,y);
            	}
            }
            generations = 0;
            gridTotal = 0;
            gameChanges = 0;
			setJLabels();
        }
    };
    
   
    public static void go_random(boolean show) {
    	Random r = new Random();
    	int total = 0;
        for (int y=0; y<grid.length; y++) {
        	for (int x=0; x<grid[0].length; x++) {
        		grid[y][x] = (boolean)(r.nextInt(2)==0);
        		if (grid[y][x]) total++;
        		if (show) setCell(x,y);
        	}
        }
        generations = 0;
        gridTotal = total;
        gameChanges = 0;
        if (show) {
			JLabel_4.setText("Go...");
			setJLabels();
        }
        
    } // END: go_random()
    
    
    public static void go_reset(boolean show) {
    	int total = 0;
		if (generations > 0 ) {
			grid = copyGrid(gridBackup);
		}
        for (int y=0; y<grid.length; y++) {
        	for (int x=0; x<grid[0].length; x++) {
        		if (grid[y][x]) total++;
        		if (show) setCell(x,y);
        	}
        }
        generations = 0;
        gridTotal = total;
        gameChanges = 0;
        if (show) {
			JLabel_4.setText("Go...");
			setJLabels();
        }
        
    } // END: go_random()
    
    private final ActionListener randomListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	timer.stop();
        	go_random(true);
        }
    };
    
    
    private final ActionListener resetListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	timer.stop();
        	go_reset(true);
        }
    };
    
    private final ActionListener loadListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	timer.stop();
        	
        	int result = fileChooser.showOpenDialog(ConwaysGOL_window.this);
        	if (result == JFileChooser.APPROVE_OPTION) {
        	    File selectedFile = fileChooser.getSelectedFile();
            	go_Load(selectedFile.getAbsolutePath(), true);
        	}
        	
        }
    };
    
    private final ActionListener saveListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	timer.stop();
        	
        	int result = fileChooser.showSaveDialog(ConwaysGOL_window.this);
        	if (result == JFileChooser.APPROVE_OPTION) {
        	    File selectedFile = fileChooser.getSelectedFile();
        	    go_Save(selectedFile.getAbsolutePath());
        	}
        }
    };
    
    private final ActionListener playListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	if (generations == 0) {
    			gridBackup = copyGrid(grid);        		
        	}

        	timer.start();
        }
    };
    
    private final ActionListener gameSpeedListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	int newSpeed = Integer.parseInt(gameSpeedField.getText());
        	System.out.println(newSpeed);
        	if (newSpeed>=MAX_SPEED && newSpeed<MIN_SPEED) {
        		gameSpeed = newSpeed;
        	    timer = new Timer(gameSpeed, new ActionListener() { 

        	        public void actionPerformed(ActionEvent e) {

        	        	goOnce();
        	        }
        	    });
        		timer.start();
        	}
        }
    };
    
    private final ActionListener onceListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	timer.stop();
        	goOnce();
        }
    };
    
    private final ActionListener pauseListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	timer.stop();
        }
    };
    
    void changeGridSize(int xChange, int yChange) {

    	//printGrid();
    	int newX = grid[0].length + xChange;
    	int newY = grid.length + yChange;

    	// avoid a zero length array...
    	if ( newX < 0 || newY < 0) return;

    	boolean[][] gridCopy = new boolean[newY][newX];
    	
    	// avoid out of bounds:
    	
    	int smallestX = (newX < grid[0].length) ? newX : grid[0].length;
    	int smallestY = (newY < grid.length) ? newY : grid.length;
    	System.out.println("CHANGE: " + newX + ", " + newY);
    	System.out.println("SMALLEST: " + smallestX + ", " + smallestY);

		for(int y=0; y < smallestY; y++) {
			for (int x=0; x < smallestX; x++) {
				gridCopy[y][x] = grid[y][x];
			}
		}
		grid = copyGrid(gridCopy);

		refreshGrid();
		//printGrid();
    }
    
    // #################################################################
    // Must be a better way than this repetition but for speed....
    
    private final ActionListener gridChangeXM = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	timer.stop();
        	System.out.println("reduce x");
        	//printGrid();
        	changeGridSize(-1, 0);
        }
    };
    
    private final ActionListener gridChangeXP = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	timer.stop();
        	System.out.println("increase x");
        	changeGridSize(1, 0);
        }
    };
    
    private final ActionListener gridChangeYM = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	timer.stop();
        	System.out.println("reduce y");
        	changeGridSize(0, -1);
        }
    };
    
    private final ActionListener gridChangeYP = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	timer.stop();
        	System.out.println("increase y");
        	changeGridSize(0, 1);
        }
    };
    
    // #####################################################
    
    public static void showBoard() {
        for (int y=0; y<grid.length; y++) {
        	for (int x=0; x<grid[0].length; x++) {
        		setCell(x,y);
        	}
        }
    }
        	
	
    public JPanel buildgridPanel(int xCells, int yCells) {
  
        JPanel panel = new JPanel();
   
		gridButtons = new JButton[yCells][xCells];
        
        panel.setLayout(new GridLayout(yCells, xCells));
        for (int y=0; y<grid.length; y++) {
        	for (int x=0; x<grid[0].length; x++) {
            	gridButtons[y][x] = new JButton("");
                gridButtons[y][x].putClientProperty(ID_PROPERTY_x, x);
                gridButtons[y][x].putClientProperty(ID_PROPERTY_y, y);
                gridButtons[y][x].addActionListener(gridListener);
        		setCell(x,y);
        		
        		panel.add(gridButtons[y][x]);
        	}
        }
        
        
        return panel;

    }
    
    
    public JPanel buildButtons() {
    	JPanel panel = new JPanel();

        gridXMinus = new JButton("-");
        gridXMinus.addActionListener(gridChangeXM);
        panel.add(gridXMinus);
        gridXPlus = new JButton("+");
        gridXPlus.addActionListener(gridChangeXP);
        panel.add(gridXPlus);
        gridYMinus = new JButton("-");
        gridYMinus.addActionListener(gridChangeYM);
        panel.add(gridYMinus);
        gridYPlus = new JButton("+");
        gridYPlus.addActionListener(gridChangeYP);
        panel.add(gridYPlus);
    	
        buttonSave = new JButton("Save");
        buttonSave.addActionListener(saveListener);
        panel.add(buttonSave);
        
        buttonLoad = new JButton("Load");
        buttonLoad.addActionListener(loadListener);
        panel.add(buttonLoad);
    	
      gameSpeedField = new JTextField(""+gameSpeed, 3);
      panel.add(gameSpeedField);
      gameSpeedField.addActionListener(gameSpeedListener);
      
   // Listen for changes in the text
      
      buttonPlay = new JButton("PLAY");
      buttonPlay.addActionListener(playListener);
      panel.add(buttonPlay);
      
      buttonPaws = new JButton("PAUSE");
      buttonPaws.addActionListener(pauseListener);
      panel.add(buttonPaws);
      
      buttonOnce = new JButton("ONCE");
      buttonOnce.addActionListener(onceListener);
      panel.add(buttonOnce);
      
      buttonClear = new JButton("CLEAR");
      buttonClear.addActionListener(clearListener);
      panel.add(buttonClear);
      
      buttonRandom = new JButton("Random");
      buttonRandom.addActionListener(randomListener);
      panel.add(buttonRandom);
      
      buttonReset = new JButton("Undo");
      buttonReset.addActionListener(resetListener);
      panel.add(buttonReset);
            
      buttonExit = new JButton("EXIT");
      buttonExit.setMnemonic(KeyEvent.VK_C);
      buttonExit.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              System.exit(0);
          }
      });
      panel.add(buttonExit);
      
      JLabel_1a = new JLabel("     Gen: ");
      JLabel_1a.setFont(new Font("Dialog", Font.BOLD, 18));
      panel.add(JLabel_1a);
      
      JLabel_1 = new JLabel(""+generations);
      JLabel_1.setFont(new Font("Dialog", Font.BOLD, 18));
      panel.add(JLabel_1);

      JLabel_2a = new JLabel("     Alive: ");
      JLabel_2a.setFont(new Font("Dialog", Font.BOLD, 18));
      panel.add(JLabel_2a);
      
      JLabel_2 = new JLabel(""+gridTotal);
      JLabel_2.setFont(new Font("Dialog", Font.BOLD, 18));
      panel.add(JLabel_2);
      
      JLabel_3a = new JLabel("     +-: ");
      JLabel_3a.setFont(new Font("Dialog", Font.BOLD, 18));
      panel.add(JLabel_3a);
      
      JLabel_3 = new JLabel(""+gameChanges);
      JLabel_3.setFont(new Font("Dialog", Font.BOLD, 18));
      panel.add(JLabel_3);
      
      JLabel_4 = new JLabel("     Go...");
      JLabel_4.setFont(new Font("Dialog", Font.BOLD, 18));
      panel.add(JLabel_4);

      
      return panel;
    }
    
    
    
    // ##############################################
    
    
    void doCounstructorStuff(int xCells, int yCells) {
        JPanel primaryPanel = new JPanel(new BorderLayout());
        JPanel gridPanel = buildgridPanel(xCells, yCells);
        JPanel secondaryPanel = buildButtons();
        primaryPanel.add(secondaryPanel, BorderLayout.NORTH);
        primaryPanel.add(gridPanel, BorderLayout.CENTER);

        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(primaryPanel, BorderLayout.CENTER);

        setVisible(false);
//		try {
//			TimeUnit.SECONDS.sleep(1);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  

        setAlwaysOnTop(true);
        pack();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        
        
    }
    
    // CONSTRUCTOR: 
	
    public ConwaysGOL_window(int xCells, int yCells) {
    	
    	
        super("Conway's Game of Life");
    	fileChooser = new JFileChooser();
    	fileChooser.setMultiSelectionEnabled(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "txt files (*.txt)", "txt");
        fileChooser.setFileFilter(filter);
    	grid = new boolean[yCells][xCells];
        doCounstructorStuff(xCells, yCells);
    }
    private static String[] load_life(String filename) {
     	
        File aFile = new File(filename);     
        StringBuffer contents = new StringBuffer();
        BufferedReader input = null;
        
        try {
            input = new BufferedReader( new FileReader(aFile) );
            String line = null; 

            while (( line = input.readLine()) != null){
                contents.append(line);

                contents.append(System.getProperty("line.separator"));
            }
        } catch (FileNotFoundException ex){
            System.out.println("Can't find the file '" + filename + "' - are you sure the file is in this location: "+filename);
            
            return null;
        } catch (IOException ex){
            System.out.println("Input output exception while processing file");
            ex.printStackTrace();
        } finally{
            try {
                if (input!= null) {
                    input.close();
                }
            }catch (IOException ex){
                System.out.println("Input output exception while processing file");
                ex.printStackTrace();
            }
        }
        String[] array = contents.toString().split("\r\n"); 
        return array;
    }
    
    public void refreshGrid() {
		getContentPane().removeAll();

		doCounstructorStuff(grid[0].length,  grid.length);
    }
    
    void go_Save(String filename) {
    	String[] strGrid = gridToString();
        try {
            FileWriter myWriter = new FileWriter(filename);
            for (int i=0; i<strGrid.length; i++) {
                myWriter.write(strGrid[i]+"\r\n");            	
            }

            myWriter.close();
    		this.setTitle("Conway's Game of Life - " + filename);           
            System.out.println("Saved");
          } catch (IOException error) {
            System.out.println("File Write Error.");
            error.printStackTrace();
          }
    }
    

    
     void go_Load(String filename, boolean show) {

    	timer.stop();
		String[] strGrid = load_life(filename);
		
		this.setTitle("Conway's Game of Life - " + filename);
		
		int curGridXDIM = grid[0].length;
		int curGridYDIM = grid.length;
		System.out.println("X_DIM: " + strGrid[0].length());
		System.out.println("Y_DIM: " + strGrid.length);
		// Convert the string input into the boolean array:

		
		stringToGrid(strGrid);
		
		System.out.println("X_DIM_: " + grid[0].length);
		System.out.println("Y_DIM_: " + grid.length);
		
		if (curGridXDIM != grid[0].length || curGridYDIM != grid.length) {
			refreshGrid();
		}
		
		
    	int total = 0;
		gridBackup = copyGrid(grid);

        for (int y=0; y<grid.length; y++) {
        	for (int x=0; x<grid[0].length; x++) {
        		if (grid[y][x]) total++;
        		if (show) setCell(x,y);
        	}
        }
        generations = 0;
        gridTotal = total;
        gameChanges = 0;
        if (show) {
			JLabel_4.setText("Go...");
			setJLabels();
        }
        
    } // END: go_random()
    

}

public class ConwaysGOL {
	
	public static ConwaysGOL_window gridWindow;
	
    public static void drawGrid() {
    	gridWindow = new ConwaysGOL_window(20,20);
    }
	
    

	public static void main(String[] args) {


		String[] strGrid = new String[20];
		strGrid[ 0] = "00000000000000000000";
		strGrid[ 1] = "00000000000000000000";
		strGrid[ 2] = "00000000000000000000";
		strGrid[ 3] = "00000000000000000000";
		strGrid[ 4] = "00000000000000000000";
		strGrid[ 5] = "00000000000000000000";
		strGrid[ 6] = "00000000100000000000";
		strGrid[ 7] = "00000000100000000000";
		strGrid[ 8] = "00000000100000000000";
		strGrid[ 9] = "00000000000000000000";
		strGrid[10] = "00000000000000000000";
		strGrid[11] = "00000000000000000000";
		strGrid[12] = "00000000000000000000";
		strGrid[13] = "00000000000000000000";
		strGrid[14] = "00000000000000000000";
		strGrid[15] = "00000000000000000000";
		strGrid[16] = "00000000000000000000";
		strGrid[17] = "00000000000000000000";
		strGrid[18] = "00000000000000000000";
		strGrid[19] = "00000000000000000000";
		drawGrid();
	}

}