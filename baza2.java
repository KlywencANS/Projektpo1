import java.io.*;
import java.io.File;
import java.util.*;

public class baza{
    static String filePath;
    static long fileSize;

    public static void main(String[] args) throws IOException{
        while(true)
            menu();
    }
    static void center(String text) {
        int padding = (214 - text.length()) / 2;
        String paddingSpaces = String.format("%" + padding + "s", "");
        System.out.println(paddingSpaces + text);
    }

    static void menu() throws IOException{
        Scanner scanner = new Scanner(System.in);
        String choice;
        clearScreen();

        center("PROJEKT MINI BAZY DANYCH 2024, AUTOR: ALAN KALKOWSKI, NR. INDEKSU: 20449");
        center("===============================");
        center("    1. Otwórz bazę danych");
        center("    2. Utwórz nową bazę danych");
        center("    3. Przeglądaj bazę danych");
        center("    4. Wyjdź z programu");
        center("===============================");

        if(filePath != null)
            System.out.println("Obecnie otwarty plik: "+filePath);

        System.out.println("\nWybierz opcję: ");

        do{
            choice = scanner.next();
        }while(!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equals("4"));

        switch(choice){
            case "1":
                System.out.println("Wybrana opcja 1");
                openDatabase();
                clearScreen();
                break;
            case "2":
                System.out.println("Wybrana opcja 2");
                createDatabase();
                clearScreen();
                break;
            case "3":
                System.out.println("Wybrana opcja 3");
                browseDatabase();
                clearScreen();
                break;
            case "4":
                System.out.println("Wybrana opcja 6");
                System.out.println("\nZamykanie programu");
                System.exit(0);
                break;
        }
    }

    static void openDatabase()throws IOException{
        Scanner scanner = new Scanner(System.in);
        String name;
        boolean valid;

        System.out.println("Wprowadź nazwę pliku: ");
        name = scanner.next();
        valid = checkValidity(name);

        if(!valid) {
            File file = new File(name);
            if(file.exists()) {
                System.out.println("Plik " + name + " został otwarty.");
                filePath = name;
            }
            else{
                System.out.println("Plik "+name+" nie istnieje. Najpierw musisz utworzyć plik (opcja 2).");
            }
        }
        System.out.println("Naciśnij dowolny klawisz, aby kontynuować");
        scanner.next();
    }

    static void createDatabase()throws IOException{
        Scanner scanner = new Scanner(System.in);
        String dbName = "";
        boolean valid;

        do{
            clearScreen();
            for(int i=0;i<25;i++)
                System.out.println();
            System.out.println("Wprowadź nazwę bazy danych: xxxx.dat");
            dbName = scanner.next();
            dbName = dbName.trim();
            valid = checkValidity(dbName);
            System.out.println("Wynik: "+valid);
            System.out.println("S - zatrzymaj, inny - kontynuuj");
            String ws = scanner.next();
            ws = ws.toLowerCase();
            File newFile = new File(dbName);
            if(ws.charAt(0)=='s') {
                if (!newFile.exists()) {
                    newFile.createNewFile();
                    System.out.println("Plik " + dbName + " został utworzony");
                    break;
                } else System.out.println("Plik " + dbName + " nie został utworzony");
            }
        }while(!valid);
        System.out.println("Naciśnij dowolny klawisz, aby kontynuować");
        scanner.next();
        menu();
    }

    static boolean checkValidity(String dbName){
        //xxxx.dat
        boolean valid = true;
        int len = dbName.length();
        System.out.println("Długość łańcucha: "+len);
        if(len<5){
            System.out.println("Nazwa bazy danych jest za krótka");
            return false;
        }
        String extension = ".dat";
        String dbExtension = dbName.substring(len-4);
        if(dbExtension.equals(extension)){
            System.out.println("Rozszerzenie bazy danych: poprawne");
        }else{
            System.out.println("Rozszerzenie bazy danych: NIEPOPRAWNE");
            valid = false;
        }
        String dbNameOnly = dbName.substring(0,len-4);
        System.out.println("Nazwa bazy danych: "+dbNameOnly);
        char[] arr = dbNameOnly.toCharArray();
        for(int i=0;i<len-4;i++){
            if(!Character.isLetterOrDigit(arr[i]))
                System.out.println("Niepoprawny znak w nazwie: "+arr[i]);
            valid = false;
        }
        return valid;
    }

    static void browseDatabase() throws IOException {
        clearScreen();
        Scanner scanner = new Scanner(System.in);
        String input;
        boolean status = true;
        int records = 0, buffer = 0, r;
        ArrayList<Mouse> fromFile = new ArrayList<>();
        Mouse readData = null, m1 = new Mouse();
        ObjectInputStream inputStream = null;

        if (filePath == null) {
            System.out.println("Obecnie nie jest otwarty żaden plik!");
            System.out.println("Naciśnij dowolny klawisz, a następnie ENTER");
            scanner.next();
            menu();
        }

        File file = new File(filePath);
        fileSize = file.length();

        if (fileSize == 0) {
            System.out.println("BRAK DANYCH W PLIKU!");
            System.out.println("Czy chcesz dodać nowy rekord? t - tak; n - nie");
            input = scanner.next();
            input = input.toLowerCase();
            if (input.equals("n")) {
                menu();
            } else if (input.equals("t")) {
                System.out.println("Dodawanie nowego rekordu!");
                m1.inputMouse();
                fromFile.add(m1);
                records++;
            }
        } else {
            inputStream = new ObjectInputStream(new FileInputStream(filePath));
            while (status) {
                try {
                    readData = (Mouse) inputStream.readObject();
                    if (readData != null) {
                        fromFile.add(readData);
                        records++;
                    }
                } catch (ClassNotFoundException e) {
                    System.out.println(e);
                } catch(EOFException e){
                    status = false;
                }
            }
        }

        do {
            r = fromFile.size();
            System.out.println("Liczba rekordów w pliku: " + r);
            System.out.println("Numer rekordu: " + (buffer + 1));
            readData = fromFile.get(buffer);
            System.out.println(readData.printData());

            System.out.println("Y - dane w górę; B - dane w dół; A - dodaj nowy obiekt; D - usuń bieżący obiekt; M - modyfikuj obiekt bazy");
            System.out.println("F - przejdź na początek; L - przejdź na koniec; X - wyjdź z trybu przeglądania I - Sortowanie bazy; K - Usuwanie bazy" );
            System.out.println("Wprowadź operację: ");
            input = scanner.next();
            input = input.toLowerCase();
            switch (input) {
                case "y" -> buffer++;
                case "b" -> buffer--;
                case "a" -> {
                    System.out.println("Dodawanie nowego rekordu!");
                    Mouse newMouse = new Mouse();
                    newMouse.inputMouse();
                    fromFile.add(newMouse);
                    records++;
                    System.out.println("Rekord został dodany pomyślnie.");
                }
                case "d" -> {
                    try {
                        fromFile.remove(buffer);
                        records--;
                        System.out.println("Rekord pomyślnie usunięty z bazy danych");
                    } catch (IndexOutOfBoundsException e) {
                        buffer = 0;
                    }
                }
                case "m" -> {
                    System.out.println("Modyfikowanie rekordu!");

                    // Wybór numeru rekordu do modyfikacji
                    System.out.println("Podaj numer rekordu do modyfikacji (od 1 do " + records + "): ");
                    int recordNumber;
                    while (true) {
                        try {
                            recordNumber = scanner.nextInt();
                            if (recordNumber < 1 || recordNumber > records) {
                                System.out.println("Podaj poprawny numer rekordu (od 1 do " + records + "): ");
                            } else {
                                break;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Podaj poprawny numer rekordu (liczbę całkowitą od 1 do " + records + "): ");
                            scanner.next(); // Czyść bufor wejściowy
                        }
                    }

                    // Wyszukiwanie rekordu do modyfikacji
                    Mouse modifiedMouse = fromFile.get(recordNumber - 1);
                    modifiedMouse.printData(); // Wyświetlenie danych przed modyfikacją

                    // Modyfikacja pola
                    System.out.println("Wybierz pole do modyfikacji (1 - Marka, 2 - Model, 3 - Typ, 4 - DPI, 5 - Cena, 6 - Stan magazynowy): ");
                    int fieldNumber;
                    while (true) {
                        try {
                            fieldNumber = scanner.nextInt();
                            if (fieldNumber < 1 || fieldNumber > 6) {
                                System.out.println("Wybierz poprawne pole do modyfikacji (od 1 do 6): ");
                            } else {
                                break;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Wybierz poprawne pole do modyfikacji (liczbę całkowitą od 1 do 6): ");
                            scanner.next(); // Czyść bufor wejściowy
                        }
                    }

                    // Modyfikacja wybranego pola
                    switch (fieldNumber) {
                        case 1 -> {
                            System.out.println("Aktualna marka: " + modifiedMouse.brand);
                            System.out.println("Podaj nową markę: ");
                            modifiedMouse.brand = scanner.next();
                        }
                        case 2 -> {
                            System.out.println("Aktualny model: " + modifiedMouse.model);
                            System.out.println("Podaj nowy model: ");
                            modifiedMouse.model = scanner.next();
                        }
                        case 3 -> {
                            System.out.println("Aktualny typ: " + modifiedMouse.type);
                            System.out.println("Podaj nowy typ: ");
                            modifiedMouse.type = scanner.next();
                        }
                        case 4 -> {
                            System.out.println("Aktualne DPI: " + modifiedMouse.dpi);
                            System.out.println("Podaj nowe DPI: ");
                            modifiedMouse.dpi = scanner.nextInt();
                        }
                        case 5 -> {
                            System.out.println("Aktualna cena: " + modifiedMouse.price);
                            System.out.println("Podaj nową cenę: ");
                            modifiedMouse.price = scanner.nextInt();
                        }
                        case 6 -> {
                            System.out.println("Aktualny stan magazynowy: " + modifiedMouse.stock);
                            System.out.println("Podaj nowy stan magazynowy: ");
                            modifiedMouse.stock = scanner.nextInt();
                        }
                    }

                    // Wyświetlenie zaktualizowanych danych
                    System.out.println("Rekord został pomyślnie zmodyfikowany:");
                    modifiedMouse.printData();

                    // Zapisanie zaktualizowanych danych do listy
                    fromFile.set(recordNumber - 1, modifiedMouse);
                }
                case "f" -> buffer = 0;
                case "l" -> buffer = r - 1;
                case "i" -> sortDatabase();
                case "k" -> deleteDatabase();
            }
        } while (!input.equals("x"));

        if (inputStream != null) {
            inputStream.close();
        }
        System.out.println("Zapisywanie danych do bazy danych...");
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath));
        for (int i = 0; i < fromFile.size(); i++) {
            outputStream.writeObject(fromFile.get(i));
        }
        System.out.println("Opuszczanie bazy danych. Naciśnij dowolny klawisz");
        outputStream.close();
        scanner.next();
    }

    static void sortDatabase() throws IOException, ArrayIndexOutOfBoundsException {
        clearScreen();
        Scanner scanner = new Scanner(System.in);
        File file = new File(filePath);
        long fileSize = file.length();
        int records = 0;
        String input;
        ArrayList<Mouse> fromFile = new ArrayList<>();
        ArrayList<Mouse> toFile = new ArrayList<>();
        Mouse m1 = new Mouse();

        if(fileSize == 0){
            System.out.println("Brak rekordów w pliku! Sortowanie tablicy niemożliwe!");
            System.out.println("Naciśnij dowolny klawisz, a następnie ENTER");
            scanner.next();
            menu();
        }else {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath));
            boolean status = true;
            while (status) {
                try {
                    m1 = (Mouse) inputStream.readObject();
                } catch (ClassNotFoundException e) {
                    System.out.println(e);
                }catch(EOFException e){
                    status = false;
                }
                if (m1 != null) {
                    fromFile.add(m1);
                    records++;
                }
            }
            System.out.println("SORTOWANIE PLIKU " + filePath);
            inputStream.close();
        }

        Mouse[] mice = new Mouse[fromFile.size()];
        Mouse temp;
        for(int i=0;i<fromFile.size();i++){
            mice[i] = fromFile.get(i);
        }

        System.out.println("Wszystkie rekordy zostały poprawnie odczytane. Według którego pola chcesz posortować bazę danych: ");
        System.out.println("1. Marka");
        System.out.println("2. Model");
        System.out.println("3. Typ");
        System.out.println("4. DPI");
        System.out.println("5. Cena");
        System.out.println("6. Stan magazynowy");
        System.out.println("Wybierz pole: ");
        input = scanner.next();

        switch(input){
            case "1" ->{
                System.out.println("Sortowanie według Marki");
                for(int i=1;i<mice.length;i++){
                    for(int j=0;j<mice.length-i;j++){
                        if (((mice[j].brand).compareTo(mice[j+1].brand)) >0 ) {
                            temp = mice[j];
                            mice[j] = mice[j+1];
                            mice[j+1] = temp;
                        }
                    }
                }
                for(int k=0;k<fromFile.size();k++){
                    try {
                        toFile.add(mice[k]);
                    }catch(ArrayIndexOutOfBoundsException e){
                        System.out.println("Indeks poza zakresem!");
                        System.out.println("Indeks: "+k);
                    }
                }
            }
            case "2" -> {
                System.out.println("Sortowanie według Modelu");
                for(int i=1;i<mice.length;i++){
                    for(int j=0;j<mice.length-i;j++){
                        if (((mice[j].model).compareTo(mice[j+1].model)) >0 ) {
                            temp = mice[j];
                            mice[j] = mice[j+1];
                            mice[j+1] = temp;
                        }
                    }
                }
                for(int k=0;k<fromFile.size();k++){
                    try {
                        toFile.add(mice[k]);
                    }catch(ArrayIndexOutOfBoundsException e){
                        System.out.println("Indeks poza zakresem!");
                        System.out.println("Indeks: "+k);
                    }
                }
            }
            case "3" -> {
                System.out.println("Sortowanie według Typu");
                for(int i=1;i<mice.length;i++){
                    for(int j=0;j<mice.length-i;j++){
                        if (((mice[j].type).compareTo(mice[j+1].type)) >0 ) {
                            temp = mice[j];
                            mice[j] = mice[j+1];
                            mice[j+1] = temp;
                        }
                    }
                }
                for(int k=0;k<fromFile.size();k++){
                    try {
                        toFile.add(mice[k]);
                    }catch(ArrayIndexOutOfBoundsException e){
                        System.out.println("Indeks poza zakresem!");
                        System.out.println("Indeks: "+k);
                    }
                }
            }
            case "4" -> {
                System.out.println("Sortowanie według DPI");
                for(int i=1;i<mice.length;i++){
                    for(int j=0;j<mice.length-i;j++){
                        if (mice[j].dpi > mice[j+1].dpi) {
                            temp = mice[j];
                            mice[j] = mice[j+1];
                            mice[j+1] = temp;
                        }
                    }
                }
                for(int k=0;k<fromFile.size();k++){
                    try {
                        toFile.add(mice[k]);
                    }catch(ArrayIndexOutOfBoundsException e){
                        System.out.println("Indeks poza zakresem!");
                        System.out.println("Indeks: "+k);
                    }
                }
            }
            case "5" ->{
                System.out.println("Sortowanie według Ceny");
                for(int i=1;i<mice.length;i++){
                    for(int j=0;j<mice.length-i;j++){
                        if (mice[j].price > mice[j+1].price) {
                            temp = mice[j];
                            mice[j] = mice[j+1];
                            mice[j+1] = temp;
                        }
                    }
                }
                for(int k=0;k<fromFile.size();k++){
                    try {
                        toFile.add(mice[k]);
                    }catch(ArrayIndexOutOfBoundsException e){
                        System.out.println("Indeks poza zakresem!");
                        System.out.println("Indeks: "+k);
                    }
                }
            }
            case "6" ->{
                System.out.println("Sortowanie według Stanu Magazynowego");
                for(int i=1;i<mice.length;i++){
                    for(int j=0;j<mice.length-i;j++){
                        if (mice[j].stock > mice[j+1].stock) {
                            temp = mice[j];
                            mice[j] = mice[j+1];
                            mice[j+1] = temp;
                        }
                    }
                }
                for(int k=0;k<fromFile.size();k++){
                    try {
                        toFile.add(mice[k]);
                    }catch(ArrayIndexOutOfBoundsException e){
                        System.out.println("Indeks poza zakresem!");
                        System.out.println("Indeks: "+k);
                    }
                }
            }
        }
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath));
        for(int i=0;i<fromFile.size();i++){
            outputStream.writeObject(toFile.get(i));
        }
        System.out.println("Rekordy zostały posortowane i zapisane do pliku");
        System.out.println("Naciśnij dowolny klawisz");
        scanner.next();
    }

    static void deleteDatabase() throws IOException, NullPointerException {
        Scanner scanner = new Scanner(System.in);
        int position;
        String input;
        String name;
        System.out.println("Podaj nazwę bazy danych, wybierz 6 - aby powrócić do menu");
        name = scanner.next();
        File file = new File(name);
        if(name.equals("6")){
            menu();
        }
        if(filePath != null){
            System.out.println("Plik "+name+" jest obecnie otwarty. Czy chcesz kontynuować usuwanie bazy danych? t - tak; n - nie");
            input = scanner.next();
            input = input.toLowerCase();
            if(input.equals("n")){
                menu();
            }else if(input.equals("t")){
                filePath = null;
                file.delete();
                System.out.println("Plik został usunięty, naciśnij dowolny klawisz");
                scanner.next();
                menu();
            }
        }else{
            file.delete();
            System.out.println("Plik został usunięty, naciśnij dowolny klawisz");
            scanner.next();
            menu();
        }
    }

    static boolean clearScreen(){
        System.out.println(new String(new char[15]).replace("\0","\r\n"));
        return true;
    }
}

class Mouse implements Serializable{
    String brand;
    String model;
    String type;
    int dpi;
    int price;
    int stock;
    Mouse(){

    }
    void inputMouse(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Marka: ");
        brand = scanner.next();
        System.out.println("Model: ");
        model = scanner.next();
        System.out.println("Typ: ");
        type = scanner.next();
        System.out.println("DPI: ");
        dpi = scanner.nextInt();
        System.out.println("Cena: ");
        price = scanner.nextInt();
        System.out.println("Stan magazynowy: ");
        stock = scanner.nextInt();
    }
    Mouse printData(){
        System.out.println("Marka: "+brand);
        System.out.println("Model: "+model);
        System.out.println("Typ: "+type);
        System.out.println("DPI: "+dpi);
        System.out.println("Cena: "+price);
        System.out.println("Stan magazynowy: "+stock);
        return null;
    }
}
