package builders;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

import autoMode.Environment;
import building.Home;
import character.QCharacter;
import city.Map;
import log.LoggerUtility;
import utils.BoundedCounter;
import utils.Coordinates;

public class QCharacterBuilder {
	
	public static String[] FILE_HEADER_MAPPING_NAME = {"name"};
	public static String[] FILE_HEADER_MAPPING_FIRSTNAME = {"gender","firstName"};
	
	private static Logger logger = LoggerUtility.getLogger(QCharacterBuilder.class);
	
	private QCharacter character;
	
	public QCharacterBuilder(QCharacter character){
		this.character = character;
	}
	
	/**
	 * initialize the different component of the character by using the other methodes
	 */
	public QCharacter createCharacter(){
		character.setAlive(true);
		character.setNbOfDeath(0);
		initCharacterName();
		initCharacterFirstName();
		character.setAge(randomSelection(10, 100));
		initLife();
		character.setRewardPriority(0);
		character.setGoingHome(false);
		character.setPath(new ArrayList<Coordinates>());
		if (character != null)
			logger.info("Character: " + character.getFirstName() + " " + character.getName() + " created");
		else
			logger.fatal("A character hasn't been created");
		return character;
	}
	
	/**
	 * This method initialize character's ID
	 * init with the population, when we assign character's home/work
	 */
	public void initCharacterID(){
		String id = "" + character.hashCode();
		character.setId(id);
	}
	
	/**
	 * This methode initialize the 3 BoundedCounter of the character
	 */
	public void initLife(){
		character.setLife(new BoundedCounter[3]);
		for (int i = 0; i < 3; i++) {
			character.setLife(new BoundedCounter(75, 0, 100), i);
		}
	}
	
	/**
	 * random name initialization by reading name.csv
	 */
	public void initCharacterName(){
		
		try{
			CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING_NAME);
			
			FileReader fileReader = new FileReader("./res/name.csv");
			CSVParser csvFileParser = new CSVParser(fileReader, csvFileFormat);
			
			List<CSVRecord> csvRecords = csvFileParser.getRecords();
			
			//random selection of name, first name and gender
			int randomLineForName = randomSelection(0, csvRecords.size()-1);
			
			//research in file
			for(int i=0; i<=randomLineForName; i++){
				CSVRecord record = csvRecords.get(i);
				if(i==randomLineForName){
					character.setName(record.get("name"));
				}
			}
			
			
			
			csvFileParser.close();
			fileReader.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * random firstName and gender initialization by ready firstName.csv
	 */
	public void initCharacterFirstName(){
		
		try{
			CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING_FIRSTNAME);
			
			FileReader fileReader = new FileReader("./res/firstName.csv");
			CSVParser csvFileParser = new CSVParser(fileReader, csvFileFormat);
			
			List<CSVRecord> csvRecords = csvFileParser.getRecords();
			
			//random selection of name, first name and gender
			int randomLineForFirstName = randomSelection(0, csvRecords.size()-1);
			
			//research in file
			for(int i=0; i<=randomLineForFirstName; i++){
				CSVRecord record = csvRecords.get(i);
				if(i==randomLineForFirstName){
					character.setFirstName(record.get("firstName"));
					String boolGender = record.get("gender");
					if(boolGender.equals("1"))
						character.setGender(true);
					if(boolGender.equals("0"))
						character.setGender(false);
				}
			}
			
			csvFileParser.close();
			fileReader.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * init the character's home
	 * @param map
	 */
	public QCharacter initCharacterHome(Map map){
		int home;
		do{
			home = randomSelection(0, map.getHomeList().size()-1);
		}while(map.getHomeList().get(home).isFull()); 
		character.setHome(map.getHomeList().get(home));
		map.getHomeList().get(home).addUser();
		//initialise la position du perso à l'adresse de son domicile
		character.setPosition(map.getHomeList().get(home).getAddress());
		
		return character;
	}
	
	public QCharacter initCharacterEnvironment(Map map, Home home){
		character.setEnvironment(new Environment(map, home));
		character.setInitialPosition(home.getAddress());
		character.setCurrentState(character.getEnvironment().getState(home.getAddress().getX(), home.getAddress().getY()));

		return character;
	}
	
	/**
	 * found a random number betwin min and max
	 * @param min
	 * @param max
	 * @return random number found
	 */
	public int randomSelection(int min, int max){
		int random;
		Random rand = new Random();
		
		random = rand.nextInt(max - min +1) + min;
		
		return random;
	}
}
