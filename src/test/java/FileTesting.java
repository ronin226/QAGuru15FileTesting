import static com.codeborne.pdftest.assertj.Assertions.assertThat;
import static com.codeborne.xlstest.XLS.containsText;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import org.junit.jupiter.api.Test;
import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import model.Cat;

public class FileTesting {
    @Test
    void ZipTest() throws Exception {
        ZipFile zipf = new ZipFile(new File("src/test/resources/testarchive.zip"));
        InputStream is = ClassLoader.getSystemResourceAsStream("testarchive.zip");
        ZipInputStream zis = new ZipInputStream(is);
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            try (InputStream inputStream = zipf.getInputStream(entry)) {
                if (entry.getName().endsWith(".pdf")) {
                    PDF pdf = new PDF(inputStream);
                    assertThat(pdf).containsExactText("is empty");
                }
                if (entry.getName().endsWith(".xlsx")) {
                    XLS spreadsheet = new XLS(inputStream);
                    assertThat(spreadsheet, containsText("michael"));
                }
                if (entry.getName().endsWith(".csv")) {
                    CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
                    List<String[]> content = reader.readAll();
                    String[] row = content.get(1);
                    assertThat(row[0]).isEqualTo("michael");
                    assertThat(row[1]).isEqualTo("jordan");
                }
            }

        }
    }



    @Test
    void JsonTest() throws Exception {
        File file = new File("src/test/resources/cat.json");
        ObjectMapper objectMapper = new ObjectMapper();
        Cat cat = objectMapper.readValue(file, Cat.class);

        assertThat(cat.name).isEqualTo("marusya");
        assertThat(cat.breed).isEqualTo("siberian");
        assertThat(cat.body.get(0)).isEqualTo("paws");
        assertThat(cat.paws).isEqualTo(4);
        assertThat(cat.color).isEqualTo("blue");
    }

}
