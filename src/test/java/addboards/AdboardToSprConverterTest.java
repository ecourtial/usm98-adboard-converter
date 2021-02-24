package addboards;

import com.ecourtial.usm98textures.tools.BinaryService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AdboardToSprConverterTest {

    @Test
    public void testConvert() throws IOException, Exception {
        // Create a basic BMP (same as in the toBmpConverter test)
        String bmpHexContent = "42 4D 46 00 00 00 00 00 00 00 36 00 00 00 28 00 00 00 02 00 00 00 02 00 00 00 01 00 18 00 00 00 00 00 10 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 02 04 B5 AA 33 2A 00 00 AA 33 2A 02 04 B5 00 00";
        BinaryService binaryService = new BinaryService();
        String outputPath = "src/test/assets/outputBmpTest2.bmp";
        binaryService.writeHexString(outputPath, bmpHexContent.replace(" ", ""));
        
        Map<String, String> palette =  new HashMap < > ();
        palette.put("42-51-170", "35");
        palette.put( "181-4-2", "38");
        
        // Test
        AdboardToSprConverter converter = new AdboardToSprConverter();
        assertEquals(
                "35383835",
                converter.convert(palette, outputPath, 2, 2)
        );
    }
}
