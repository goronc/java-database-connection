import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseConnection {
    
    public static void main(String[] args) {
        Properties properties = new Properties();

        // Charger les propriétés depuis le fichier database.properties
        try (InputStream input = new FileInputStream("database.properties")) {
            properties.load(input);
        } catch (IOException ex) {
            System.out.println("Erreur lors du chargement des propriétés.");
            ex.printStackTrace();
            return;
        }

        // Récupérer les informations de connexion depuis les propriétés
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");

        // Charger le pilote JDBC
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Pilote chargé avec succès !");
        } catch (ClassNotFoundException e) {
            System.out.println("Impossible de charger le pilote JDBC.");
            e.printStackTrace();
            return;
        }

        // Établir la connexion
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            if (connection != null) {
                System.out.println("Connexion réussie à la base de données !");
                //faire les actions que l'ont veut
                ajouterBDD(connection,"tcell", 1, "mort");
                getBDD(connection,"tcell");
                
            } else {
                System.out.println("Échec de la connexion à la base de données.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données.");
            e.printStackTrace();
        }

    }

    // Fonction pour récupérer les données de la table acteurs
    public static void getBDD(Connection connection,String table) {
        String query = "SELECT * FROM " + table;
        
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            

            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                int position = resultSet.getInt("position");
                String etat = resultSet.getString("etat");
                
                // Afficher les résultats
                System.out.println("ID: " + id + ", position: " + position + ", etat: " + etat);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'exécution de la requête.");
            e.printStackTrace();
        }
    }



    public static void ajouterBDD(Connection connection,String table, int position, String etat) {
        String query = "INSERT INTO "+table+" (position, etat) VALUES (?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, position);
            statement.setString(2, etat);
            
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("element ajouter a "+ table);
            }
        } catch (SQLException e) {
            System.out.println("erreur lors de l'insertion a " + table);
            e.printStackTrace();
        }
    }
}


