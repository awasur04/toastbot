package utils;

import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

public class StrikeDatabase {

    private HashMap<Long, Integer> sDatabase;

    public StrikeDatabase() {
        this.sDatabase = new HashMap<>();
    }

    public void loadData() {
        try (Scanner scanner = new Scanner(Paths.get("strikedata.txt"))) {
            while (scanner.hasNextLine()) {
                String row = scanner.nextLine();
                String[] pieces = row.split(",");
                if (pieces.length > 1) {
                    sDatabase.put(Long.valueOf(pieces[0]), Integer.valueOf(pieces[1]));
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void saveData(HashMap<Long, Integer> data) {
        try (FileWriter myWriter = new FileWriter("strikedata.txt")) {
            for (Long id : data.keySet()) {
                myWriter.write(id + "," + data.get(id) + "\n");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public HashMap<Long, Integer> getDatabase() {
        return this.sDatabase;
    }
}
