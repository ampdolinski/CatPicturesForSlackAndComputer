package _Z6_Kotki;

import com.google.gson.Gson;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackMessage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        Gson gson = new Gson();

        for (int i = 1; i < 2; i++) {
            Cat cat = getCat(gson);
            createAndSaveImage(cat, i);
            SlackApi api = new SlackApi("https://hooks.slack.com/services/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");

            SlackMessage wiadomosc = new SlackMessage("Koteł nr " + i);
            SlackAttachment zalacznik = new SlackAttachment("Test2");
            zalacznik.setAuthorIcon(":smile:");

            zalacznik.setImageUrl(cat.file);
            wiadomosc.addAttachments(zalacznik);
            wiadomosc.setIcon("<3");

            api.call(wiadomosc);
            //            api.call(new SlackMessage(cat.file));
        }

    }

    private static Cat getCat(Gson gson) throws IOException {
        String link = "https://aws.random.cat/meow";
        URL obj = new URL(link);
        URLConnection con = obj.openConnection();
        con.setRequestProperty("User-Agent", "Chrome");
        InputStream in = con.getInputStream();
        Scanner scanner = new Scanner(in);

        return gson.fromJson(scanner.nextLine(), Cat.class);
    }

    private static void createAndSaveImage(Cat cat, int i) throws IOException {
        System.out.println(cat.file);

        BufferedImage image = ImageIO.read(new URL(cat.file));

        System.out.printf("Rozdzielczość obrazka %d: %d x %d%n", i, image.getHeight(), image.getWidth());

        int lastDot = cat.file.lastIndexOf(".");
        String extention = cat.file.substring(lastDot + 1);

        File outputfile = new File("/Users/LAPI/Desktop/What does the cat say/cat_" + i + "."
                + extention);

//        to rozwiązanie też spoko, ale ogranicza się tylko do ostatnich
//        3 liter, a mogą być 4 litery rozszerzenia
//         cat.file.subSequence(cat.file.length()-3, cat.file.length()));

        ImageIO.write(image, extention, outputfile);

        System.out.println("Rozmiar pliku: " + outputfile.length()/1024 + " KB");

    }

}
