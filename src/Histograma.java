import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.pow;

/**
 * Created by gustavo.thur on 03/04/2017.
 */
public class Histograma {
    public int saturate(int value){
        if(value > 255)
            return 255;
        else if (value < 0)
            return 0;
        else
            return value;
    }

    public double distance (Color pixel, Color pixel1){
        double dist = Math.sqrt(
                pow((pixel.getRed()-pixel1.getRed()),2)+
                        pow((pixel.getGreen()-pixel1.getGreen()),2)+
                        pow((pixel.getBlue()-pixel1.getBlue()),2));
        return dist;
    }

    public int[] HistogramaCalculadora(BufferedImage image){
        int[] histogram = new int [256];

        for (int y = 0; y < image.getHeight(); y++)
        {
            for (int x = 0; x < image.getWidth(); x++)
            {
                Color color = new Color(image.getRGB(x,y));
                int red = color.getRed();
                histogram[red]+=1;
            }
        }
        return histogram;
    }

    private int Value_Menor(int[] histogram){
        for(int i = 0; i < histogram.length; i++)
        {
            if(histogram[i] != 0)
                return i;
        }
        return 0;
    }

    public int[] Acumulado_calculado(int[] histogram){
        int[] contador = new int[256];
        contador[0] = histogram[0];
        for (int i = 1; i < histogram.length; i++)
        {
            contador[i] = histogram[i] + contador[i-1];
        }
        return contador;
    }

    public int[] Color_Calculate(int[] histogram, int pix){
        int[] colorsMap = new int[256];
        int[] contador = Acumulado_calculado(histogram);
        float Menor = Value_Menor(histogram);

        for (int i = 0; i < histogram.length; i++)
        {
            colorsMap[i] = Math.round(((contador[i] - Menor) / (pix - Menor)) * 255);
        }

        return colorsMap;
    }

    public BufferedImage Equalize (BufferedImage image){
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        int[] histogram = HistogramaCalculadora(image);
        int[] colorsMap = Color_Calculate(histogram, image.getWidth() * image.getHeight());


        for(int y = 0; y < image.getHeight(); y++)
        {
            for(int x = 0; x < image.getWidth(); x++)
            {
                Color color = new Color(image.getRGB(x,y));
                int tom = color.getRed();
                int novo_tom = colorsMap[tom];
                Color novo_cor = new Color(novo_tom, novo_tom, novo_tom);
                out.setRGB(x,y, novo_cor.getRGB());
            }
        }

        return out;
    }

    public void run () throws IOException {
        File PATH = new File("C:\\Users\\gustavo.thur\\IdeaProjects\\Dunno3\\src");
        BufferedImage car = ImageIO.read(new File(PATH, "car.png"));
        BufferedImage cars = ImageIO.read(new File(PATH, "cars.jpg"));
        BufferedImage crowd = ImageIO.read(new File(PATH, "crowd.png"));
        BufferedImage montanha = ImageIO.read(new File(PATH, "montanha.jpg"));
        BufferedImage university = ImageIO.read(new File(PATH, "university.png"));

        BufferedImage equalizedCar = Equalize(car);
        BufferedImage equalizedCars = Equalize(cars);
        BufferedImage equalizedCrowd = Equalize(crowd);
        BufferedImage equalizedMontanha = Equalize(montanha);
        BufferedImage equalizedUniversity = Equalize(university);

        ImageIO.write(equalizedCar, "png", new File(PATH, "equalizedCar.png"));
        ImageIO.write(equalizedCars, "png", new File(PATH, "equalizedCars.png"));
        ImageIO.write(equalizedCrowd, "png", new File(PATH, "equalizedCrowd.png"));
        ImageIO.write(equalizedMontanha, "png", new File(PATH, "equalizedMontanha.png"));
        ImageIO.write(equalizedUniversity, "png", new File(PATH, "equalizedUniversity.png"));
    }

    public static void main (String[] args) throws IOException {
        Histograma atividade = new Histograma();
        atividade.run();
    }
}
