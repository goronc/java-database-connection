import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseConnection {
    public static void main(String[] args) {
        Properties properties = new Properties();


        try (InputStream input = new FileInputStream("database.properties")) {
            properties.load(input);
        } catch (IOException ex) {
            System.out.println("Erreur chargement propriétés.");
            ex.printStackTrace();
            return;
        }


        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Pilote OK");
        } catch (ClassNotFoundException e) {
            System.out.println("Echec chargement Pilote");
            e.printStackTrace();
            return;
        }


        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            if (connection != null) {
                System.out.println("Connexion BDD OK");
                
                // création de la requete
                Statement statement = connection.createStatement();
                String query = "SELECT numAct,nomAct,prenomAct FROM acteurs";
                
                // executer la requête
                ResultSet resultSet = statement.executeQuery(query);
                
                // recuperation des données
                while (resultSet.next()) {

                    int numAct = resultSet.getInt("numAct");
                    String nomAct = resultSet.getString("nomAct");
                    String prenomAct = resultSet.getString("prenomAct");
                    
                    // affichage des données
                    System.out.println("ID: " + numAct + ", Nom: " + nomAct + ", Prénom: " + prenomAct);
                }
                
                // fermer les connections
                resultSet.close();
                statement.close();
            } else {
                System.out.println("Echec connexion BDD");
            }
        } catch (SQLException e) {
            System.out.println("Erreur connexion BDD");
            e.printStackTrace();
        }


    }
}
