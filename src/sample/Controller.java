package sample;

import GoogleAPI.Audio;
import GoogleAPI.GoogleTranslate;
import GoogleAPI.Language;
import javazoom.jl.decoder.JavaLayerException;
import java.io.IOException;
import java.io.InputStream;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.List;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.lang.StringBuffer;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import  javafx.stage.Stage;
import javafx.scene.input.KeyEvent;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import javafx.util.Pair;

import javax.swing.*;

public class Controller {
    @FXML
    private TextField textField;
    @FXML
    private TableView<Word> table;
    @FXML
    private TableColumn<Word,String> TableCl;
    @FXML
    private WebView Explain;
    @FXML
    private Button add;
    @FXML
    private Button edit;
    @FXML
    private Button remove;
    @FXML
    private Button exit;
    private WebEngine engine;
    private List<Word> list;
    private ListWordManager t;
    private  KeyEvent keyevent;
    private static int num;
    private Voice voice;
    private VoiceManager voiceManager;
    private String val;
    private String ex;
    private boolean press;
    private String newString;
    private  BufferedReader file;
    private FileOutputStream File;
    private int count, count1, count2;

    public Controller() {
        num=0;
        val="";
        ex="";
        press=false;
        count=0;
    }
    public int getNum() {
        return num;
    }

    public String getNewString() {
        if(getNum()==0) { newString="C:\\BT lon UET diction\\asg1-rua-d-t\\src\\data\\Anh-Viet.txt";}
        else { newString="C:\\BT lon UET diction\\asg1-rua-d-t\\src\\data\\Viet-Anh.txt";}
        return newString;
    }
    public void Exit() {
        Platform.exit();
        System.exit(0);
    }
    // chuyển anh-việt
    public void Eng() throws Exception {
        num=0;
        initialize();
    }
    public void Viet() throws Exception {
        num=1;
        initialize();
    }
    @FXML
    public void initialize() throws Exception {
        newString=getNewString();
        t = new ListWordManager();
        t.CreatListWord(newString);
        list = new ArrayList<Word>();
        Set<String> set = t.getList1().keySet();
        for (String key : set) {
            Word newWord = new Word(key, t.getList1().get(key));
            list.add(newWord);
        }
        // add list lên 1 tập observable theo dõi sự thay đổi của list
        ObservableList<Word> observableList = FXCollections.observableArrayList(list);
        // đưa ob vào fil để đc phép lọc danh sách
        FilteredList<Word> filteredList = new FilteredList<>(observableList,/*predicate*/ p -> true);
        // slấy các giá trị từ thuộc tính của lớp Word add vào cột của table
        TableCl.setCellValueFactory(new PropertyValueFactory<>("English"));
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(word -> {
                // xét trường hợp ô nhập vào rỗng
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String Word1 = newValue.toLowerCase();
                if (word.getEnglish().toLowerCase().contains(Word1)) {
                    return true;

                }
                return false;
            });
        });
        MouseClick();
        Pressenter();
        SortedList<Word> sortedData = new SortedList<>(filteredList);
        sortedData.comparatorProperty().bind(table.comparatorProperty()); // ràng buộc sự thay đổi của table vào sort
        table.setItems(sortedData);

    }

    public void MouseClick() {

        engine = Explain.getEngine();
        table.setOnMouseClicked(event -> {
            try {
                engine.reload();
                Word chooseWord = table.getSelectionModel().getSelectedItem();
                String explain = t.getList1().get(chooseWord.getEnglish());
                engine.loadContent(explain);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
    public void search() {
        try {
            ObservableList<Word> list1 = table.getItems();
            if (list1.size() == 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notice");
                alert.setHeaderText("Oh!! Sorry :((((");
                alert.setContentText("We don't find your word in dictionary");
                alert.showAndWait();
            }
            else {
                ex= t.getList1().get(val);
                if(ex!=null) { engine.loadContent(ex);}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void Pressenter() {
        textField.setOnKeyPressed(event -> {
            if(event.getCode()== KeyCode.ENTER) {
                press=true;
                val= textField.getText();
                search();
            }
        });
    }
    public void butEnter() {
        press=true;
        val= textField.getText();
        search();
    }
    public void clickspeak() {
        String voice1;
        Word chooseWord = table.getSelectionModel().getSelectedItem();
        System.setProperty("mbrola.base", "FreeTTS\\mbrola");
        voiceManager = VoiceManager.getInstance();
        voice = voiceManager.getVoice("mbrola_us3");
        voice.allocate();
        if (press== false) {
            voice1= chooseWord.getEnglish();
        } else {
            voice1= val;
            press=false;

        }
        voice.speak(voice1);
        voice.allocate();
    }

    public void add(ActionEvent evt) throws Exception {
        newString=getNewString();
        javafx.scene.control.Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add new Word");
        dialog.setHeaderText("Input your Word");
        ButtonType input= new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(input, ButtonType.CANCEL);

        GridPane gridPane= new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(20, 150, 10,10));

        TextField word= new TextField();
        word.setPromptText("input your word");
        TextField ex= new TextField();
        ex.setPromptText("mean");
        TextField pro= new TextField();
        pro.setPromptText("pronuciation");
        TextField kind= new TextField();
        kind.setPromptText("kind of word");

        gridPane.add(new Label("Word: "),0,0);
        gridPane.add(word,1,0);
        gridPane.add(new Label("Mean: "),0,1);
        gridPane.add(ex,1,1);
        gridPane.add(new Label("Pronunciation: "),0,2);
        gridPane.add(pro,1,2);
        gridPane.add(new Label("Kind: "),0,3);
        gridPane.add(kind,1,3);
        dialog.getDialogPane().setContent(gridPane);
        dialog.setResultConverter(dialogBut->{
            if(dialogBut==input) {
                try {
                    file = new BufferedReader(new FileReader(newString));
                    File = new FileOutputStream(newString, true);
                    String line;
                    String input1 ="";
                    input1=System.lineSeparator()+word.getText()+"<html><i>"+word.getText()+" "+pro.getText()+"</i><br/><ul><li><b><i>"+ kind.getText()+"</i></b><ul><li><font color='#cc0000'><b>"+ ex.getText()+"</b></font></li></ul></li></ul></html>";
                    File.write(input1.getBytes());

                    file.close();
                    File.close();
                    System.out.println("oki");
                } catch (Exception e) {
                    System.out.println("Add not word");
                }
            }
            return null;
        });
        Optional<Pair<String, String>> result =dialog.showAndWait();
//        t.CreatListWord("C:\\BT lon UET diction\\asg1-rua-d-t\\src\\Sourse\\Anh-Viet.txt");
        initialize();

    }
    public void edit(ActionEvent evt) throws Exception{
        newString=getNewString();
        count2=0;
        javafx.scene.control.Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("edit Word");
        dialog.setHeaderText("Input your Word");
        ButtonType input= new ButtonType("Edit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(input, ButtonType.CANCEL);

        GridPane gridPane= new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(20, 150, 10,10));

        TextField word= new TextField();
        word.setPromptText("your word");
        TextField ex= new TextField();
        ex.setPromptText("change to");

        gridPane.add(new Label("Word: "),0,0);
        gridPane.add(word,1,0);
        gridPane.add(new Label("Change to: "),0,1);
        gridPane.add(ex,1,1);
        dialog.getDialogPane().setContent(gridPane);
        dialog.setResultConverter(dialogBut->{
            if(dialogBut==input) {
                try
                {
                    file = new BufferedReader(new FileReader(newString));
                    String line;
                    StringBuffer input1=new StringBuffer("");
                    while ((line = file.readLine()) != null)
                    {
                        String ns="", ns1="";
                        if(line.indexOf("<")>0) {
                            ns = line.substring(0, line.indexOf("<")/*tim vi tri*/);
                            ns1 = line.substring(line.indexOf("<"), line.length());
                        }
                        if (ns.equals(word.getText()))
                        {
                            line = ex.getText()+ns1 ;
                            System.out.println("Line changed.");
                        }
                        if(count2==0) {input1.append(line);}
                        else { input1.append(System.lineSeparator()); input1.append(line);}
                        count2++;
                    }
                    FileWriter fw=new FileWriter(newString);
                    fw.write(input1.toString());
                    file.close();
                    fw.close();
                }
                catch (Exception e)
                {
                    System.out.println("Problem reading file.");
                }
            }
            return null;
        });
        Optional<Pair<String, String>> result =dialog.showAndWait();
        initialize();
    }




    public void Remove(ActionEvent evt)  throws Exception {
        newString=getNewString();
        count1=0;
        javafx.scene.control.Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Delete new Word");
        dialog.setHeaderText("Input your Word");
        ButtonType input = new ButtonType("delete", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(input, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField word = new TextField();
        word.setPromptText("input your word");

        gridPane.add(new Label("Word: "), 0, 0);
        gridPane.add(word, 1, 0);

        dialog.getDialogPane().setContent(gridPane);
        dialog.setResultConverter(dialogBut -> {
            if (dialogBut == input) {
                try
                {
                    file = new BufferedReader(new FileReader(newString));
                    String line;
                    StringBuffer input1=new StringBuffer("");
                    while ((line = file.readLine()) != null)
                    {
                        String ns="";
                        if(line.indexOf("<")>0) {
                            ns = line.substring(0, line.indexOf("<")/*tim vi tri*/);
                        }
                        if (ns.equals(word.getText()))
                        {
                            System.out.println(word.getText());
                            line = "";
                            System.out.println("Line deleted.");
                        }
                        if(count1==0) {input1.append(line);}
                        else { input1.append(System.lineSeparator()); input1.append(line);}
                        count1++;
                    }
                    FileWriter fw=new FileWriter(newString);
                    fw.write(input1.toString());
                    file.close();
                    fw.close();
                }
                catch (Exception e)
                {
                    System.out.println("Problem reading file.");
                }
            }
            return null;
        });
        Optional<Pair<String, String>> result =dialog.showAndWait();
        initialize();
    }



    @FXML
    private void exit(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Word");
        alert.setHeaderText("Are you sure want to move this word ?");
        //alert.setContentText("C:/MyFile.txt");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == null && option.get() ==ButtonType.CANCEL) {
            //this.label.setText("No selection!");
        } else if (option.get() == ButtonType.OK) {
            // get a handle to the stage
            Stage stage = (Stage) exit.getScene().getWindow();
            // do what you have to do
            stage.close();
        }

    }
    public void Wiki() {
        String URL="https://vi.wikipedia.org/wiki/";
        Word chooseWord = table.getSelectionModel().getSelectedItem();
        String explain= chooseWord.getEnglish();
        URL+= explain;
        engine=Explain.getEngine();
        engine.load(URL);
    }
    public void ggTrans() throws IOException, JavaLayerException {
        InputStream sound;String str,str1;
        Word chooseWord = table.getSelectionModel().getSelectedItem();
        if (press == false) {
            str1 = chooseWord.getEnglish();
        } else {
            str1 = textField.getText();
            press = false;
        }
        if(textField.getText()!= null) {
            Audio audio = Audio.getInstance();
            if (num == 1) {
                sound = audio.getAudio(str1, Language.VIETNAMESE);
                str = GoogleTranslate.translate("vi", "en", str1);
            } else {
                sound = audio.getAudio(str1, Language.ENGLISH);
                str = GoogleTranslate.translate("en", "vi", str1);
            }
            audio.play(sound);

            engine.loadContent(str);
        }
    }
}
