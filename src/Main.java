import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fi.jyu.mit.ohj2.Help;
import fi.jyu.mit.ohj2.Mjonot;
import fi.jyu.mit.ohj2.Syotto;


/**
 * Made for April Fool's 2021.
 * Has a group of commands, the description of which you can get by "help commandname" or "? commandname":
 * Write, Randomize, Exit
 * @author WindySilver
 * @version 1.4.2021
 */
public class Main {


    /**
     *  Executes a command.
     */
    public interface CommandInterface {
        /**
         * @param parameters Parameters
         * @return Returns something
         */
        String execute(String parameters);
    }


    /**
     * Gives help.
     * @author WindySilver
     * @version 1.4.2021
     *
     */
    public static class Helper implements CommandInterface {
        
        private Help helper = null;
        private ByteArrayOutputStream bos = new ByteArrayOutputStream();
        
        /**
         * @param string The name of the file that is opened.
         */
        public Helper(String string) {
            try {
                helper = new Help(string);
            } catch (IOException e) {
                System.err.println(e);
            }
            helper.setOut(new PrintStream(bos));
        }

        @Override
        public String execute(String parametrit) {
            bos.reset();
            helper.printMatchingTopics(parametrit.toUpperCase());
            return bos.toString();
        }
        
    }


    /**
     * Writes either 55 bananas or something else, depending on the input input.
     * The first word's first letter is uppercase and the writing ends with a dot.
     * Format inputting: "numberword", e.g. "300orange", "4apple", "17banana".
     * @author WindySilver
     * @version 1.4.2021
     *
     */
    public static class Writer implements CommandInterface {
        @Override
        public String execute(String parameter) {
            return (write(parameter));
        }
        
        private static String write(String parameter) {
            int length = Mjonot.erotaInt(parameter, 0);
            int sizelength = String.valueOf(length).length();
            String word = null;
            if (length != 0) {
            word = parameter.substring(sizelength);
            }
            else {
                length = 55;
                word = parameter;
            }
            if (parameter == "") word = "banana";
            StringBuilder output = new StringBuilder(word.substring(0, 1).toUpperCase() + word.substring(1));
            for (int i = 1; i<length;i++) {
                output.append(" " + word);
            }
            output.append(".");
            return(output.toString());
        }
    }


    /**
     * Writes either 55 randomized bananas, oranges and pineapples or something else, depending on the input input.
     * The first word's first letter is uppercase and the writing ends with a dot.
     * Format inputting: "numberword1 word2 word3", e.g. "300orange banana", "4apple pineapple grape pear orange", "17banana orange pineapple".
     * @author WindySilver
     * @version 1.4.2021
     *
     */
    public static class Randomizer implements CommandInterface {
        @Override
        public String execute(String parameter) {
            return (write(parameter));
        }
        
        private static String write(String parameter) {
            int length = Mjonot.erotaInt(parameter, 0);
            int sizelength = String.valueOf(length).length();
            String word = null;
            if (length != 0) {
            word = parameter.substring(sizelength);
            }
            else {
                length = 55;
                word = parameter;
            }
            if (parameter == "") word = "banana orange pineapple";
            List<String> words = separateWords(word);
            return (randomize(words, length));
        } 
        
        private static List<String> separateWords(String parameter) {
            List<String> result = new ArrayList<String>();
            StringBuilder editing = new StringBuilder(parameter).append(" ");
            while(editing.indexOf(" ") != -1) {
                result.add(editing.substring(0, editing.indexOf(" ")));
                editing = editing.delete(0, editing.indexOf(" ")+1);
            }
            return result;
        }
        
        private static String randomize(List<String> list, int length) {
            Random random = new Random();
            String first = list.get(random.nextInt(list.size()));
            StringBuilder output = new StringBuilder(first.substring(0, 1).toUpperCase() + first.substring(1));
            for (int i = 1; i<length;i++) {
                output.append(" " + list.get(random.nextInt(list.size())));
            }
            output.append(".");
            return(output.toString());
        }
    }
    
    /**
     * The exit command.
     * @author WindySilver
     * @version 1.4.2021
     *
     */
    public static class Exit implements CommandInterface {
        @Override
        public String execute(String parameter) {
            return (leave("Exiting..."));
        }
        
        private static String leave(String parameter) {
            System.exit(0);
            return (parameter);
        }
    }


    /**
     * The command's name and function.
     */
    public static class Command {
        
        private String name;
        private CommandInterface command;
        
        /**
         * @param name The command's name
         * @param command The command's interface
         */
        public Command(String name, CommandInterface command) {
            this.name = name;
            this.command = command;
        }
        
        /**
         * @return name
         */
        public String getName()
        {
            return name;
        }
        
        /**
         * @return Command interface
         */
        public CommandInterface getCommand() {
            return command;
        }
    }


    /**
     * Lista komennoista ja metodit etsimiseksi ja suorittamiseksi.
     */
    public static class Commands {
        private static List<Command> commands = new ArrayList<Command>();

        /**
         * @param s User input
         * @return The interpreted input
         */
        public String interpret(String s) {
            Command rightCommand = null;
            String end = "";
            String begin = "";
            if(isHelp(s) && s.indexOf(" ") == -1)
                {
                rightCommand = commands.get(commands.size()-1);
                return rightCommand.command.execute(s);
                }
            
            if (s.indexOf(" ") != -1) end = s.substring(s.indexOf(" ")+1);
            if (end == "") begin = s;
            else begin = s.substring(0, s.indexOf(" ")).toLowerCase();
            
            for(int i = 0;i<commands.size();i++)
            {
                if(isRight(commands.get(i).getName(), begin)) { rightCommand = commands.get(i); break;}
            }
            
            if (rightCommand == null) return ("I don't know the command " + begin + "!");
            return rightCommand.command.execute(end);
        }


        private boolean isHelp(String s) {
            return (isRight("?", s) || isRight("help", s));
        }


        /**
         * @param commandName The command's name
         * @param beginning The input
         * @return Is it the right command or not
         */
        public boolean isRight(String commandName, String beginning) {
            if (beginning.length()>commandName.length()) return false;
            for (int i=0;i<beginning.length();i++)
            {
                if(beginning.charAt(i) != commandName.charAt(i)) return false;
            }
            return true;
        }


        /**
         * Adds a command to the the list.
         * @param command The command that gets added.
         */
        public void add(Command command) {
            commands.add(command);
        }
    }


    /**
     * The main program.
     * @param args Not used.
     */
    public static void main(String[] args) {
        Commands commands = new Commands();
        Helper hjelp = new Helper("command.txt");
        commands.add(new Command("write", new Writer()));
        commands.add(new Command("randomize", new Randomizer()));
        commands.add(new Command("exit", new Exit()));
        commands.add(new Command("?", hjelp));
        commands.add(new Command("help", hjelp));

        String s;

        while (true) {
            s = Syotto.kysy("Give a command");
            if (s.equals("")) break;
            String result = commands.interpret(s);
            System.out.println(result);
        }
    }
}