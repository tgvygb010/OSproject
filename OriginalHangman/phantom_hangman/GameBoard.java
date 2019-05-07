package phantom_hangman;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Displays a Hangman game board to the screen for interaction with the player.
 * @author Jeff A.
 */
public class GameBoard extends JFrame 
{
    /**
     * The width of the GameBoard.
     */
    private final int WIDTH;
    
    /**
     * The height of the GameBoard.
     */
    private final int HEIGHT;
    
    /**
     * The maximum number of guesses before game over.
     */
    private final int MAX_INCORRECT;
    
    /**
     * The maximum password length permitted.
     */
    private final int MAX_PASSWORD_LENGTH;
    
    /**
     * The directory of the images of the hangman.
     */
    private final String HANGMAN_IMAGE_DIRECTORY;
    
    /**
     * The type of the images of the hangman.
     */
    private final String HANGMAN_IMAGE_TYPE;
    
    /**
     * The base (common) name of the images of the hangman (e.g. "hangman" for
     * "hangman_0.png, hangman_1.png, ...")
     */
    private final String HANGMAN_IMAGE_BASE_NAME;
    
    /**
     * The directory of the images of the letters.
     */
    private final String LETTER_IMAGE_DIRECTORY;
    
    /**
     * The type of the images of the letters.
     */
    private final String LETTER_IMAGE_TYPE;
    
    /**
     * The letter rack containing a the letters to be guessed.
     */
    private LetterRack gameRack;
    
    /**
     * The hangman image placeholder.
     */
    private Hangman gameHangman;
    
    /**
     * The number of incorrect guesses.
     */
    private int numIncorrect;
    
    /**
     * Display the password hidden as *'s, revealing each letter as it is
     * guessed
     */
    private JLabel correct;
    
    /**
     * Displays the number of incorrect guesses.
     */
    private JLabel incorrect;
    
    /**
     * The password to be guessed by the player.
     */
    private String password;
    
    /**
     * StringBuilder used to hide the password, revealing letters as they are
     * guessed by the player.
     */
    private StringBuilder passwordHidden;
    
    /**
     * The default constructor.
     */
    public GameBoard()
    {
        WIDTH = 500;
       HEIGHT = 500;
        MAX_INCORRECT = 6;
        MAX_PASSWORD_LENGTH = 10;
        
        // The default directory for the sample images is images/ and the 
        //     default image type is .png; ensure this directory is
        //     created in the project folder if the sample images are used.
        HANGMAN_IMAGE_DIRECTORY = LETTER_IMAGE_DIRECTORY = "src/images/";
        HANGMAN_IMAGE_TYPE = LETTER_IMAGE_TYPE = ".png";
        HANGMAN_IMAGE_BASE_NAME = "hangman";
        
        setTitle("Phantom Hangman");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        addCloseWindowListener();
        
        initialize();
    }
    
    /**
     * Initializes all elements of the GameBoard that must be refreshed upon
     * the start of a new game.
     */
    private void initialize()
    {        
        numIncorrect = 0;
        
        correct = new JLabel("Word: ");
        incorrect = new JLabel("Incorrect: " + numIncorrect);
        password = new String();
        passwordHidden = new StringBuilder();
        
        getPassword();
        addTextPanel();
        addLetterRack();
        addHangman();
        
        // set board slightly above middle of screen to prevent dialogs
        //     from blocking images
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - getSize().width / 2,
                dim.height / 2 - getSize().height / 2 - 200);
        setVisible(true);
    }
    
    /**
     * Prompts the user to confirm before quitting out of the window.
     */
    private void addCloseWindowListener()
    {
        // NOTE: Must be DO_NOTHING_ON_CLOSE for prompt to function correctly
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent we)
            {
                int prompt = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to quit?",
                        "Quit?", 
                        JOptionPane.YES_NO_OPTION);
                
                if (prompt == JOptionPane.YES_OPTION)
                    System.exit(0);
            }
        });
    }
    
    /**
     * Adds the correct and incorrect labels to the top of the GameBoard
     */
    private void addTextPanel()
    {
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(1,2));
        textPanel.add(correct);
        textPanel.add(incorrect);
        // use BorderLayout to set the components of the gameboard in
        //     "visually agreeable" locations
        add(textPanel, BorderLayout.NORTH);
    }
    
    /**
     * Adds the LetterRack to the bottom of the GameBoard and attaches
     * the LetterTile TileListeners to the LetterTiles.
     */
    private void addLetterRack()
    {
        gameRack = new LetterRack(password, 
                LETTER_IMAGE_DIRECTORY, 
                LETTER_IMAGE_TYPE);
        gameRack.attachListeners(new TileListener());
        add(gameRack, BorderLayout.SOUTH);
    }
    
    /**
     * Adds a panel that contains the hangman images to the middle of the
     * GameBoard.
     */
    private void addHangman()
    {
        JPanel hangmanPanel = new JPanel();
        gameHangman = new Hangman(HANGMAN_IMAGE_BASE_NAME,
                HANGMAN_IMAGE_DIRECTORY,
                HANGMAN_IMAGE_TYPE);
        hangmanPanel.add(gameHangman);
        add(hangmanPanel, BorderLayout.CENTER);
    }
    
    /**
     * Retrieves the password from the player, constrained by the length and
     * content of the password.
     */
    private void getPassword()
    {
        String [] a = { "hercules", "tarzan", "mulan", "incredibles","pinocchio","aladin","cinderella","frozen","minions","zootopia" };
        Random rand = new Random();
        int n = rand.nextInt(10);
        password = a[n];

        passwordHidden.append(password.replaceAll(".", "*"));
        correct.setText(correct.getText() + passwordHidden.toString());
    }
    
    /**
     * Prompts the user for a new game when one game ends.
     */
    private void newGameDialog()
    {
        int dialogResult = JOptionPane.showConfirmDialog(null, 
                "The password was: " + password +
                "\nWould You Like to Start a New Game?",
                "Play Again?",
                JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION)
            initialize(); // re-initialize the GameBoard
        else
            System.exit(0);
    }
    
    /**
     * A custom MouseListener for the LetterTiles that detects when the user
     * "guesses" (clicks on) a LetterTile and updates the game accordingly.
     */
    private class TileListener implements MouseListener 
    {
        @Override
        public void mousePressed(MouseEvent e) 
        {
            Object source = e.getSource();
            if(source instanceof LetterTile)
            {
                char c = ' ';
                int index = 0;
                boolean updated = false;
                
                // cast the source of the click to a LetterTile object
                LetterTile tilePressed = (LetterTile) source;
                c = tilePressed.guess();
                
                // reveal each instance of the character if it appears in the
                //     the password
                while ((index = password.toLowerCase().indexOf(c, index)) != -1)
                {
                    passwordHidden.setCharAt(index, password.charAt(index));
                    index++;
                    updated = true;
                }
                
                // if the guess was correct, update the GameBoard and check
                //     for a win
                if (updated)
                {
                    correct.setText("Word: " + passwordHidden.toString());
                    
                    if (passwordHidden.toString().equals(password))
                    {
                        gameRack.removeListeners();
                        gameHangman.winImage();
                        newGameDialog();
                    }
                }
                
                // otherwise, add an incorrect guess and check for a loss
                else
                {
                    incorrect.setText("Incorrect: " + ++numIncorrect);
                    
                    if (numIncorrect >= MAX_INCORRECT)
                    {
                        gameHangman.loseImage();
                        gameRack.removeListeners();
                        newGameDialog();
                    }
                    
                    else
                        gameHangman.nextImage(numIncorrect);
                }
            }
        }
        
        // These methods must be implemented in every MouseListener
        //     implementation, but since they are not used in this application, 
        //     they contain no code
        
        @Override
        public void mouseClicked(MouseEvent e) {}  

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}
        
        @Override
        public void mouseExited(MouseEvent e) {}
    }
}