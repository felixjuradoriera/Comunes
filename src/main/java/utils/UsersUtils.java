package utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import dto.User;

public class UsersUtils {
	
	
	private static final String CSV_USERS = "C:"+ File.separator +"BOT" + File.separator +"CONF"+File.separator+ "users.csv";
	
	
	
    public static List<User> readUsers() {
        List<User> users = new ArrayList<>();

        if (!Files.exists(Paths.get(CSV_USERS))) {
            return users; // si no existe, devolvemos lista vacía
        }

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_USERS))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    User user = new User();
                    user.setChatId(Long.valueOf(parts[0].trim()));
                    user.setName(parts[1].trim());
                    users.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static User getUser(String chatId) {
        List<User> users = readUsers();

       for (User user : users) {
		if (String.valueOf(user.getChatId()).equals(chatId)){
			return user;	
		}
       }
       return null; 
    }
    
	 public static void saveUserIfNotExists(User user) {
	        List<User> users = readUsers(); // leer los que ya están

	        // Verificar si ya existe ese chatId
	        boolean exists = users.stream()
	                .anyMatch(u -> u.getChatId().equals(user.getChatId()));

	        if (!exists) {
	            try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_USERS, true))) {
	                writer.write(user.getChatId() + "," + user.getName());
	                writer.newLine();
	                System.out.println("✅ Usuario agregado: " + user.getChatId());
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        } else {
	            System.out.println("ℹ️ Usuario ya existe: " + user.getChatId());
	        }
	    }
	 
	 public static void deleteUserByChatId(String chatIdToDelete) {
		    // 1. Leer todos los usuarios actuales
		    List<User> users = readUsers();

		    // 2. Filtrar fuera el usuario a borrar
		    List<User> updatedUsers = new ArrayList<>();
		    for (User u : users) {
		    	String chatId=u.getChatId().toString();
		        if (!chatId.equals(chatIdToDelete)) {
		            updatedUsers.add(u);
		        }
		    }

		    // 3. Sobrescribir el CSV con los usuarios filtrados
		    try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_USERS, false))) {
		        for (User u : updatedUsers) {
		            writer.write(u.getChatId() + "," + u.getName());
		            writer.newLine();
		        }
		        System.out.println("✅ Usuario eliminado si existía: " + chatIdToDelete);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}


}
