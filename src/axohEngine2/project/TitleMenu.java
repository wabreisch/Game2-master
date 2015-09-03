/****************************************************************************************************
 * @author Travis R. Dewitt
 * @version 1.0
 * Date: July 5, 2015
 * 
 * Title: Title Menu
 * Description: Create a title menu with a graphic and options to load/ssave/delete a file
 * 
 * TODO: Create option of deleting a file from the menu
 * 
 * This work is licensed under a Attribution-NonCommercial 4.0 International
 * CC BY-NC-ND license. http://creativecommons.org/licenses/by-nc/4.0/
 ****************************************************************************************************/
//Package
package axohEngine2.project;

//Imports
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.File;

import javax.swing.JFrame;

import axohEngine2.entities.AnimatedSprite;
import axohEngine2.entities.ImageEntity;

public class TitleMenu {
	
	/********************
	 * Variables
	 ********************/
	//files - An array of at most size 3 which contain all of the currently existing files
	//existingFiles - The File object which points to the initial directory location where the save will be located
	//location - Which slot is currently being highlighted by the user
	//_fileName - String which contains the character the user is typing on screen to be used as a file name
	//getName - Boolean that when true, set the system to collect keypresses and display them on screen for file names
	private String[] files;
	private File existingFiles;
	private int location;
	private String _fileName = "";
	private boolean getName = false;
	
	//_mainImage - The initial background image of the title screen
	//_secondary - The image which appear after choosing load game
	//titleArrow - The AnimatedSprite which indicates which option the user is currently hovering over
	//_option - A choice the player might make like load or newGame or delete
	private ImageEntity _mainImage;
	private ImageEntity _secondary;
	private AnimatedSprite _titleArrow;
	private OPTION _option;
	
	//Fonts to be used to display text, variouse ones for various uses
	private Font _simple;
	private Font _bold;
	private Font _bigBold;
	
	//SCREENWIDTH, SCREENHEIGHT - width and height of the game JFrame window in pixels
	private int SCREENWIDTH;
	private int SCREENHEIGHT;
	
	/*******************************************************************
	 * Constructor
	 * 
	 * @param mainImage - ImageEntity background
	 * @param secondary - ImageEntity load game background
	 * @param titleArrow - AnimatedSprite for currently selected option
	 * @param screenWidth - width of the window in pixels
	 * @param screenHeight - height of the window in pixels
	 * @param simple - The font to use for normal text
	 * @param bold - The font to use for bold text
	 * @param bigBold - The font to use for big loud remarks, very bold
	 *******************************************************************/
	public TitleMenu(ImageEntity mainImage, ImageEntity secondary,AnimatedSprite titleArrow, int screenWidth, int screenHeight, Font simple, Font bold, Font bigBold) {
		existingFiles = new File("C:/gamedata/saves/");
		_mainImage = mainImage;
		_secondary = secondary;
		_titleArrow = titleArrow;
		SCREENWIDTH = screenWidth;
		SCREENHEIGHT = screenHeight;
		_simple = simple;
		_bold = bold;
		_bigBold = bigBold;
		_option = OPTION.NONE;
	}
	
	/****************************************************************************
	 * Render the title screen and change what is being shown based on options the user chooses
	 * 
	 * @param frame - JFrame window where the images will be rendered
	 * @param g2d - Graphics2D object needed to render images
	 * @param titleX - x position of the arrow
	 * @param titleY - y position of the arrow
	 * @param titleX2 - x position of the arrow once a choice is made
	 * @param titleY2 - y position of the arrow once a choice has been made
	 ****************************************************************************/
	public void render(JFrame frame, Graphics2D g2d, int titleX, int titleY, int titleX2, int titleY2) {
		g2d.drawImage(_mainImage.getImage(), 0, 0, SCREENWIDTH, SCREENHEIGHT, frame);
		g2d.setColor(Color.BLACK);
		g2d.setFont(_bold);
		g2d.drawString("New Game", 660, 700);
		g2d.drawString("Load Game", 560, 800);
		g2d.setColor(Color.YELLOW);
		g2d.setFont(_bigBold);
		drawString(g2d, "The\n   Judgement", 500, 100);
		g2d.drawImage(_titleArrow.getImage(), titleX, titleY, _titleArrow.getSpriteSize(), _titleArrow.getSpriteSize(), frame);
		
		if(_option == OPTION.NEWGAME || _option == OPTION.LOADGAME){
			g2d.setColor(Color.BLACK);
			g2d.setFont(_simple);
			g2d.drawImage(_secondary.getImage(), 0, 0, SCREENWIDTH, SCREENHEIGHT, frame);
			if(files != null){
				for(int i = 0; i < files.length; i++){
					g2d.drawString(files[i], 540, 388 + i * 165);
				}
			}
			if(_option == OPTION.NEWGAME) {
				g2d.drawString("New Game", 620, 190); 
				g2d.drawString(_fileName, 540, 388 + location * 165);
			}
			if(_option == OPTION.LOADGAME) {
				g2d.drawString("Load Game", 620, 190); 
			}
			g2d.drawImage(_titleArrow.getImage(), titleX2, titleY2, _titleArrow.getSpriteSize(), _titleArrow.getSpriteSize(), frame);
		}
	}
	
	/******************************************************************
	 * Update the title screen variables from outside of this class
	 * 
	 * @param option - The new currently selected option
	 * @param location - The file which the arrow is pointing to
	 ******************************************************************/
	public void update(OPTION option, int location) {
		_option = option;
		files = existingFiles.list();
		this.location = location;
	}
	
	/*********************************************************************************
	 * This method updates the current filename variable and sorts out
	 * any non valid characters. This also makes sure the the file name
	 * is always 10 characters or less.
	 * 
	 * This method is best used when checking for whatever the user is 
	 * currently pressing on the keyboard, then converting that keycode 
	 * in to a char and passing that in to this method. 
	 * 
	 * @param currentChar - The character to be checked and added in to the file name
	 **********************************************************************************/
	public void setFileName(char currentChar) {
		if(currentChar == '\0') return;
		if(currentChar == '\b') return;
		if(currentChar == '\n') return;
		if(_fileName.length() < 11) _fileName += currentChar;
	}
	
	/******************************************************************************
	 * Method used to delete the last character when typing a file name (Backspace)
	 ******************************************************************************/
	public void deleteChar() {
		if(_fileName.length() > 0) _fileName = _fileName.substring(0, _fileName.length() - 1);
	}
	
	/*****************************************************************************
	 * Method which decides what happens when the enter key is pressed, what
	 * happens depends on many different variables. This method also
	 * returns the filename if it is over one, for loading purposes.
	 * 
	 * @return - Either an empty string or the filename being chosen
	 ******************************************************************************/
	public String enter() {
		if(_option == OPTION.NEWGAME) {
			getName = true;
			return "";
		}
		if(_option == OPTION.LOADGAME) {
			if(files != null){
				if(location <= files.length  - 1){
					if(files.length == 3) return (files[location]); 
					if(files.length == 2 && location <= 1) return (files[location]); 
					if(files.length == 1 && location == 0) return (files[location]); 
				}
			}
		}
		return "";
	}
	
	//Getters for _fileName, files(The array), and is the syetm is in getName state
	public String getFileName() { return _fileName; }
	public String[] files() { return files; }
	public boolean isGetName() { return getName; }
	
	//Setter for the boolean getName
	public void setGetName(boolean onOrOff) { getName = onOrOff; }
	
	/***************************************************************************************************
	 * A special drawString method which takes in a string to be displayed and constructs it in 
	 * special ways based on certain characters it may encounter, like '\n' which is the equivalent 
	 * of the enter key
	 * 
	 * @param g2d - Graphics2D object needed to display images
	 * @param text - String to check
	 * @param x - x position for the text to display
	 * @param y - y position for the text to display
	 ****************************************************************************************************/
	void drawString(Graphics2D g2d, String text, int x, int y) {
       for (String line : text.split("\n"))
           g2d.drawString(line, x, y += g2d.getFontMetrics().getHeight());
    }
}