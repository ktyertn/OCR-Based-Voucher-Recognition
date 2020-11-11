package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.sql.ResultSet;

import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY;
import static org.opencv.imgproc.Imgproc.threshold;


public class Controller implements Initializable {

    public static String fis;
    FileChooser fc = new FileChooser();
    @FXML
    private ImageView imageView;
    @FXML
    ComboBox<String> comboBox = new ComboBox<>();
    public TableView<isletme> table;
    public TableColumn<isletme,String> columnName;
    public TableColumn<isletme,String> columnTarih;
    public TableColumn<isletme,String> columnUrun;
    public TableColumn<isletme,String> columnFiyat;
    public TableColumn<isletme,String> columnFis;
    public TableColumn<isletme,String> columnUrunfiyat;
    public TableColumn<isletme,String> columnUrunkdv;
    public TextField isletmeAditext;
    public TextField tarihText;
    public TextArea urunlerText;
    public TextField fiyatText;
    public TextField fisText;
    public TextField textBox;
    private ObservableList<isletme>data;
    private connection conn;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        javafx.stage.Stage.class.getResource("sample.fxml");
        presscomboBox();
        conn=new connection();
    }


    public void presscomboBox(){
        comboBox.getItems().addAll("isletme","tarih","urun","urun_kdv","urun_fiyat","fiyat","fis_no");
    }

    public void pressButton(ActionEvent event){

        File selectedFile = fc.showOpenDialog(null);
        try {
            Image img = SwingFXUtils.toFXImage(ImageIO.read(selectedFile),null);
            imageView.setImage(img);

        }catch (IOException ex){
            System.out.println(ex);
        }

        if(selectedFile != null){
            fis = selectedFile.getAbsolutePath();
        }

        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
        String file =fis;
        Mat src = imread(file,0);
        Mat dst = new Mat();
        Size size = new Size(src.rows()*2, src.rows()*2);
        Imgproc.resize(src, dst, size, 0, 0, Imgproc.INTER_AREA);
        Imgcodecs.imwrite("C:\\Users\\milan\\Desktop\\muronun projeleri\\untitled1\\images\\deneme.jpg",dst);
        file="C:\\Users\\milan\\Desktop\\muronun projeleri\\untitled1\\images\\deneme.jpg";
        src = imread(file,0);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
                new  Size((2*2) + 1, (2*2)+1));
        Imgproc.erode(src, dst, kernel);
        Imgcodecs.imwrite("C:\\Users\\milan\\Desktop\\muronun projeleri\\untitled1\\images\\deneme.jpg",dst);

        /*Imgproc.adaptiveThreshold(src, dst, 160, Imgproc.ADAPTIVE_THRESH_MEAN_C,
                Imgproc.THRESH_BINARY, 11, 13);*/

        System.out.println(Core.VERSION);
        File imageFile = new File("C:\\Users\\milan\\Desktop\\muronun projeleri\\untitled1\\images\\deneme.jpg");
        ITesseract instance = new Tesseract();

        instance.setLanguage("tur");
        try {
            String result = instance.doOCR(imageFile);
            System.out.println(result);
            String [] tmp;
            String isim=null;
            String fis_no=null;
            String tarih=null;
            String toplam=null;
            String urunler=" ";
            String urunler2=" ";
            String urunKDV=" ";
            String urunFiyat=" ";
            tmp=result.split("\n");

            for (int i = 0; i <tmp.length ; i++) {
                if(i==0){
                    isim=tmp[i];
                    System.out.println(isim);
                }if(tmp[i].contains("F1şNo:")){
                    fis_no=tmp[i].substring(tmp[i].indexOf(":")+1,tmp[i].length());
                    System.out.println(fis_no);

                }if(tmp[i].contains("TOPLAM")){
                    toplam=tmp[i].substring(tmp[i].indexOf("M")+2,tmp[i].length());
                    System.out.println(toplam);
                }
                if(!tmp[i].contains("...")&&!tmp[i].contains("*")){
                    char[] tut = tmp[i].toCharArray();
                    System.out.println(tut);
                    for (int j = 0; j < tmp[i].length(); j++) {
                        if (tut[j] == '.') {
                            if (tut[j + 3] == '.') {
                                System.out.println(j);
                                tarih = tmp[i].substring(j - 2, j + 8);
                            }
                        }else if (tut[j] == '/' && j+3<tut.length) {
                            if (tut[j + 3] == '/') {
                                System.out.println(j);
                                tarih = tmp[i].substring(j - 2, j + 8);
                            }
                        }
                    }
                }
            }
            for (int i = 0; i <tmp.length ; i++) {
                if(tmp[i].contains("%")){
                    urunler2=urunler2+tmp[i]+"\n";

                    urunler=tmp[i].substring(0,tmp[i].indexOf("%"));
                    urunKDV=tmp[i].substring(tmp[i].indexOf("%"),tmp[i].indexOf("%")+3);
                    if(tmp[i].charAt(tmp[i].indexOf("%")+4)!='*'){
                        urunFiyat=tmp[i].substring(tmp[i].indexOf("%")+6,tmp[i].length());
                    }else{
                        urunFiyat=tmp[i].substring(tmp[i].indexOf("%")+5,tmp[i].length());
                    }

                    try {
                        Connection con=conn.connect();
                        con.createStatement().executeUpdate(" INSERT INTO me (isletme ,tarih,urun,urun_kdv,urun_fiyat,fiyat,fis_no)" +
                                "VALUES ('"+isim+"','"+tarih+"','"+urunler+"','"+urunKDV+"','"+urunFiyat+"','"+toplam+"','"+fis_no+"')");
                        isletmeAditext.setText(isim);
                        tarihText.setText(tarih);
                        urunlerText.setText(urunler2);
                        fiyatText.setText(toplam);
                        fisText.setText(fis_no);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }


        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }

    }
    public void loadDatafromDatabase(ActionEvent e){
        String searchValue =comboBox.getValue();
        String svalue= textBox.getText();
        Connection con=conn.connect();
        data= FXCollections.observableArrayList();

        try {
            ResultSet rs=con.createStatement().executeQuery("select * from me where "+searchValue+" like '%"+svalue+"%'");
            while(rs.next()){
                data.add(new isletme(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(6),rs.getString(7),rs.getString(5),rs.getString(4)));
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        columnName      .setCellValueFactory(new PropertyValueFactory<>("name"));
        columnTarih     .setCellValueFactory(new PropertyValueFactory<>("tarih"));
        columnUrun      .setCellValueFactory(new PropertyValueFactory<>("urun"));
        columnUrunkdv   .setCellValueFactory(new PropertyValueFactory<>("urun_kdv"));
        columnUrunfiyat .setCellValueFactory(new PropertyValueFactory<>("urun_fiyat"));
        columnFis       .setCellValueFactory(new PropertyValueFactory<>("fis"));
        columnFiyat     .setCellValueFactory(new PropertyValueFactory<>("fiyat"));

        table.setItems(null);
        table.setItems(data);
    }



}
